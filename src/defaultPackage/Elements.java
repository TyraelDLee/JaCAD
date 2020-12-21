package defaultPackage;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Element - all elements that make up the drawing will extend this abstract class.
 * Extend from Eric's code
 *
 * @author Yanlong LI, u5890571
 */
public abstract class Elements {
    abstract public void draw(Graphics2D g);
    abstract public ArrayList<Point2D> controlPoints();
    abstract public void moveControlPoint(int control, Point2D pos);

    /**
     * Set the offset, called by draw board when user pan the draw board.
     *
     * @param pos, a point for new central point.
     * */
    abstract public void setOffset(Point2D pos);

    /**
     * Set the zoom, called by draw board when user zoom the draw board.
     *
     * update the element based on the draw board x-y axis.
     * */
    abstract public void setZoom(double zoom, double centralX, double centralY, double offsetX, double offsetY, double zoomFactor, double gZoom);

    /**
     * Update the zoom factor for zoom feature.
     * */
    abstract public void setZoomFactor(double zoomFactor, double zoomFactorGlobal);

    /**
     * Notify the draw board this element is defined finish.
     * */
    abstract public boolean isFinish();

    /**
     * Add points for each user click
     * */
    abstract public void addPoint(Point2D pos);

    /**
     * Update the new point for user choose
     * */
    abstract public void drawUpdate(Point2D pos);

    /**
     * Set the group id to that element for merge
     * */
    abstract public void setGroup(int group);

    /**
     * return the group id for merge
     * */
    abstract public int getGroup();
    /**
     * Set the label content for user
     * */
    abstract public void setLabel(String describes);

    /**
     * Determine show the label or not
     * */
    abstract public void setShowLabel(boolean show);

    /**
     * Determine show the measure or not
     * */
    abstract public void setShowMeasure(boolean show);

    /**
     * Set the element color
     * */
    abstract public void setColor(Color color);

    /**
     * Determine show the control or not
     * */
    abstract public void showControl(boolean show);

    /**
     * update the measurements
     * */
    abstract public void updateMeasurements(double centralX, double centralY, double offsetX, double offsetY, double zoomFactor, double gZoom);

    /**
     * Set the unit for measurements
     * */
    abstract public void setUnit(String unit);

    /**
     * Get the elements information as String
     * */
    abstract public String save(double zoom, double centralX, double centralY, double offsetX, double offsetY, double zoomFactor, double gZoom);

    /**
     * Get the elements information from string
     * */
    abstract public void load(double zoom, double centralX, double centralY, double offsetX, double offsetY, double zoomFactor, double gZoom, String describe, Color fillColor, Point2D[] points, int group);

    abstract public double[] getMeasurements();
}
