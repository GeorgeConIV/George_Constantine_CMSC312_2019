package GUI;

import ProcessStuff.OSGlobals;

import javax.swing.*;

public class GUI{
    private JPanel panel1;
    private JLabel cycleCount;
    private JLabel procsCompleted;
    private JLabel completed;
    private JLabel cyclesper;
    private JCheckBox dis1;
    private JCheckBox dis2;
    private JCheckBox dis3;
    private JCheckBox dis4;
    private JList list1;

    JFrame frame;
    public GUI()
    {
    }


    public void update()
    {
        this.cycleCount.setText("Cycle count: " + OSGlobals.cycleCount);
        this.completed.setText("# of processes completed: " + OSGlobals.procsCompleted);
        try {
            this.cyclesper.setText("Average # of cycles per process: " + (int) Math.floor(OSGlobals.cycleCount / OSGlobals.procsCompleted));
        } catch(ArithmeticException e)
        {}

    }

    public JLabel getCycleCoun()
    {
        return cycleCount;
    }


    public JPanel getPanel1()
    {
        return panel1;
    }

}
