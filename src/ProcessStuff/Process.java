package ProcessStuff;

import commands.Exe;
import commands.Operation;
import commands.Out;
import commands.Yield;
import memory.PageTable;

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
    //TODO: add register simulations

    static PageTable memoryMan = new PageTable();

    List<Operation> operations;

    public Process(States state, String progName, Integer runtime, Integer memory, List<Operation> operations, Integer priority)
    {
        this.state = state;
        this.progName = progName;
        this.runtime = runtime;
        this.memory = memory;
        memoryMan.allocateMem(memory);
        this.operations = operations;
        this.priority = priority;
        System.out.println("Created process: " + progName);
    }

    public void runProcess()
    {
        if(state == States.RUN)
        {
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
    }

    public Process getChild()
    {
        return child;
    }

    public boolean hasChild()
    {
        return hasChild;
    }

    public boolean hasParent()
    {
        return hasParent;
    }

    @Override
    public int compareTo(Process proc)
    {
        return priority.compareTo(proc.getPriority());
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