package commands;

public class Yield implements Operation
{
    Integer cycleCount;
    Integer cyclesRemaining;
    public Yield(Integer cycleCount)
    {
        this.cycleCount = cycleCount;
        this.cyclesRemaining = cycleCount;
    }

    @Override
    public Integer getCyclesRemaining()
    {
        return cyclesRemaining;
    }

    @Override
    public void Run()
    {
        cyclesRemaining--;
    }

    @Override
    public String toString()
    {
        return "YIELD " + cyclesRemaining.toString();
    }
}
