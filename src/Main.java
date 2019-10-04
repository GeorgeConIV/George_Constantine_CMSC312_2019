import commands.Operation;

import java.util.List;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter name of program file(in current dir): ");
        String file = scanner.next();
        boolean keepLoopGoin = true;

        FileManipulator fileMan = new FileManipulator(file);
        List<String> listOfCommands = fileMan.getListOfCommandsWhitespaceDelimited();

        Parser parse = new Parser(listOfCommands);

        for(Operation command: parse.getListOfCommands())
        {
            System.out.println(command.toString());
        }

    }
}
