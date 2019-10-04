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

    @Override
    public String toString()
    {
        return cyclesRemaining.toString();
    }


}
