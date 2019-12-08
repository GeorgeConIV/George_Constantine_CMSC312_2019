import ProcessStuff.OSGlobals;
import ProcessStuff.Process;
import ProcessStuff.ProcessGenerator;
import ProcessStuff.ProcessManager;
import commands.IOEvent;
import memory.PageTableEntry;

import java.util.ConcurrentModificationException;
import java.util.List;

import static ProcessStuff.OSGlobals.genMore;
import static ProcessStuff.OSGlobals.threadEn;

public class Simulator implements Runnable
{
    List<Process> startList;
    List<Process> addList;
    List<Process> doneList;

    List<PageTableEntry> debugPageList;

    ProcessManager procMan;
    boolean test = true;
    ProcessGenerator pGen;
    static int count = 0;
    int local;
    String name;

    public Simulator(List<Process> startList, ProcessGenerator pGen, ProcessManager procMan)
    {
        this.startList = startList;
        this.procMan = procMan;
        procMan.initProcMan(startList);
        this.pGen = pGen;
        name = "Sim #" + count;
        local = count;
        count++;
        if(OSGlobals.debug)
            System.out.println("[SIMULATOR] Creating sim: " + name);
    }

    public void simulate()
    {
        run();
    }

    @Override
    public void run() {
        iterate();
    }

    public void iterate()
    {
        while (OSGlobals.enable)
        {
            if (check())
            {
                while (!procMan.checkComplete())
                {
                    try {
                        procMan.runForTime(new IOEvent());
                    } catch (ConcurrentModificationException e) {
                        if (OSGlobals.debug)
                            System.out.println("ERROR: \n" + e.getMessage());

                    }
                }
                if (genMore)
                {
                    procMan.addProcesses(pGen.generateRandomProcess(OSGlobals.startProcs));
                }

            }
            else
            {
                try {
                    Thread.sleep(10);
                } catch(InterruptedException e) {}
            }

        }

        if(OSGlobals.debug) {
            System.out.println("[SIMULATOR]" + name + " Completed batch of generated processes");
            doneList = procMan.getDone();
            System.out.println("[SIMULATOR]" + name + " List of done procs:");
            for (Process proc : doneList)
                System.out.println(proc.getProgName());
        }
    }
    private synchronized boolean check()
    {
        return threadEn[local];
    }
}
