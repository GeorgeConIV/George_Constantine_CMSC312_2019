/**
 * George Constantine, iteration 2
 *
 * Usage: just run it the main, its pretty self explanitory
 */

import GUI.GWrapper;
import ProcessStuff.*;
import ProcessStuff.Process;
import memory.PageTable;

import javax.swing.*;
import java.util.List;
import java.util.Scanner;

public class Main
{
    static PageTable pt = new PageTable();
    public static void main(String[] args)
    {
        GWrapper g = new GWrapper();
        Thread t1 = new Thread(g);
        t1.start();
        //Scanner scanner = new Scanner(System.in);
        //System.out.println("Enter number of processes to be generated: ");
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

        List<Process> processes;
        List<Process> procs;
        List<Process> procs3;
        List<Process> procs4;


        ProcessManager procMan1 = new ProcessManager();
        ProcessManager procMan2 = new ProcessManager();
        ProcessManager procMan3 = new ProcessManager();
        ProcessManager procMan4 = new ProcessManager();

        ProcessGenerator pGen = new ProcessGenerator(pt, semaphors, procMan1);
        processes = pGen.generateRandomProcess(num);

        ProcessGenerator pGen2 = new ProcessGenerator(pt, semaphors, procMan2);
        procs = pGen2.generateRandomProcess(num);

        ProcessGenerator pGen3 = new ProcessGenerator(pt, semaphors, procMan3);
        procs3 = pGen3.generateRandomProcess(num);

        ProcessGenerator pGen4 = new ProcessGenerator(pt, semaphors, procMan4);
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
