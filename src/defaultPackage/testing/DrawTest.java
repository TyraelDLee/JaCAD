package defaultPackage.testing;

import defaultPackage.*;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Yanlong LI, u5890571
 */

public class DrawTest implements Runnable {
    MainWindow gui;

    private LineElement line = new LineElement(new Point2D.Double(200,101),new Point2D.Double(100,110), Color.BLACK);
    @Test
    public void drawlinetest() {
        gui = new MainWindow();
        gui.init();
        try {
            SwingUtilities.invokeAndWait(this);

            Draw expected = new Draw(new LowLevelElements());
            expected.add(line);
            line.addPoint(new Point2D.Double(200,101));

            assertEquals("Expect total number of elements:" +expected.size()+" but got "+gui.draw.size(), expected.size(),gui.draw.size());
            if(gui.draw.size() == expected.size()){
                for (int i = 0; i < expected.size(); i++) {
                    assertEquals(gui.draw.get(i).save(15/0.2,0,0,0,0,15,0.2), expected.get(i).save(15/0.2,0,0,0,0,15,0.2));
                }
            }else
                fail();

        }
        catch (InvocationTargetException e) {
            System.out.println(e.getMessage());
            fail();
        }catch (InterruptedException e){
            fail();
        }
    }

    @Override
    public void run() {
        gui.background.update("LINE");
        gui.background.mouseClicked(new MouseEvent(gui.background,0,0L,0,100,110,1,false, 1));
        gui.background.mouseClicked(new MouseEvent(gui.background,0,20L,0,200,101,1,false, 1));
    }
}
