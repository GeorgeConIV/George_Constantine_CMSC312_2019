package commands;

public class Exe implements Operation
{

    Integer cycleCount;
    Integer cyclesRemaining;
    public Exe()
    {
        cycleCount = 0;
        cyclesRemaining = 0;
    }

    @Override
    public Integer getCyclesRemaining()
    {
        return cyclesRemaining;
    }

    @Override
    public void Run()
    {

    }

    @Override
    public String toString()
    {
        return "EXE";
    }
}
