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
    private boolean isOrphan=false;
    private Mailbox mail;
    private int recievedData = -1;

    public static PageTable memoryMan;
    ProcessManager procMan;

    List<Operation> operations;
    private List<PageTableEntry> memSpace;
    Semaphore semNeeded = new Semaphore('@');
    SemManager sems;

    public Process(States state, String progName, Integer runtime,
                   Integer memory, List<Operation> operations, Integer priority, PageTable memoryMan, ProcessManager procMan, SemManager sems, Mailbox mail)
    {
        this.state = state;
        this.progName = progName;
        this.runtime = runtime;
        this.memory = memory;
        this.memoryMan = memoryMan;
        memSpace = this.memoryMan.allocateMem(memory);
        this.operations = operations;
        this.priority = priority;
        this.procMan = procMan;
        this.sems = sems;
        this.mail = mail;
        if(OSGlobals.debug)
            System.out.println("[PROCESS] Created process: " + progName);


    }

    public Process(States state, Integer runtime,
                   Integer memory, List<Operation> operations, Integer priority, PageTable memoryMan, ProcessManager procMan, SemManager sems)
    {
        this.state = state;
        this.runtime = runtime;
        this.memory = memory;
        this.memoryMan = memoryMan;
        memSpace = this.memoryMan.allocateMem(memory);
        this.operations = operations;
        this.priority = priority;
        this.procMan = procMan;
        this.sems = sems;
        if(OSGlobals.debug)
            System.out.println("[PROCESS] Created process: ");


    }

    synchronized public void runProcess()
    {
        if(state == States.RUN)
        {
            OSGlobals.cycleCount++;
            cycleCount++;
            /**
             * critical section resolving
             */
            if(getCurrentOp().critical())
            {
                semNeeded = sems.getNeeded(getCurrentOp().getCritVar());
                //mail = sems.getNeededMailbox(getCurrentOp().getCritVar());
                if(getCurrentOp().getCyclesRemaining()==0) //!sems.checkWaiting(getCurrentOp().getCritVar(), this)
                {
                    semNeeded.signalSem();
                    //System.out.println("\n SIGNAL \n" + this);
                    semAdded = true;
                }
                else if(semAdded || sems.check(semNeeded.associatedVar))
                {
                    semNeeded.waitSem(this);
                    //System.out.println("\n WAIT \n" + this);
                    semAdded = false;
                }
                if(getCurrentOp().getCyclesRemaining() == 25)
                {
                    if (mail.needsOwner)
                    {
                        mail.setOwner(this);
                        int dat = (int) (Math.random() * (100));
                        mail.setData(this, dat);
                    }
                    else
                    {
                        if(recievedData != mail.readData(this))
                        {
                            recievedData = mail.readData(this);
                        }
                    }
                }
                else if(getCurrentOp().getCyclesRemaining() < 25)
                {
                    if(recievedData != mail.readData(this))
                    {
                        if(OSGlobals.debug)
                            System.out.println(getProgName() + " recieved data: " + recievedData + " on group: " + mail.associatedVar);
                        recievedData = mail.readData(this);
                    }
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
                try {
                    if (mail.owner.equals(this))
                        mail.removeOwner();
                } catch(NullPointerException e) {}
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
        if(mail.owner.equals(this))
            mail.removeOwner();
        if(child.hasChild)
            child.killChild();
        hasChild = false;
        child.isOrphan = true;
        child.setState(States.EXIT);
        memoryMan.deallocateMem(child.getMemSpace());
    }

    public boolean isOrphan()
    {
        return isOrphan;
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

    public List<PageTableEntry> getMemSpace()
    {
        return memSpace;
    }

    public void killProc()
    {
        memoryMan.deallocateMem(memSpace);
        setState(States.EXIT);
        if(hasParent)
        {
            parent.hasChild = false;
        }
        if(hasChild)
        {
            killChild();
        }
    }

    public void setProgName(String progName)
    {
        this.progName = progName;
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
            str = str.concat("\n" + progName +"'s CHILD:\n" + child.getProgName());

        return str;
    }

}