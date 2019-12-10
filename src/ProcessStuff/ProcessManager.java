package ProcessStuff;

import commands.IOEvent;
import commands.IOOp;
import commands.Yield;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

public class ProcessManager
{
    boolean verbose = false;

    final Integer timeConstant = 25;
    Integer timeRemaining = timeConstant;
    Integer totalProcCount;
    boolean semAdded = true;

    List<Process> readyQueue = new ArrayList<>();
    List<Process> done = new ArrayList<>();
    List<Process> waitingQueue = new ArrayList<>();
    List<Process> running = new ArrayList<>();
    List<Process> IOQueue = new ArrayList();
    List<Process> parents = new ArrayList();
    List<Semaphore> sems = new ArrayList<>();

    Process active;

    public ProcessManager() {}

    /**
     * initializes the process manager
     * @param waitingQueue
     * @throws ConcurrentModificationException
     */
    public void initProcMan(List<Process> waitingQueue) throws ConcurrentModificationException
    {
        this.waitingQueue = waitingQueue;

        for(Process p : waitingQueue)
            if(!p.hasParent())
                parents.add(p);

        for(Process proc : waitingQueue)
            proc.setState(Process.States.WAIT);

        for(Process p : waitingQueue)
            if(p.getMemSpace().isEmpty())
                waitingQueue.remove(p);

        this.running.add(waitingQueue.get(0));
        this.running.get(0).setState(Process.States.RUN);
        this.active = this.running.get(0);
        this.waitingQueue.remove(0);
        totalProcCount = waitingQueue.size();

    }

    /**
     * IO manager. Similar to the scheduler, except that it's for IO operations.
     * @param io
     */
    synchronized public void handleIOInterrupt(IOEvent io)
    {
        if(!running.isEmpty())
        {
            removefromRunning(active);
            if(active.getCurrentOp() instanceof  IOOp)
            {
                addToIOQueue(active);
            }
            else
            {
                addToWaitingQueue(active);
            }


        }

        active = IOQueue.get(0);

        addToRunning(active);
        removeFromIOQueue(active);

        while(io.getClockCycles() > 0)
        {
            if(active.getState() == Process.States.EXIT && !done.contains(active))
            {
                removefromRunning(active);
                addToDone(active);
                if(active.hasChild())
                {
                    killChild(active);
                }
                active.killProc();
                if(!IOQueue.isEmpty())
                {
                    active = IOQueue.get(0);
                    addToRunning(active);
                    removeFromIOQueue(active);
                }
            }
            active.runProcess();
            io.decrementClockCycles();
        }
            removefromRunning(active);
            addToIOQueue(active);

            if(!waitingQueue.isEmpty())
            {
                active = waitingQueue.get(0);
            }
            else if(!IOQueue.isEmpty())
            {
                active = IOQueue.get(0);
            }
            else
            {}

            addToRunning(active);
            removeFromWaitingQueue(active);
    }

    /**
     * round robin task scheduler.
     * @param io an object that determines if an io interupt occurs
     */
    synchronized public void runForTime(IOEvent io)
    {
        boolean testy = true;
        if(io.getTriggered() && !IOQueue.isEmpty())
        {
            handleIOInterrupt(io);
        }
        while(timeRemaining > 0 && testy)
        {
            if(active.getState() == Process.States.EXIT && !done.contains(active))
            {
                removefromRunning(active);
                addToDone(active);
                if(active.hasChild())
                {
                    killChild(active);
                }
                active.killProc();
                if(!waitingQueue.isEmpty())
                {
                    active = waitingQueue.get(0);
                    addToRunning(active);
                    removeFromWaitingQueue(active);
                }
                testy = false;
            }

            else if(active.getCurrentOp() instanceof Yield)
            {
                getNewActive();
            }

            //if the instruction is IO, it gets added to the IO queue, and wait for an IO interupt
            else if(active.getCurrentOp() instanceof IOOp && !IOQueue.contains(active))
            {
                removefromRunning(active);
                addToIOQueue(active);
                if(!waitingQueue.isEmpty())
                {
                    active = waitingQueue.get(0);
                    addToRunning(active);
                    removeFromWaitingQueue(active);
                }
            }

            active.runProcess();

            timeRemaining--;
        }

        timeRemaining = timeConstant;

        if(testy)
        {
            getNewActive();
        }
    }

    // process manager is considered complete when all processes without children are finished.
    // If their child processes aren't done, too bad. They don't get to finish
    public boolean checkComplete()
    {
        boolean parentsDone = true;
        for(Process p : parents)
            if(p.getState() != Process.States.EXIT) {
                parentsDone = false;
                break;
            }

        return parentsDone;
    }

    private Process getNewActive()
    {
        if(!running.isEmpty() && !waitingQueue.isEmpty())
        {
            addToWaitingQueue(running.get(0));
            removefromRunning(running.get(0));

            active = waitingQueue.get(0);

            addToRunning(waitingQueue.get(0));
            removeFromWaitingQueue(waitingQueue.get(0));
        }

        return active;
    }

    public void orderWaiting()
    {
        Collections.sort(waitingQueue);
        if(waitingQueue.size() > 1) {
            if(OSGlobals.debug)
                System.out.println("-----------NEW WAITING QUEUE ORDER--------");
            for (Process p : waitingQueue) {
                if(OSGlobals.debug)
                    System.out.println(p.getProgName());
            }
        }
    }

    private void killChild(Process daddy)
    {

        if(daddy.getChild().hasChild())
            killChild(daddy.getChild());
        if(waitingQueue.contains(daddy.getChild()))
            waitingQueue.remove(daddy.getChild());
        if(IOQueue.contains(daddy.getChild()))
            IOQueue.remove(daddy.getChild());
        if(running.contains(daddy.getChild()))
            running.remove(daddy.getChild());
        if(readyQueue.contains(daddy.getChild()))
            readyQueue.remove(daddy.getChild());

        daddy.killChild();
    }

    //TODO: add the proper initialization stuff such that the new procs actually run
    public void addProcesses(List<Process> newProcs)
    {
        for(Process proc : newProcs)
        {
            addToWaitingQueue(proc);
            if(!proc.hasParent())
                parents.add(proc);
            //only adds processes if there is enough memory
            if(proc.getMemSpace().isEmpty())
            {
                waitingQueue.remove(proc);
            }


        }
    }

    public int getRemaining()
    {
        return IOQueue.size() + waitingQueue.size();
    }

    private void addToReadyQueue(Process proc)
    {
        proc.setState(Process.States.READY);
        readyQueue.add(proc);
    }

    private void removeFromReadyQueue(Process proc)
    {
        readyQueue.remove(proc);
    }

    private void addToWaitingQueue(Process proc)
    {
        if(proc.getState() != Process.States.EXIT && !waitingQueue.contains(proc))
        {
            proc.setState(Process.States.WAIT);
            orderWaiting();
            waitingQueue.add(proc);
            if(verbose)
                System.out.println("[Scheduler]Added process: " + proc.getProgName() + " to waiting queue");
        }
    }

    private void removeFromWaitingQueue(Process proc)
    {
        waitingQueue.remove(proc);
        if(verbose)
            System.out.println("[Scheduler]Removed process: " + proc.getProgName() + " from waiting queue");
    }

    private void addToRunning(Process proc)
    {
        if(!running.contains(proc))
        {
            proc.setState(Process.States.RUN);
            running.add(proc);
            if(verbose)
                System.out.println("[Scheduler]Added process: " + proc.getProgName() + " to running queue");
        }
    }

    private void removefromRunning(Process proc)
    {
        running.remove(proc);
        if(verbose)
            System.out.println("[Scheduler]Removed process: " + proc.getProgName() + " from running queue");
    }

    public void addToDone(Process proc)
    {
        if(!done.contains(proc))
        {
            OSGlobals.procsCompleted++;
            proc.setState(Process.States.EXIT);
            done.add(proc);
            if(OSGlobals.debug)
                System.out.println("[Scheduler]----------------------Process: " + proc.getProgName() + " is done!---------------");
        }
    }

    private void addToIOQueue(Process proc)
    {
        if(proc.getState() != Process.States.EXIT && !IOQueue.contains(proc))
        {
            proc.setState(Process.States.WAIT);
            IOQueue.add(proc);
            if(verbose)
                System.out.println("[Scheduler]Added process: " + proc.getProgName() + " to IO queue");
        }
    }
    private void removeFromIOQueue(Process proc)
    {
        IOQueue.remove(proc);
        if(verbose)
            System.out.println("[Scheduler]Removed process: " + proc.getProgName() + " from IO queue");
    }

    public Process getActive()
    {
        return active;
    }



    public List<Process> getDone()
    {
        return done;
    }
}
