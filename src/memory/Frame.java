package memory;

public class Frame
{
    private Integer frameID;
    private Boolean free;
    public Frame(Integer frameID)
    {
        this.frameID = frameID;
        free = true;
    }

    public Integer getFrameID()
    {
        return frameID;
    }

    public Frame allocate()
    {
        if(free)
        {
            free = false;
            return this;
        }
        else
        {
            System.out.println("[MEMORY] Memory error: attempting to allocate memory that is allocated");
            return this;
        }
    }

    public void deallocate()
    {
        if(!free)
        {
            free = true;
        }
        else
        {
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
