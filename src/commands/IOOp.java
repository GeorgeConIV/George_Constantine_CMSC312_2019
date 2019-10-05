package commands;

public class IOOp implements Operation
{
    Integer cycleCount;
    Integer remainingCycles;

    public IOOp(Integer cycleCount)
    {
        this.remainingCycles = cycleCount;
        this.cycleCount = cycleCount;
    }

    public void Run()
    {

    }

    @Override
    public String toString()
    {
        return ("I/O ".concat(remainingCycles.toString()));
    }
}
