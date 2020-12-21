package defaultPackage;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * The line class, define the draw arcs, label and measure.
 * line will be created by define 2 points. Each line has 2
 * top points and 1 control points.
 *
 * @author Yanlong LI, u5890571
 * */
public class LineElement extends Elements{
    private Point2D[] points = new Point2D[2];
    private Point2D[] initPoints = new Point2D[2];
    private double zoomFactor = 15/0.2;
    private Point2D offset = new Point2D.Double(0,0);
    private int currentPoints = 0;
    private double length = 0;
    private Color fillColor = Color.BLACK;
    private String describe = "Line";
    private boolean showMeasure = false;
    private boolean showLabel = false;
    private boolean showControl = false;
    private String unit = "mm";
    private double unitScale = 1;
    private int group = -1;
    private boolean fromLoad = false;

    public LineElement(){}
    public LineElement(Point2D start, Point2D end, Color fillColor){
        this.points[0] = start;
        this.points[1] = end;
        this.initPoints = new Point2D[]{start,end};
        this.fillColor = fillColor;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(this.fillColor);
        g.draw(new Line2D.Double(points[0],points[1]));
        if(this.showMeasure)
            drawMeasure(g);
        if(this.showControl)
            drawControl(g);
        if(this.showLabel)
            drawLabel(g);
    }

    private void drawLabel(Graphics2D g) {
        Font font = g.getFont();
        FontMetrics fm = g.getFontMetrics(font);
        int textWidth = fm.stringWidth(this.describe)/2;
        Point2D edge = (points[0].getY()<points[1].getY())?new Point2D.Double(PUtil.mid(points[0],points[1]).getX(), points[1].getY()):new Point2D.Double(PUtil.mid(points[0],points[1]).getX(), points[0].getY());
        g.draw(new Line2D.Double(new Point2D.Double(PUtil.mid(points[0],points[1]).getX(), PUtil.mid(points[0],points[1]).getY()+5), edge));
        g.drawString(this.describe,(int)edge.getX()-textWidth, (int)edge.getY()+10);
    }

    private void drawMeasure(Graphics2D g){
        Font font = g.getFont();
        FontMetrics fm = g.getFontMetrics(font);
        Point2D mid = PUtil.mid(this.points[0],this.points[1]);
        int lengthLocation = fm.stringWidth(this.length+this.unit)/2;
        g.drawString(this.length+this.unit, (int)mid.getX()-lengthLocation, (int)mid.getY()-15);
    }

    private void drawControl(Graphics2D g){
        g.setColor(Color.BLACK);
        ArrayList<Point2D> pt = this.controlPoints();
        for (Point2D p : pt) {
            g.draw(new Ellipse2D.Double(p.getX() - 2, p.getY() - 2, 4, 4));
        }
    }

    @Override
    public ArrayList<Point2D> controlPoints() {
        ArrayList<Point2D> controlpoints = new ArrayList<Point2D>();
        controlpoints.add(PUtil.mid(points[0], points[1]));//This point control the translate
        controlpoints.add(points[0]);
        controlpoints.add(points[1]);
        return controlpoints;
    }

    @Override
    public void moveControlPoint(int control, Point2D pos) {
        if (control == 1) // points[0]
            points[0] = pos;
        else if (control == 2) // points[1]
            points[1] = pos;
        else if (control == 0) { // mid
            Point2D vec = PUtil.sub(pos, PUtil.mid(points[0], points[1]));
            points[0] = PUtil.add(points[0], vec);
            points[1] = PUtil.add(points[1], vec);
        }
        this.initPoints[0] = this.points[0];
        this.initPoints[1] = this.points[1];
    }

    @Override
    public void setOffset(Point2D pos) {
        this.offset = PUtil.add(pos, this.offset);
        this.points[0] = PUtil.add(pos,this.points[0]);
        this.points[1] = PUtil.add(pos,this.points[1]);
        this.initPoints[0] = this.points[0];
        this.initPoints[1] = this.points[1];
    }

    @Override
    public void setZoom(double zoom, double centralX, double centralY, double offsetX, double offsetY, double zoomFactor, double gZoom) {
        for (int i = 0; i < 2; i++) {
            Point2D newI = MUtil.getPoint(this.initPoints[i],centralX,centralY, offsetX, offsetY, zoomFactor, gZoom);
            this.points[i] = MUtil.getLoc(PUtil.scale(newI, zoom/this.zoomFactor), centralX,centralY, offsetX, offsetY, zoomFactor, gZoom);
        }
    }

    @Override
    public void setZoomFactor(double zoomFactor, double zoomFactorGlobal) {
        this.zoomFactor = zoomFactor/zoomFactorGlobal;
    }

    @Override
    public boolean isFinish() {
        return this.fromLoad || this.currentPoints==1;
    }

    @Override
    public void addPoint(Point2D pos) {
        this.points[this.currentPoints] = pos;
        this.currentPoints++;
    }

    @Override
    public void drawUpdate(Point2D pos) {
        this.points[this.currentPoints] = pos;
        this.initPoints[0] = this.points[0];
        this.initPoints[1] = this.points[1];
    }

    @Override
    public void setGroup(int group) {
        this.group = group;
    }

    @Override
    public int getGroup() {
        return this.group;
    }

    @Override
    public void setLabel(String describes) {
        this.describe = describes;
    }

    @Override
    public void setShowLabel(boolean show) {
        this.showLabel = show;
    }

    @Override
    public void setShowMeasure(boolean show) {
        this.showMeasure = show;
    }

    @Override
    public void setColor(Color color) {
        this.fillColor = color;
    }

    @Override
    public void showControl(boolean show) {
        this.showControl = show;
    }

    @Override
    public void updateMeasurements(double centralX, double centralY, double offsetX, double offsetY, double zoomFactor, double gZoom) {
        double dx = MUtil.getPoint(this.points[0],centralX,centralY,offsetX,offsetY,zoomFactor,gZoom).getX() - MUtil.getPoint(this.points[1],centralX,centralY,offsetX,offsetY,zoomFactor,gZoom).getX();
        double dy = MUtil.getPoint(this.points[0],centralX,centralY,offsetX,offsetY,zoomFactor,gZoom).getY() - MUtil.getPoint(this.points[1],centralX,centralY,offsetX,offsetY,zoomFactor,gZoom).getY();
        this.length = (int)(Math.sqrt(dx * dx + dy * dy) * 1000 / this.unitScale) / 1000.0;
    }

    @Override
    public void setUnit(String unit) {
        this.unit = unit;
        if(this.unit.equals("mm"))
            this.unitScale = 1;
        if(this.unit.equals("cm"))
            this.unitScale = 10;
        if(this.unit.equals("dm"))
            this.unitScale = 100;
        if(this.unit.equals("m"))
            this.unitScale = 1000;
    }

    @Override
    public String save(double zoom, double centralX, double centralY, double offsetX, double offsetY, double zoomFactor, double gZoom) {
        String object = "LINE\t";
        object+=this.describe+"\t";
        object+=this.fillColor.getRed()+" "+this.fillColor.getGreen()+" "+this.fillColor.getBlue()+" "+"\t";
        object+=this.group+"\t";
        for(Point2D pts : points){
            object+=MUtil.getPoint(pts,centralX,centralY, offsetX, offsetY, zoomFactor, gZoom).getX()+","+
                    MUtil.getPoint(pts,centralX,centralY, offsetX, offsetY, zoomFactor, gZoom).getY()+" ";
        }
        return object;
    }

    @Override
    public void load(double zoom, double centralX, double centralY, double offsetX, double offsetY, double zoomFactor, double gZoom, String describe, Color fillColor, Point2D[] points, int group) {
        this.describe = describe;
        this.fillColor = fillColor;
        this.group = group;
        for (int i = 0; i < points.length; i++) {
            points[i] = MUtil.getLoc(PUtil.scale(points[i], zoom/this.zoomFactor), centralX,centralY, offsetX, offsetY, zoomFactor, gZoom);
        }
        this.points = points;
        this.initPoints[0] = this.points[0];
        this.initPoints[1] = this.points[1];
        this.fromLoad = true;
    }

    @Override
    public double[] getMeasurements() {
        return new double[]{this.length};
    }
}
