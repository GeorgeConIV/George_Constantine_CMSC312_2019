package commands;

public interface Operation
{
    public boolean getIO();
    public void Run();
    public boolean critical();
    public Integer getCyclesRemaining();
}
