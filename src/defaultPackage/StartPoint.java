package defaultPackage;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * The empty element used for click creation.
 *
 * @author Yanlong LI, u5890571*/
public class StartPoint extends Elements {
    @Override
    public void draw(Graphics2D g) {

    }

    @Override
    public ArrayList<Point2D> controlPoints() {
        return null;
    }

    @Override
    public void moveControlPoint(int control, Point2D pos) {

    }

    @Override
    public void setOffset(Point2D pos) {

    }

    @Override
    public void setZoom(double zoom, double centralX, double centralY, double offsetX, double offsetY, double zoomFactor, double gZoom) {

    }

    @Override
    public void setZoomFactor(double zoomFactor, double zoomFactorGlobal) {

    }

    @Override
    public boolean isFinish() {
        return true;
    }

    @Override
    public void addPoint(Point2D pos) {

    }

    @Override
    public void drawUpdate(Point2D pos) {

    }

    @Override
    public void setGroup(int group) {

    }

    @Override
    public int getGroup() {
        return 0;
    }

    @Override
    public void setLabel(String describes) {

    }

    @Override
    public void setShowLabel(boolean show) {

    }

    @Override
    public void setShowMeasure(boolean show) {

    }

    @Override
    public void setColor(Color color) {

    }

    @Override
    public void showControl(boolean show) {

    }

    @Override
    public void updateMeasurements(double centralX, double centralY, double offsetX, double offsetY, double zoomFactor, double gZoom) {

    }

    @Override
    public void setUnit(String unit) {

    }

    @Override
    public String save(double zoom, double centralX, double centralY, double offsetX, double offsetY, double zoomFactor, double gZoom) {
        return "";
    }

    @Override
    public void load(double zoom, double centralX, double centralY, double offsetX, double offsetY, double zoomFactor, double gZoom, String describe, Color fillColor, Point2D[] points, int group) {

    }

    @Override
    public double[] getMeasurements() {
        return new double[0];
    }
}
