/**
 * George Constantine, iteration 1
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
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of processes to be generated: ");
        Integer num = Integer.parseInt(scanner.next());

        PageTable pt = new PageTable();

        Thread t1 = new Thread(pt);
        t1.start();

        List<Process> processes;
        ProcessGenerator pGen = new ProcessGenerator(pt);

        processes = pGen.generateRandomProcess(num);
        for(Process command: processes)
        {
            System.out.println(command.toString()+ "\n");
        }


        Simulator sim = new Simulator(processes);
        Thread t2 = new Thread(sim);
        t2.start();

    }
}
