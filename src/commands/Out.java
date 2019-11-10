package commands;

public class Out implements Operation
{
    public Out()
    {
    }

    @Override
    public Integer getCyclesRemaining()
    {
        return 0;
    }

    public boolean getIO()
    {
        return false;
    }
    public boolean critical() { return false; }
    @Override
    public void Run()
    {

    }

    @Override
    public String toString()
    {
        return "OUT";
    }
}
