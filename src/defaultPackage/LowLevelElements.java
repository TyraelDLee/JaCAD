package defaultPackage;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * The low level elements, draw board will create elements
 * from here.
 * Extend from Eric's code.
 *
 * @author Yanlong LI, u5890571
 * */
public class LowLevelElements implements ElementsFactory {
    private static final String LINE = "LINE";
    private static final String SQUARE = "SQUARE";
    private static final String RECTANGLE = "RECTANGLE";
    private static final String TRIANGLE = "TRIANGLE";
    private static final String CIRCLE = "CIRCLE";
    private static final String ARC = "ARC";
    private static final String POLY = "POLY";

    public Elements createElementFromMouseClick(String cmd, Color color, Point pos) {
        Elements elementsForClick;
        if(cmd.equals(LINE))
            elementsForClick = new LineElement(pos,pos, color);
        else if(cmd.equals(SQUARE))
            elementsForClick = new RectangleElement(pos, true, color);
        else if(cmd.equals(RECTANGLE))
            elementsForClick = new RectangleElement(pos, false, color);
        else if(cmd.equals(TRIANGLE))
            elementsForClick = new TriangleElement(pos, color);
        else if(cmd.equals(CIRCLE))
            elementsForClick = new CircleElement(pos, color);
        else if(cmd.equals(ARC))
            elementsForClick = new ArcElement(pos, color);
        else if(cmd.equals(POLY))
            elementsForClick = new PolyElement(pos, color);
        else
            elementsForClick = null;
        return elementsForClick;
    }

    @Override
    public Elements createElementFromSave(String object, double zoom, double centralX, double centralY, double offsetX, double offsetY, double zoomFactor, double gZoom ) {
        String cmd = object.split("\t")[0];
        String describe = object.split("\t")[1];
        String[] color = object.split("\t")[2].split(" ");
        Color fillColor = new Color(Integer.parseInt(color[0]), Integer.parseInt(color[1]),Integer.parseInt(color[2]));
        int group = Integer.parseInt(object.split("\t")[3]);
        String[] points = object.split("\t")[4].split(" ");
        Point2D[] points2D = new Point2D[points.length];
        for (int i = 0; i < points.length; i++) {
            double x = Double.parseDouble(points[i].split(",")[0]);
            double y = Double.parseDouble(points[i].split(",")[1]);
            points2D[i] = new Point2D.Double(x,y);
        }
        Elements elementsForClick;
        if(cmd.equals(LINE))
            elementsForClick = new LineElement();
        else if(cmd.equals(SQUARE))
            elementsForClick = new RectangleElement();
        else if(cmd.equals(RECTANGLE))
            elementsForClick = new RectangleElement();
        else if(cmd.equals(TRIANGLE))
            elementsForClick = new TriangleElement();
        else if(cmd.equals(CIRCLE))
            elementsForClick = new CircleElement();
        else if(cmd.equals(ARC))
            elementsForClick = new ArcElement();
        else if(cmd.equals(POLY))
            elementsForClick = new PolyElement();
        else elementsForClick = null;
        elementsForClick.load(zoom,centralX,centralY,offsetX,offsetY,zoomFactor,gZoom,describe,fillColor,points2D, group);
        return elementsForClick;
    }
}
