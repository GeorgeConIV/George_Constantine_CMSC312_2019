import ProcessStuff.Process;
import ProcessStuff.ProcessManager;

import java.util.ArrayList;
import java.util.List;

public class Simulator
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
        while(!procMan.checkComplete())
        {
            procMan.runForTime();
            System.out.println(procMan.getActive().toString());
        }
    }

}
