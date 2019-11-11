import ProcessStuff.Process;
import ProcessStuff.ProcessManager;
import commands.IOEvent;

import java.util.List;

public class Simulator implements Runnable
{
    List<Process> startList;
    ProcessManager procMan = new ProcessManager();

    public Simulator(List<Process> startList)
    {
        this.startList = startList;
        procMan.initProcMan(startList);
    }

    public void simulate()
    {
        run();
    }

    @Override
    public void run()
    {
        while(!procMan.checkComplete())
        {
            procMan.runForTime(new IOEvent());
        }
        System.out.println("[SIMULATOR] Completed batch of generated processes");
    }
}
