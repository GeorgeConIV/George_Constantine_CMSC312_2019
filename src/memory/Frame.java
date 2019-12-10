package memory;

import ProcessStuff.OSGlobals;

public class Frame
{
    private Integer frameID;
    private Boolean free;
    boolean memDebug = false;
    public Frame(Integer frameID)
    {
        this.frameID = frameID;
        free = true;
    }

    public Integer getFrameID()
    {
        return frameID;
    }

    /**
     * This is the equivalent to writing data to a frame in memory
     */
    public Frame allocate()
    {
        if(free)
        {
            free = false;
            return this;
        }
        else
        {
            if(OSGlobals.debug && memDebug)
                System.out.println("[MEMORY] Memory error: attempting to allocate memory that is allocated");
            return this;
        }
    }

    /**
     * deletes the data in a frame and deallocates that frame in memory
     */
    public void deallocate()
    {
        if(!free)
        {
            free = true;
        }
        else
        {
            if(OSGlobals.debug && memDebug)
                System.out.println("[MEMORY] Memory error: attempting to deallocate memory that is deallocated");
        }
    }

    public boolean getFree()
    {
        return free;
    }

    @Override
    public String toString()
    {
        return "frame id: " + frameID.toString() + " free status: " + free.toString();
    }
}
