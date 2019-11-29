package ProcessStuff;

import commands.Operation;
import commands.Out;
import commands.Yield;
import memory.PageTable;
import memory.PageTableEntry;

import java.util.ArrayList;
import java.util.List;

public class Process implements Comparable<Process>
{
    public static enum States
    {
        NEW {
            @Override
            public String toString(){
                return "NEW";
            }
        }, READY{
        @Override
            public String toString(){
                return "READY";
            }
        }, RUN{
        @Override
            public String toString(){
                return "RUN";
            }
        }, WAIT{
            @Override
            public String toString(){
                return "WAIT";
            }
        }, EXIT{
            @Override
            public String toString(){
                return "EXIT";
            }
        }
    }
    private States state;
    private String progName;
    private Integer runtime;
    private Integer memory;
    private Integer programCounter = 0;
    private Integer priority = 0;
    private Process child;
    private boolean hasChild = false;
    private Process parent;
    private boolean hasParent = false;
    private boolean semAdded = true;
    private Pipe pipe= new Pipe();
    private String data;
    private int cycleCount = 0;

    public static PageTable memoryMan = new PageTable();

    List<Operation> operations;
    private List<PageTableEntry> memSpace;
    static List<Semaphore> sems = new ArrayList<>();
    Semaphore semNeeded = new Semaphore('@');
    static Semaphore semI = new Semaphore('i');
    static Semaphore semJ = new Semaphore('j');
    static Semaphore semX = new Semaphore('x');
    static Semaphore semY = new Semaphore('y');
    static Semaphore semW = new Semaphore('w');
    static Semaphore semZ = new Semaphore('z');

    public Process(States state, String progName, Integer runtime,
                   Integer memory, List<Operation> operations, Integer priority, PageTable memoryMan)
    {
        this.state = state;
        this.progName = progName;
        this.runtime = runtime;
        this.memory = memory;
        this.memoryMan = memoryMan;
        memSpace = this.memoryMan.allocateMem(memory);
        this.operations = operations;
        this.priority = priority;
        System.out.println("[PROCESS] Created process: " + progName);

        sems.add(semI);
        sems.add(semJ);
        sems.add(semX);
        sems.add(semY);
        sems.add(semW);
        sems.add(semZ);
    }

    public void runProcess()
    {
        if(state == States.RUN)
        {
            cycleCount++;
            /**
             * critical section resolving
             */
            if(getCurrentOp().critical())
            {
                for(Semaphore s : sems)
                {
                    if(getCurrentOp().getCritVar() == s.getAssociatedVar()) {
                        semNeeded = s;
                        break;
                    }
                }
                if(getCurrentOp().getCyclesRemaining()==0)
                {
                    semNeeded.signalSem(this);
                    //System.out.println("\n SIGNAL \n" + this);
                    semAdded = true;
                }
                else if(semAdded)
                {
                    semNeeded.waitSem(this);
                    //System.out.println("\n WAIT \n" + this);
                    semAdded = false;
                }
            }

            /**
             * Sending data to child at an arbitrary point in execution
             */
            if(programCounter == 1 && getCurrentOp().getCyclesRemaining() == 0)
            {
                if(hasChild)
                {
                    pipe.send(progName + " run: " + runtime.toString());
                }
            }

            /**
             * normal operations
             */
            if(programCounter == (operations.size()))
            {
                setState(States.EXIT);
            }
            else if(operations.get(programCounter) instanceof Out)
            {
                System.out.println(this.toString());
                programCounter++;
            }
            else if(operations.get(programCounter).getCyclesRemaining() == 0 || operations.get(programCounter) instanceof Yield)
            {
                programCounter++;
            }
            else
            {
                if(hasParent && pipe.isReciever(this) && pipe.isData())
                {
                    data = pipe.recieve();
                }

                if(!getCurrentOp().critical())
                    operations.get(programCounter).Run();
                else if(semNeeded.getCurrentProcess() == this)
                    operations.get(programCounter).Run();
            }
        }


    }

    public Operation getCurrentOp()
    {
        if(state!=States.EXIT)
        {
            return operations.get(getProgramCounter());
        }
        else
        {
            return null;
        }
    }

    public int getCycleCount()
    {
        return cycleCount;
    }

    public Integer getProgramCounter()
    {
        if(programCounter == operations.size())
        {
            return programCounter-1;
        }
        else
        {
            return programCounter;
        }
    }

    public void setState(States state)
    {
        this.state = state;
    }

    public States getState()
    {
        return state;
    }

    public String getProgName()
    {
        return progName;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setParent(Process proc)
    {
        parent = proc;
        hasParent = true;
    }

    public void killChild()
    {
        hasChild = false;
        memoryMan.deallocateMem(child.getMemSpace());
    }

    public List<PageTableEntry> getMemSpace()
    {
        return memSpace;
    }

    public Process getParent()
    {
        return parent;
    }

    public void createChild(Process proc)
    {
        child = proc;
        child.setParent(this);
        hasChild = true;
        pipe = new Pipe(this, child);
        child.setPipe(pipe);
    }

    public void setPipe(Pipe pip)
    {
        pipe = pip;
    }

    public Process getChild()
    {
        return child;
    }

    public boolean hasChild()
    {
        return hasChild;
    }

    public void killProc()
    {
        memoryMan.deallocateMem(memSpace);
        if(hasParent)
        {
            parent.hasChild = false;
        }
        if(hasChild)
        {
            killChild();
        }
    }

    public boolean hasParent()
    {
        return hasParent;
    }

    @Override
    public int compareTo(Process proc)
    {
        if(this.cycleCount > (proc.getCycleCount() + 1000))
        {
            return 1;
        }
        else
        {
            return priority.compareTo(proc.getPriority());
        }
    }

    @Override
    public String toString()
    {
        String longString = "";
        for(Operation ops : operations)
        {
            longString = longString.concat("\n" + ops.toString());
        }
        String str = "-------PROGRAM INFO-------"
                + "\nState: " + state.toString()
                + "\nName: " + progName
                + "\nPriority: " + priority
                + "\nTotal Runtime: " + runtime.toString()
                + "\nMemory: " + memory.toString()
                + "\nOperations: \n" + longString;
        if(hasChild)
            str = str.concat("\n" + progName +" CHILD:\n" + child.toString());

        return str;
    }

}