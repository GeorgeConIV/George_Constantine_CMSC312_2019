package commands;

public class Yield implements Operation
{
    int currentCycles = 1;
    public Yield()
    {
    }

    @Override
    public Integer getCyclesRemaining()
    {
        return 1;
    }

    public boolean getIO()
    {
        return false;
    }
    public Character getCritVar() {return '@';}
    public boolean critical() { return false; }
    @Override
    public void Run()
    {
        currentCycles--;
    }

    @Override
    public String toString()
    {
        return "YIELD";
    }
}
