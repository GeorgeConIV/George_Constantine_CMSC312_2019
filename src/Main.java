/**
 * George Constantine, iteration 2
 *
 * Usage: just run it the main, its pretty self explanitory
 */

import ProcessStuff.Process;
import ProcessStuff.ProcessGenerator;
import memory.PageTable;

import java.util.List;
import java.util.Scanner;

public class Main
{
    static PageTable pt = new PageTable();
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of processes to be generated: ");
        Integer num = Integer.parseInt(scanner.next());

        //Thread t1 = new Thread(pt);
        //t1.start();

        List<Process> processes;
        ProcessGenerator pGen = new ProcessGenerator(pt);

        processes = pGen.generateRandomProcess(num);
        for(Process command: processes)
        {
            System.out.println(command.toString()+ "\n");
        }

        //TODO: test threading and semaphor compatibility
        //TODO: possibily change the threadable thing from sim to procman
        //a note on the above: probably not, since procman is just what manages
        //the scheduler, and the sim is the whole thing.
        Simulator sim = new Simulator(processes, pGen);
        Thread t2 = new Thread(sim);
        t2.start();

        Simulator sim2 = new Simulator(pGen.generateRandomProcess(num), pGen);
        Thread t3 = new Thread(sim2);
        t3.start();

        /*Simulator sim3 = new Simulator(pGen.generateRandomProcess(num), pGen);
        Thread t4 = new Thread(sim3);
        t4.start();

        Simulator sim4 = new Simulator(pGen.generateRandomProcess(num), pGen);
        Thread t5 = new Thread(sim4);
        t5.start();*/

    }
}
