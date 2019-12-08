package GUI;

import javax.swing.*;

public class GWrapper implements Runnable
{
    JFrame frame;
    GUI g;

    public GWrapper()
    {
        frame = new JFrame("GUI");
        g = new GUI();
        frame.setContentPane(g.getPanel1());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void run()
    {
        while(true)
        {
            g.update();
            SwingUtilities.updateComponentTreeUI(frame);

            try {
                Thread.sleep(100);
            } catch(InterruptedException e){}
        }
    }
}
