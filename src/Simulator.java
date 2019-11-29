import ProcessStuff.Process;
import ProcessStuff.ProcessGenerator;
import ProcessStuff.ProcessManager;
import commands.IOEvent;
import memory.PageTableEntry;

import java.util.ConcurrentModificationException;
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
    static int count = 0;
    String name;

    public Simulator(List<Process> startList, ProcessGenerator pGen)
    {
        this.startList = startList;
        procMan.initProcMan(startList);
        this.pGen = pGen;
        name = "Sim #" + count;
        count++;
        System.out.println("[SIMULATOR] Creating sim: " + name);
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
            try
            {
                procMan.runForTime(new IOEvent());
            }
            catch(ConcurrentModificationException e)
            {
                System.out.println("ERROR: \n" + e.getMessage());
            }
            /*if(procMan.getRemaining() == 1 && test)
            {
                addList = pGen.generateRandomProcess(5);
                procMan.addProcesses(addList);
                test = false;

                System.out.println("\n\n[SIMULATOR] ADDING NEW PROCESSES\n\n");
                for(Process proc : addList)
                    System.out.println(proc.toString() + "\n");

            }*/
        }

        System.out.println("[SIMULATOR]" + name + " Completed batch of generated processes");
        doneList = procMan.getDone();
        System.out.println("[SIMULATOR]" + name + " List of done procs:");
        for(Process proc : doneList)
            System.out.println(proc.getProgName());

        //debugPageList = Process.memoryMan.getNotFreePages();
        /*if(!debugPageList.isEmpty())
        {
            System.out.println("\n[SIMULATOR]" + name + " List of pages still in use");
            for (PageTableEntry pte : debugPageList)
                System.out.println(pte.toString());
        }*/
    }
}
