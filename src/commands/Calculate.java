package commands;

public class Calculate implements Operation
{
    Integer cycleCount;
    Integer cyclesRemaining;
    public Calculate(Integer cycleCount)
    {
        this.cycleCount = cycleCount;
        this.cyclesRemaining = cycleCount;
    }

    public void Run()
    {
        cyclesRemaining--;
    }

    public Integer getCyclesRemaining()
    {
        return cyclesRemaining;
    }

    @Override
    public String toString()
    {
        return ("CALCULATE ".concat(cyclesRemaining.toString()));
    }


}
