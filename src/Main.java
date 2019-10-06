import ProcessStuff.Process;
import ProcessStuff.ProcessGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        /*Scanner scanner = new Scanner(System.in);
        System.out.println("Enter name of program file(in current dir): ");
        String file = scanner.next();

        FileManipulator fileMan = new FileManipulator(file);
        //List<String> listOfCommands = fileMan.getListOfCommandsWhitespaceDelimited();

        Parser parse = new Parser(fileMan.getListOfCommandsWhitespaceDelimited());
        Process prog = parse.initProgramFromFile();
        System.out.println(prog.toString());*/

        List<Process> processes = new ArrayList<>();
        ProcessGenerator pGen = new ProcessGenerator();

        processes = pGen.generateRandomProcess(3);

        System.out.println("------------------------------------");
        for(Process print : processes)
        {
            System.out.println(print);
        }
        System.out.println("------------------------------------");

        Simulator sim = new Simulator(processes);
        sim.simulate();


        /*Parser parse = new Parser(listOfCommands);

        for(Operation command: parse.getListOfCommands())
        {
            System.out.println(command.toString());
        }*/

    }
}
