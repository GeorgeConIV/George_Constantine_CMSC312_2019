package commands;

public class IOOp implements Operation
{
    Integer cycleCount;
    Integer cyclesRemaining;

    public IOOp(Integer cycleCount)
    {
        this.cyclesRemaining = cycleCount;
        this.cycleCount = cycleCount;
    }

    public void Run()
    {
        cyclesRemaining--;
    }

    @Override
    public Integer getCyclesRemaining()
    {
        return cyclesRemaining;
    }

    @Override
    public String toString()
    {
        return ("I/O ".concat(cyclesRemaining.toString()));
    }
}
