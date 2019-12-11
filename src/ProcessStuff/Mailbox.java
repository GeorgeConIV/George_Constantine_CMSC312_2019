package ProcessStuff;

public class Mailbox
{
    Integer data;
    Integer readCount = 0;
    Character associatedVar = '@';
    boolean newData = false;
    boolean needsOwner = true;
    Process owner;

    public Mailbox(Character associatedVar)
    {
        this.data = 0;
        this.associatedVar = associatedVar;
    }

    public synchronized void setData(Process caller, Integer data)
    {
        if(caller.equals(owner))
        {
            this.data = data;
            readCount = 0;
            newData = true;

            if(OSGlobals.debug)
                System.out.println(caller.getProgName() + " sent data: " + data + " on group: " + associatedVar);
        }
    }

    public synchronized Integer readData(Process caller)
    {
        readCount++;
        if(owner.getState().equals(Process.States.EXIT))
            needsOwner = true;

        return data;
    }


    public synchronized void removeOwner()
    {
        needsOwner = true;
    }

    public synchronized void setOwner(Process own)
    {
        owner = own;
        needsOwner = false;
    }
}
