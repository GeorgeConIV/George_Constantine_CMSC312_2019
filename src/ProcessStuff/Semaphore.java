package ProcessStuff;

import commands.Operation;

import java.util.ArrayList;
import java.util.List;

public class Semaphore
{
    List<Process> waitingQueue = new ArrayList<>();
    Character associatedVar;
    boolean availible;
    boolean semDebug = false;
    Process active;

    public Semaphore(Character associatedVar)
    {
        this.associatedVar = associatedVar;
        availible = true;
    }


    /**
     * allows a process to take control of a semaphor
     * @param caller used to track the active process of the semaphor
     */
    synchronized public void waitSem(Process caller)
    {
        if(availible && waitingQueue.isEmpty())
        {
            if(OSGlobals.debug && semDebug)
                System.out.println("[SEMAPHOR] Process: " + caller.getProgName() + " taking control of semaphor " + associatedVar);
            active = caller;
            availible = false;
        }
        else
        {
            if(OSGlobals.debug&& semDebug)
                System.out.println("[SEMAPHOR] Process: " + caller.getProgName() + " being added to wait queue of semaphor " + associatedVar);
            waitingQueue.add(caller);
        }
    }

    /**
     * releases control of the semaphore and gets the next one in the waiting queue, if there is any in it.
     */
    synchronized public void signalSem() {
        if (!waitingQueue.isEmpty()){
            if(OSGlobals.debug&& semDebug)
                System.out.println("[SEMAPHOR] Process: " + active.getProgName() + " releasing control of semaphor " + associatedVar);
            active = waitingQueue.get(0);
            waitingQueue.remove(active);
            if(OSGlobals.debug&& semDebug)
                System.out.println("[SEMAPHOR] Process: " + active.getProgName() + " taking control of semaphor " + associatedVar);
        }
        else
        {
            //active = caller;
            if(OSGlobals.debug&& semDebug)
                System.out.println("[SEMAPHOR] Process: " + active.getProgName() + " releasing control of semaphor " + associatedVar);
        }
        availible = true;
    }

    synchronized public Process getCurrentProcess()
    {
        Process temp = active;
        //used to release
        if(active.isOrphan())
            signalSem();
        if(active.getState().equals(Process.States.EXIT))
            signalSem();

        return temp;
    }

    public boolean isAvailible() {
        return availible;
    }

    public Character getAssociatedVar()
    {
        return associatedVar;
    }

}
