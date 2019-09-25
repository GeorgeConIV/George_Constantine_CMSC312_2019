import commands.Command;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser
{
    List<String> preParsed;
    List<Command> parsed;

    String calcRegex = "(CALCULATE)([0-9]*)";
    String ioRegex = "(I\\\\O)([0-9]*)";
    Pattern patOne = Pattern.compile(calcRegex);
    Pattern patTwo = Pattern.compile(ioRegex);
    Matcher matcher;

    public Parser(List<String> preParesed)
    {
        this.preParsed = preParesed;
    }

    public List<Command> getListOfCommands()
    {
        matcher = patOne.matcher(calcRegex);
        matcher.find();

        return parsed;
    }

}
