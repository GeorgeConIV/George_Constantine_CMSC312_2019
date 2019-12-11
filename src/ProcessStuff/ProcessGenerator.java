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
    static int totalCount = 1;
    int opLegth;
    PageTable memoryMan;
    SemManager sems;
    ProcessManager procMan;
    public Mailbox mailI = new Mailbox('m');
    public Mailbox mailJ = new Mailbox('b');


    public ProcessGenerator(PageTable memoryMan, SemManager sems, ProcessManager procMan)
    {
        this.sems = sems;
        this.procMan = procMan;
        this.memoryMan = memoryMan;
    }

    public List<Process> generateRandomProcess(Integer amount) {
        List<Process> procs = new ArrayList<>();
        Process proc;

        while(amount > 0)
        {

            genChild = Math.random();
            //Will randomly generate a process with a child process
            proc = generateProcess(genChild);
            //adds all children and grand children of a process
            do
            {
                procs.add(proc);
                if(proc.hasChild())
                    proc = proc.getChild();
            }while(proc.hasChild());

            amount--;

        }
        return procs;
    }

    /**
     * Generates a process with random parameters and operations.
     * @param hasChild used in the recursive generation of multi level parent-child relationships
     * @return the process that was created
     */
    private Process generateProcess(double hasChild)
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
        opList.add(new Calculate(50, true, Optional.of(possibleVars.get(1))));
        opList.add(new IOOp(10));
        String name;
        int memory = (int) (Math.random() * 160000);
        double mail = Math.random();
        Mailbox mailb;
        if(mail>0.5)
            mailb = mailI;
        else
            mailb = mailJ;
        Process proc;
        if(hasChild > 0.5)
        {
            name = "P" + totalCount;
            totalCount++;
            proc = new Process(Process.States.NEW, name, 300, memory, opList, priority, memoryMan, procMan, sems, mailb);
            genChild = Math.random();
            Process child = generateProcess(genChild);
            child.setParent(proc);
            proc.createChild(child);
        }
        else
        {
            name = "P" + totalCount;
            totalCount++;
            proc = new Process(Process.States.NEW, name, 300, memory, opList, priority, memoryMan, procMan, sems, mailb);

        }

        return proc;
    }
}
