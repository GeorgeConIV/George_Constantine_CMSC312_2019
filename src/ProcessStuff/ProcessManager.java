package ProcessStuff;

import java.util.ArrayList;
import java.util.List;

public class ProcessManager
{
    final Integer timeConstant = 50;
    Integer timeRemaining = timeConstant;

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
        for(Process proc : waitingQueue)
        {
            proc.setState(Process.States.WAIT);
        }
        this.running.add(waitingQueue.get(0));
        this.running.get(0).setState(Process.States.RUN);
        this.active = this.running.get(0);
        this.waitingQueue.remove(0);
    }

    //TODO: fix this whole ass file, its a mess and done poorly,
    //TODO: but I am too brain dead to do it properly right now
    public void runForTime()
    {
        boolean testy = true;
        while(timeRemaining > 0 && testy)
        {
            if(active.getState() == Process.States.EXIT)
            {
                removefromRunning(active);
                addToDone(active);
                if(!waitingQueue.isEmpty())
                {
                    active = waitingQueue.get(0);
                    addToRunning(active);
                    removeFromWaitingQueue(active);
                }
                System.out.println("\n\n" + (done.get(done.size()-1).getProgName()) + " is done!");
                System.out.println((done.get(done.size()-1)).toString());
                testy = false;
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
        if(waitingQueue.isEmpty())
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
        addToWaitingQueue(running.get(0));
        removefromRunning(running.get(0));

        active = waitingQueue.get(0);

        addToRunning(waitingQueue.get(0));
        removeFromWaitingQueue(waitingQueue.get(0));

        return active;
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
        proc.setState(Process.States.WAIT);
        waitingQueue.add(proc);
    }

    private void removeFromWaitingQueue(Process proc)
    {
        waitingQueue.remove(proc);
    }

    private void addToRunning(Process proc)
    {
        proc.setState(Process.States.RUN);
        running.add(proc);
    }

    private void removefromRunning(Process proc)
    {
        running.remove(proc);
    }

    public void addToDone(Process proc)
    {
        done.add(proc);
    }

    private void addToIOQueue(Process proc)
    {
        IOQueue.add(proc);
    }

    public Process getActive()
    {
        return active;
    }
}
