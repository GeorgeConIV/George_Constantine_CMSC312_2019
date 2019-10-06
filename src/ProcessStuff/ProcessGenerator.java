package ProcessStuff;

import commands.*;

import java.util.ArrayList;
import java.util.List;

public class ProcessGenerator
{
    Double opCount;
    Double opType;
    int opLegth;


    public ProcessGenerator() {}

    public List<Process> generateRandomProcess(Integer amount) {
        List<Process> procs = new ArrayList<>();
        while(amount > 0)
        {
            List<Operation> opList = new ArrayList<>();

            Process proc;

            opCount = Math.random();
            opCount = (opCount * 5) + 3;

            while (opCount > 0) {
                opType = Math.random();
                opLegth = (int) ((Math.random() * 100) + 10);
                if (opType < 0.25) {
                    opList.add(new Calculate(opLegth));
                } else if (opType < 0.50) {
                    opList.add(new IOOp(opLegth));
                } else if (opType < 0.75) {
                    opList.add(new Out(opLegth));
                } else if (opType <= 1.0) {
                    opList.add(new Yield(opLegth));
                }
                opCount--;
            }
            proc = new Process(Process.States.NEW, "P"+amount.toString(), 300, 45, opList);
            amount--;
            procs.add(proc);
        }
        return procs;
    }
}
