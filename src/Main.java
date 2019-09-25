import java.util.List;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter name of program file(in current dir): ");
        String file = scanner.next();
        FileManipulator fileMan = new FileManipulator(file);
        List<String> listOfCommands = fileMan.getListOfCommandsWhitespaceDelimited();

        for(String command: listOfCommands)
        {
            System.out.println(command);
        }
    }
}
