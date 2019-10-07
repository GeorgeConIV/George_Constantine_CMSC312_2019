package ProcessStuff;

import commands.Exe;
import commands.Operation;

import java.util.List;

public class Process
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
    Integer priority; //I don't know what should determine this, so for now it shall remain unused
    //TODO: add register simulations


    List<Operation> operations;

    public Process(States state, String progName, Integer runtime, Integer memory, List<Operation> operations)
    {
        this.state = state;
        this.progName = progName;
        this.runtime = runtime;
        this.memory = memory;
        this.operations = operations;
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
            else if(operations.get(programCounter).getCyclesRemaining() == 0)
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
                + "\nTotal Runtime: " + runtime.toString()
                + "\nMemory: " + memory.toString()
                + "\nOperations: \n" + longString;
    }

}
