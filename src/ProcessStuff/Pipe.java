package ProcessStuff;

public class Pipe
{
    Process sender;
    Process reciever;

    String dat;
    Boolean flag = false;

    public Pipe(Process sender, Process reciever)
    {
        this.reciever = reciever;
        this.sender = sender;
    }

    public Pipe()
    {

    }

    public void send(String data)
    {
        dat = data;
        flag = true;
        System.out.println("[PIPE] Sending data to child: \"" + data + "\"");
    }

    public String recieve()
    {
        flag = false;
        System.out.println("[PIPE] Recieving data from parent: \"" + dat + "\"");
        return dat;
    }

    public Boolean isData()
    {
        return flag;
    }

    public Boolean isReciever(Process proc)
    {
        return proc.equals(reciever);
    }
}
