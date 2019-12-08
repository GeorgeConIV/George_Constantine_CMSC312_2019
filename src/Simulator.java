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
    String name;

    public Simulator(List<Process> startList, ProcessGenerator pGen, ProcessManager procMan)
    {
        this.startList = startList;
        this.procMan = procMan;
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
        int i = 100;
        while(i>0)
        {
            /*if(!threadEn[count-1])
            {
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e){}
            }*/
            while (!procMan.checkComplete())
            {
                /*if(!threadEn[count-1])
                {
                    try {
                        Thread.sleep(1000);
                    } catch(InterruptedException e){}
                }*/
                try
                {
                    procMan.runForTime(new IOEvent());
                }
                catch (ConcurrentModificationException e)
                {
                    System.out.println("ERROR: \n" + e.getMessage());
                }

            }
            //if(genMore)
            //{
                procMan.addProcesses(pGen.generateRandomProcess(5));
                genMore = false;
            //}
            i--;


        }

        System.out.println("[SIMULATOR]" + name + " Completed batch of generated processes");
        doneList = procMan.getDone();
        System.out.println("[SIMULATOR]" + name + " List of done procs:");
        for(Process proc : doneList)
            System.out.println(proc.getProgName());
    }
}
