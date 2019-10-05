import commands.Operation;

import java.util.ArrayList;
import java.util.List;

public class ProcessManager
{
    List<Operation> readyQueue = new ArrayList<>();
    List<Operation> waitingQueue = new ArrayList<>();
    List<Operation> running = new ArrayList<>();

    public ProcessManager() {}

    public void initProcMan()
    {

    }
}
