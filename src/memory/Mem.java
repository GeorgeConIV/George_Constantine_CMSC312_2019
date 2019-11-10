package memory;

public class Mem implements Runnable
{
    PageTable pt;

    public Mem(PageTable pt)
    {
        this.pt = pt;
    }

    @Override
    public void run()
    {

    }

}
