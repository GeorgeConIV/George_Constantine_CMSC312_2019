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
    public Character getCritVar() {return '@';}

    public boolean critical() { return false; }
    public boolean getIO()
    {
       return false;
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
