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
    public boolean critical() { return false; }
    public void Run()
    {
        cyclesRemaining--;
    }

    public boolean getIO()
    {
        return true;
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
