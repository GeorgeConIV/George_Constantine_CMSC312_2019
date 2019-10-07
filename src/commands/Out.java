package commands;

public class Out implements Operation
{
    Integer cycleCount;
    Integer cyclesRemaining;
    public Out(Integer cycleCount)
    {
        this.cycleCount = cycleCount;
        this.cyclesRemaining = cycleCount;
    }

    @Override
    public Integer getCyclesRemaining()
    {
        return cyclesRemaining;
    }

    public boolean getIO()
    {
        return false;
    }

    @Override
    public void Run()
    {
        cyclesRemaining--;
    }

    @Override
    public String toString()
    {
        return "OUT " + cyclesRemaining.toString();
    }
}
