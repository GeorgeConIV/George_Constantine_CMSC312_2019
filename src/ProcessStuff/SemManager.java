package ProcessStuff;

import java.util.ArrayList;
import java.util.List;

public class SemManager
{
    List<Semaphore> sems = new ArrayList<>();
    Semaphore semI = new Semaphore('i');
    Semaphore semJ = new Semaphore('j');
    Semaphore semX = new Semaphore('x');
    Semaphore semY = new Semaphore('y');
    Semaphore semW = new Semaphore('w');
    Semaphore semZ = new Semaphore('z');


    public SemManager()
    {
        sems.add(semI);
        sems.add(semJ);
        sems.add(semX);
        sems.add(semY);
        sems.add(semW);
        sems.add(semZ);
    }

    public List<Semaphore> getListOfSems()
    {
        return sems;
    }

    public Semaphore getNeeded(Character needed)
    {
        for(Semaphore s : sems)
        {
            if(needed == s.getAssociatedVar()) {
                return s;
            }
        }
        return new Semaphore('@');
    }

    public boolean checkWaiting(Character need, Process proc)
    {
        Semaphore sem = getNeeded(need);
        return sem.waitingQueue.contains(proc);
    }

    public boolean check(Character need)
    {
        Semaphore sem = getNeeded(need);
        return sem.active.getState().equals(Process.States.EXIT);

    }
}
