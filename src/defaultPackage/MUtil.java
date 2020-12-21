package defaultPackage;

import java.awt.geom.Point2D;

/**
 * The help class for translation location information
 * between draw board x-y axis and window axis.
 *
 * @author Yanlong LI, u5890571
 * */
public class MUtil {
    /**
     * Return the location based on x-y axis.
     * the cross point of X and Y is 0, 0,
     * unit mm. 1cm = 10 * 1 mm = 10 * 10 px
     *
     * The zoom has two stage:
     * 1. the grid zoom. the distance between each line in coord will be within 15 - 5.
     *    if > 15 or < 5 then will change the global zoom factor.
     * 2. the global zoom factor.
     *
     * @param centralX, the central point x-axis, related to screen size.
     * @param centralY, the central point y-axis, related to screen size.
     * @param offsetX, the x-axis offset value for the draw board pan.
     * @param offsetY, the y-axis offset value for the draw board pan.
     * @param zoomFactor, the draw board zoom factor, between 5 and 15.
     * @param gZoom, the global zoom factor.
     * @return the new point from screen coord (top left is 0,0) to draw board coord (central point of screen is 0,0).*/
    public static Point2D getPoint(Point2D pt, double centralX, double centralY, double offsetX, double offsetY, double zoomFactor,  double gZoom){
        return new Point2D.Double((pt.getX() - centralX - offsetX) / zoomFactor*gZoom,
                -(pt.getY() - centralY - offsetY) / zoomFactor*gZoom);
    }

    /**
     * Return the location based on the display.
     * Left top is 0, 0 unit px.
     * */
    public static Point2D getLoc(Point2D pt, double centralX, double centralY, double offsetX, double offsetY, double zoomFactor, double gZoom){
        return new Point2D.Double((pt.getX()/gZoom * zoomFactor + centralX + offsetX) ,
                -(pt.getY()/gZoom * zoomFactor - centralY - offsetY));
    }

    public static double getPointValue(double length, double zoomFactor, double gZoom){
        return length / zoomFactor*gZoom;
    }
}
