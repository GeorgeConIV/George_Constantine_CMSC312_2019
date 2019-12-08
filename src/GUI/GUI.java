package GUI;

import ProcessStuff.OSGlobals;

import javax.swing.*;

public class GUI{
    private JPanel panel1;
    private JLabel cycleCount;
    private JLabel procsCompleted;
    private JLabel completed;
    private JLabel cyclesper;
    public JCheckBox dis1;
    public JCheckBox dis2;
    public JCheckBox dis3;
    public JCheckBox dis4;
    public JCheckBox generateProcs;
    public JCheckBox onOff;
    public JTextField numProcs;
    public JButton start;
    private JLabel warn;
    private JLabel warn2;

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

    private void createUIComponents() {
    }
}
