package defaultPackage;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
/**
 * The Draw class extend from Eric's code
 *
 * Replace ArrayList to HashSet.
 *
 * @author Yanlong LI, u5890571
 * */
public class Draw extends ArrayList<Elements> {
    private static final int CONTROLPOINTRADIUS = 5;

    ElementsFactory drawFactory;

    public Draw(){}

    public Draw(ElementsFactory drawFactory) {
        this.drawFactory = drawFactory;
    }

    public void draw(Graphics2D g) {
        for (Elements e : this) e.draw(g);
    }

    public ElementControl findControl(Point point) {
        for (Elements e : this) {
            ArrayList<Point2D>  cps = e.controlPoints();
            for (int i = 0;i<cps.size();i++) {  // return the first control point within the limited radius
                if (cps.get(i).distance(point) < CONTROLPOINTRADIUS) return new ElementControl(e,i);
            }
        }
        return null;
    }

    public Elements findElement(Point point){
        for (Elements e : this) {
            ArrayList<Point2D>  cps = e.controlPoints();
            for (int i = 0;i<cps.size();i++) {  // return the first control point within the limited radius
                if (cps.get(i).distance(point) < CONTROLPOINTRADIUS) return e;
            }
        }
        return null;
    }
}
