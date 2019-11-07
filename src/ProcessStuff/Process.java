package ProcessStuff;

import commands.Exe;
import commands.Operation;
import commands.Out;
import commands.Yield;

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
    States state;
    String progName;
    Integer runtime;
    Integer memory;
    Integer programCounter = 0;
    Integer priority = 0; //I don't know what should determine this, so for now it shall remain unused
    //TODO: add register simulations


    List<Operation> operations;

    public Process(States state, String progName, Integer runtime, Integer memory, List<Operation> operations, Integer priority)
    {
        this.state = state;
        this.progName = progName;
        this.runtime = runtime;
        this.memory = memory;
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
        else
        {

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
        return "-------PROGRAM INFO-------"
                + "\nState: " + state.toString()
                + "\nName: " + progName
                + "\nPriority: " + priority
                + "\nTotal Runtime: " + runtime.toString()
                + "\nMemory: " + memory.toString()
                + "\nOperations: \n" + longString;
    }

}
/**
 * p6 = 3
 * p5 = 1
 * p4 = 2
 * p3 = 0
 * p2 = 3
 * p1 = 0
 *
 *
 */