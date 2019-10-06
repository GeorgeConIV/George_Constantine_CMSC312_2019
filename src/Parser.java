import ProcessStuff.Process;
import commands.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser
{
    List<String> preParsed;
    List<Operation> parsed = new ArrayList<>();
    String preParseString = "";
    String preParseOpString = "";
    Integer stringLoc = 0;
    char charBuff = ' ';

    String nameRegex = "(Name: )([\\w ]*)";
    String runtimeRegex = "(Total runtime: )([0-9]*)";
    String memRegex ="(Memory: )([0-9]*)";

    Matcher nameMatcher;
    Matcher runtimeMatcher;
    Matcher memMatcher;

    Pattern patName = Pattern.compile(nameRegex);
    Pattern patRuntime = Pattern.compile(runtimeRegex);
    Pattern patMem = Pattern.compile(memRegex);



    public Parser(List<String> preParesed)
    {
        this.preParsed = preParesed;
        for(String s: preParesed)
        {
            preParseString = preParseString.concat(s + "\n");
        }

        List<String> test = this.preParsed;
        test.remove(0);
        test.remove(0);
        test.remove(0);
        for(String s: test)
        {
            preParseOpString = preParseOpString.concat(s + "\n");
        }
    }

    public Process initProgramFromFile()
    {
        Process prog;
        String name;
        Integer runtime;
        Integer memory;
        List<Operation> listOfOps;

        nameMatcher = patName.matcher(preParseString);
        nameMatcher.find();
        name = nameMatcher.group(2);

        runtimeMatcher = patRuntime.matcher(preParseString);
        runtimeMatcher.find();
        runtime = Integer.parseInt(runtimeMatcher.group(2));

        memMatcher = patMem.matcher(preParseString);
        memMatcher.find();
        memory = Integer.parseInt(memMatcher.group(2));

        listOfOps = getListOfCommands();

        prog = new Process(Process.States.NEW, name, runtime, memory, listOfOps);
        return prog;
    }

    public char getChar()
    {
        if(preParseOpString.length() == stringLoc)
        {
            return '\0';
        }

        char c = preParseOpString.charAt(stringLoc);
        stringLoc++;
        return c;
    }

    public Operation getOp()
    {
        Operation op = new Calculate(1);
        Integer cycles = 0;
        String cycleString = "";
        String opString = "";
        while(Character.isWhitespace(charBuff))
        {
            charBuff = getChar();
        }
        if(Character.isLetter(charBuff))
        {
            do
            {
                opString += charBuff;
                charBuff = getChar();
            } while(Character.isLetter(charBuff) || charBuff == '/');

            switch(opString)
            {
                case "CALCULATE":
                    do
                    {
                        charBuff = getChar();
                        cycleString += charBuff;
                    } while(Character.isDigit(charBuff));

                    cycleString = cycleString.trim();
                    cycles = Integer.parseInt(cycleString);
                    op = new Calculate(cycles);
                    break;
                case  "I/O":
                    do
                    {
                        charBuff = getChar();
                        cycleString += charBuff;
                    } while(Character.isDigit(charBuff));

                    cycleString = cycleString.trim();
                    cycles = Integer.parseInt(cycleString);
                    op = new IOOp(cycles);
                    break;
                case "YIELD":
                    do
                    {
                        charBuff = getChar();
                        cycleString += charBuff;
                    } while(Character.isDigit(charBuff));

                    cycleString = cycleString.trim();
                    cycles = Integer.parseInt(cycleString);
                    op = new Yield(cycles);
                    break;
                case "OUT":
                    do
                    {
                        charBuff = getChar();
                        cycleString += charBuff;
                    } while(Character.isDigit(charBuff));

                    cycleString = cycleString.trim();
                    cycles = Integer.parseInt(cycleString);
                    op = new Out(cycles);
                    break;
                case "EXE": op = new Exe(); break;
            }
        }

        return op;
    }

    public List<Operation> getListOfCommands()
    {
        Operation op;

        do
        {
            op = getOp();
            parsed.add(op);
        } while(!(op instanceof Exe));

        return parsed;
    }
}
