/**
 * George Constantine, iteration 2
 *
 * Usage: in order to use your own files, you need to import it with a single process per file.
 * If you want to just have my program auto generate the processes for you, leave generate as true.
 *
 * GUI Usage: use the start button to start the program. Once it is running, you are able to disable
 * continuous process generation, and have the program wait for more processes. You can turn off the OS
 * by pressing the OS on/off checkbox. This checkbox is misleading, and it does not turn the OS back on.
 * only off.
 *
 * The disable thread checkboxes should be used only if you are incredibly confident in your CPU.
 * Seriously, I wouldn't recomend going above 3. It will lag your computer and you will be unable to
 * move the mouse.
 *
 * If you want debug messages in your console, change the OSGlobals.debug value to true. If not (helps out the CPU),
 * then set it to false.
 */

import GUI.GWrapper;
import ProcessStuff.*;
import ProcessStuff.Process;
import memory.PageTable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main
{
    static PageTable pt = new PageTable();
    static boolean generate = true;
    public static void main(String[] args)
    {
        OSGlobals.debug = true;
        GWrapper g = new GWrapper(pt);
        Thread t1 = new Thread(g);
        t1.start();

        while(!OSGlobals.start)
        {
            try {
                Thread.sleep(100);
            } catch(InterruptedException e) {}
        }
        Integer num = 5;
        OSGlobals.startProcs = 5;
        SemManager semaphors = new SemManager();



        //Thread t1 = new Thread(pt);
        //t1.start();

        List<Process> processes = new ArrayList<>();
        List<Process> procs = new ArrayList<>();
        List<Process> procs3 = new ArrayList<>();
        List<Process> procs4 = new ArrayList<>();

        List<String> commands = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        String path;


        if(!generate)
        {
            //System.out.println("Enter the file path for the file you want to use: ");
            path = "t.txt";//scanner.nextLine();
            FileManipulator fileMan = new FileManipulator(path);
            commands = fileMan.getListOfCommandsWhitespaceDelimited();
        }


        ProcessManager procMan1 = new ProcessManager();
        ProcessManager procMan2 = new ProcessManager();
        ProcessManager procMan3 = new ProcessManager();
        ProcessManager procMan4 = new ProcessManager();


        ProcessGenerator pGen = new ProcessGenerator(pt, semaphors, procMan1);
        if(!generate)
        {
            Parser parse = new Parser(commands, pt, procMan1, semaphors, pGen);
            Process p = parse.initProgramFromFile();
            processes.add(p);
            procs.add(p);
            procs3.add(p);
            procs4.add(p);
        }
        if(generate)
            processes = pGen.generateRandomProcess(num);

        ProcessGenerator pGen2 = new ProcessGenerator(pt, semaphors, procMan2);
        if(generate)
            procs = pGen2.generateRandomProcess(num);

        ProcessGenerator pGen3 = new ProcessGenerator(pt, semaphors, procMan3);
        if(generate)
            procs3 = pGen3.generateRandomProcess(num);

        ProcessGenerator pGen4 = new ProcessGenerator(pt, semaphors, procMan4);
        if(generate)
            procs4 = pGen4.generateRandomProcess(num);

        Simulator sim = new Simulator(processes, pGen, procMan1);
        Simulator sim2 = new Simulator(procs, pGen2, procMan2);
        Simulator sim3 = new Simulator(procs3, pGen3, procMan3);
        Simulator sim4 = new Simulator(procs4, pGen4, procMan4);

        if(OSGlobals.debug) {
            for (Process command : processes) {
                System.out.println(command.toString() + "\n");
            }

            for (Process pr : procs)
                System.out.println(pr.toString());
        }
        //a note on the above: probably not, since procman is just what manages
        //the scheduler, and the sim is the whole thing.

        Thread t2 = new Thread(sim);
        t2.start();

        Thread t3 = new Thread(sim2);
        t3.start();

        Thread t4 = new Thread(sim3);
        t4.start();

        Thread t5 = new Thread(sim4);
        t5.start();
    }
}
