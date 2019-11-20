package ProcessStuff;

import commands.*;
import memory.PageTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProcessGenerator
{
    Double opCount;
    Double opType;
    Double genChild;
    int varSel;
    int priority;
    int totalCount = 1;
    int opLegth;
    PageTable memoryMan;


    public ProcessGenerator(PageTable memoryMan)
    {
        this.memoryMan = memoryMan;
    }

    public List<Process> generateRandomProcess(Integer amount) {
        List<Process> procs = new ArrayList<>();
        Process proc;

        while(amount > 0)
        {

            genChild = Math.random();

            //Will randomly generate a process with a child process
            if(genChild > 0.6) {
                proc = generateProcess(amount, false);
                Process child = generateProcess(amount, true);
                proc.createChild(child);
                child.setParent(proc);
                procs.add(proc);
                procs.add(child);
            }
            else
            {
                proc = generateProcess(amount, false);
                procs.add(proc);
            }
            amount--;

        }
        return procs;
    }

    private Process generateProcess(int amount, boolean isChild)
    {
        opCount = Math.random();
        opCount = (opCount * 5) + 3;
        List<Operation> opList = new ArrayList<>();

        List<Character> possibleVars = new ArrayList<>();  //possible values for the critical section var
        possibleVars.add('i');//talk about multiple vars in final report
        possibleVars.add('j');
        possibleVars.add('x');
        possibleVars.add('y');
        possibleVars.add('w');
        possibleVars.add('z');

        while (opCount > 0) {
            opType = Math.random();
            opLegth = (int) ((Math.random() * 100) + 10);
            if (opType < 0.6) {
                opList.add(new Calculate(opLegth, false, Optional.empty()));
            } else if (opType < 0.7) {
                opList.add(new IOOp(opLegth));
            } else if (opType < 0.9) {
                opList.add(new Out());
            } else if (opType <= 1.0) {
                opList.add(new Yield());
            }
            opCount--;
        }
        varSel = (int) ((Math.random() * 5));
        priority = (int) (Math.random() * 10);
        //adding a critical section for each process, and a io operation for each process
        opList.add(new Calculate(50, true, Optional.of(possibleVars.get(varSel))));
        opList.add(new IOOp(10));
        String name;
        int memory = (int) (Math.random() * 160000000);
        if(isChild)
            name = "P" + (totalCount-1) + "'s child";
        else {
            name = "P" + totalCount;
            totalCount++;
        }
        return new Process(Process.States.NEW, name, 300, memory, opList, priority, memoryMan);
    }
}
