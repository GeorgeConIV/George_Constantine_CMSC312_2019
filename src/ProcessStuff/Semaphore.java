package ProcessStuff;

import commands.Operation;

import java.util.ArrayList;
import java.util.List;

public class Semaphore
{
    List<Process> waitingQueue = new ArrayList<>();
    Character associatedVar;
    boolean availible;
    Process active;

    public Semaphore(Character associatedVar)
    {
        this.associatedVar = associatedVar;
        availible = true;
    }


    public void waitSem(Process caller)
    {
        if(availible && waitingQueue.isEmpty())
        {
            System.out.println("[SEMAPHOR] Process: " + caller.getProgName() + " taking control of semaphor " + associatedVar);
            active = caller;
            availible = false;
        }
        else
        {
            System.out.println("[SEMAPHOR] Process: " + caller.getProgName() + " being added to wait queue of semaphor " + associatedVar);
            waitingQueue.add(caller);
        }
    }

    public void signalSem(Process caller) {
        if (!waitingQueue.isEmpty()){
            System.out.println("[SEMAPHOR] Process: " + active.getProgName() + " releasing control of semaphor " + associatedVar);
            active = waitingQueue.get(0);
            waitingQueue.remove(active);
            System.out.println("[SEMAPHOR] Process: " + active.getProgName() + " taking control of semaphor " + associatedVar);
        }
        else
        {
            //active = caller;
            System.out.println("[SEMAPHOR] Process: " + caller.getProgName() + " releasing control of semaphor " + associatedVar);
        }
        availible = true;
    }

    public Process getCurrentProcess()
    {
        return active;
    }

    public boolean isAvailible() {
        return availible;
    }

    public Character getAssociatedVar()
    {
        return associatedVar;
    }

}
