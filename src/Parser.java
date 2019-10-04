import commands.Calculate;
import commands.IOOp;
import commands.Operation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser
{
    List<String> preParsed;
    List<Operation> parsed = new ArrayList<>();   //Change this shit back to command, you're doing this for testing purposes

    String calcRegex = "((CALCULATE)[ ]([0-9]*))";//
    String ioRegex = "((I[/]O)[ ]([0-9]*))";
    Matcher matcher;
    Matcher ioMatcher;
    String preParseString = "";

    public Parser(List<String> preParesed)
    {
        this.preParsed = preParesed;
    }

    public List<Operation> getListOfCommands()
    {
        Pattern patOne = Pattern.compile(calcRegex);
        Pattern patTwo = Pattern.compile(ioRegex);
        boolean loopCheck = true;
        boolean ioCheck = false;
        boolean calcCheck = false;

        //TODO: add functionality for adding any type of Operation to this list
        for(String s: preParsed)
        {
            preParseString = preParseString.concat(s + "\n");
        }

        matcher = patOne.matcher(preParseString);
        ioMatcher = patTwo.matcher(preParseString);
        //TODO: learn how to do nested groups with mathers regex
        //TODO: setup nested groups such that there is a group for each possible operation, I guess
        while(loopCheck)
        {

            if(matcher.find())
            {
                parsed.add(new Calculate(Integer.parseInt(matcher.group(3))));
                System.out.println(matcher.group(3));
            }
            else
            {
                calcCheck = true;
            }
            if(ioMatcher.find())
            {
                parsed.add(new IOOp(Integer.parseInt(ioMatcher.group(3))));
                System.out.println(ioMatcher.group(3));
            }
            else
            {
                ioCheck = true;
            }

            if(ioCheck && calcCheck)
            {
                break;
            }
        }
        return parsed;
    }
}
