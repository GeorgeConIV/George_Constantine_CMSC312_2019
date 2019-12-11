package GUI;

import ProcessStuff.OSGlobals;
import memory.PageTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GWrapper implements Runnable
{
    JFrame frame;
    GUI g;
    PageTable pt;

    public GWrapper(PageTable pt)
    {
        this.pt = pt;
        frame = new JFrame("GUI");
        g = new GUI();
        g.onOff.setSelected(true);
        g.generateProcs.setSelected(true);
        g.dis3.setSelected(true);
        g.dis4.setSelected(true);
        frame.setContentPane(g.getPanel1());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        g.update(pt.getMemRemaining());
        g.start.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                OSGlobals.start = true;
            }
        });
    }

    @Override
    public void run()
    {
        while(OSGlobals.enable)
        {
            if(OSGlobals.start) {
                g.update(pt.getMemRemaining());
                //SwingUtilities.updateComponentTreeUI(frame);
            }
            OSGlobals.threadEn[0] = !g.dis1.isSelected();
            OSGlobals.threadEn[1] = !g.dis2.isSelected();
            OSGlobals.threadEn[2] = !g.dis3.isSelected();
            OSGlobals.threadEn[3] = !g.dis4.isSelected();

            OSGlobals.enable = g.onOff.isSelected();
            OSGlobals.genMore = g.generateProcs.isSelected();






            try {
                Thread.sleep(100);
            } catch(InterruptedException e){}
        }

    }
}
