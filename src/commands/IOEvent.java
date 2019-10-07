package commands;

public class IOEvent
{
    Boolean triggered;
    Integer clockCycles;

    public IOEvent()
    {
        if(Math.random() > 0.1)
        {
            triggered = true;
            clockCycles = (int)(Math.random() * 50);
            //System.out.println("[I/O]IOEvent generated! for " + clockCycles + " cycles.");
        }
        else
        {
            triggered = false;
            clockCycles = 0;
        }
    }

    public Boolean getTriggered()
    {
        return triggered;
    }

    public Integer getClockCycles()
    {
        return clockCycles;
    }

    public void decrementClockCycles()
    {
        clockCycles--;
    }

}

