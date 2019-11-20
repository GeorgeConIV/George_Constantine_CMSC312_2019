import ProcessStuff.Process;
import ProcessStuff.ProcessGenerator;
import ProcessStuff.ProcessManager;
import commands.IOEvent;
import memory.PageTableEntry;

import java.util.List;

public class Simulator implements Runnable
{
    List<Process> startList;
    List<Process> addList;
    List<Process> doneList;

    List<PageTableEntry> debugPageList;

    ProcessManager procMan = new ProcessManager();
    boolean test = true;
    ProcessGenerator pGen;

    public Simulator(List<Process> startList, ProcessGenerator pGen)
    {
        this.startList = startList;
        procMan.initProcMan(startList);
        this.pGen = pGen;
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
            if(procMan.getRemaining() == 1 && test)
            {
                addList = pGen.generateRandomProcess(5);
                procMan.addProcesses(addList);
                test = false;

                System.out.println("\n\n[SIMULATOR] ADDING NEW PROCESSES\n\n");
                for(Process proc : addList)
                    System.out.println(proc.toString() + "\n");

            }
        }

        System.out.println("[SIMULATOR] Completed batch of generated processes");
        doneList = procMan.getDone();
        System.out.println("[SIMULATOR] List of done procs:");
        for(Process proc : doneList)
            System.out.println(proc.getProgName());

        debugPageList = Process.memoryMan.getNotFreePages();
        if(!debugPageList.isEmpty())
        {
            System.out.println("\n[SIMULATOR] List of pages not deallocated");
            for (PageTableEntry pte : debugPageList)
                System.out.println(pte.toString());
        }
    }
}
