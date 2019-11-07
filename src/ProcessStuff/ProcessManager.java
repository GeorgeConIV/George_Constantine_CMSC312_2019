package ProcessStuff;

import commands.IOEvent;
import commands.IOOp;
import commands.Yield;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProcessManager
{
    boolean verbose = false;

    final Integer timeConstant = 50;
    Integer timeRemaining = timeConstant;
    Integer totalProcCount;

    List<Process> readyQueue = new ArrayList<>();
    List<Process> done = new ArrayList<>();
    List<Process> waitingQueue = new ArrayList<>();
    List<Process> running = new ArrayList<>();
    List<Process> IOQueue = new ArrayList();

    Process active;

    public ProcessManager() {}

    //TODO: add the gosh darn threading stuff
    public void initProcMan(List<Process> waitingQueue)
    {
        this.waitingQueue = waitingQueue;
        totalProcCount = waitingQueue.size();
        for(Process proc : waitingQueue)
        {
            proc.setState(Process.States.WAIT);
        }
        this.running.add(waitingQueue.get(0));
        this.running.get(0).setState(Process.States.RUN);
        this.active = this.running.get(0);
        this.waitingQueue.remove(0);
    }

    public void handleIOInterrupt(IOEvent io)
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

    public void runForTime(IOEvent io)
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

    public boolean checkComplete()
    {
        if(done.size() == totalProcCount)
        {
            return true;
        }
        else
        {
            return false;
        }
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
            System.out.println("-----------NEW WAITING QUEUE ORDER--------");
            for (Process p : waitingQueue) {
                System.out.println(p.getProgName());
            }
        }
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
            proc.setState(Process.States.EXIT);
            done.add(proc);
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
}
