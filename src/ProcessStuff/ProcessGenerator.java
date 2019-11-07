package ProcessStuff;

import commands.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProcessGenerator
{
    Double opCount;
    Double opType;
    Double critChance;
    int varSel;
    int priority;
    int opLegth;


    public ProcessGenerator() {}

    public List<Process> generateRandomProcess(Integer amount) {
        List<Process> procs = new ArrayList<>();

        List<Character> possibleVars = new ArrayList<>();  //possible values for the critical section var
        possibleVars.add('i');
        possibleVars.add('j');
        possibleVars.add('x');
        possibleVars.add('y');
        possibleVars.add('w');
        possibleVars.add('z');

        while(amount > 0)
        {
            List<Operation> opList = new ArrayList<>();

            Process proc;

            opCount = Math.random();
            opCount = (opCount * 5) + 3;

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
            proc = new Process(Process.States.NEW, "P"+amount.toString(), 300, 45, opList, priority);
            amount--;
            procs.add(proc);
        }
        return procs;
    }
}
