package defaultPackage;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * The circle class, define the draw circle, label and measure.
 * Circle will be created by define 2 points. Each circle has 1
 * central points and 4 control points.
 *
 * @author Yanlong LI, u5890571
 * */
public class CircleElement extends Elements {
    private Point2D[] points = new Point2D[2];
    private Point2D[] initPoints = new Point2D[2];
    private Point2D offset = new Point2D.Double(0,0);
    private double zoomFactor = 15/0.2;
    private int currentPoints = 0;
    private double radius;
    private double meauRadius = 0;
    private double area = 0;
    private Color fillColor = Color.BLACK;
    private String describe = "Ellipse";
    private boolean showLabel = false;
    private boolean showMeasure = false;
    private boolean showControl = false;
    private String unit = "mm";
    private double unitScale = 1;
    private int group = -1;
    private boolean fromLoad = false;

    public CircleElement(){}
    public CircleElement(Point2D start, Color fillColor){
        this.points = new Point2D[]{start,start};
        this.initPoints = new Point2D[]{start, start};
        this.fillColor = fillColor;
    }
    @Override
    public void draw(Graphics2D g) {
        g.setColor(this.fillColor);
        g.draw(new Ellipse2D.Double(points[0].getX()-this.radius,points[0].getY()-this.radius, this.radius*2,this.radius*2));
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
        Point2D edge = new Point2D.Double(points[0].getX(),points[0].getY()+radius+5);
        g.draw(new Line2D.Double(edge, new Point2D.Double(edge.getX(), edge.getY()+20)));
        g.drawString(this.describe, (int)edge.getX() - textWidth, (int)edge.getY()+30);
    }

    private void drawMeasure(Graphics2D g){
        Font font = g.getFont();
        FontMetrics fm = g.getFontMetrics(font);
        int radiusLocation = fm.stringWidth(this.meauRadius+this.unit)/2;
        int areaLocation = fm.stringWidth(this.area+this.unit+"^2")/2;
        g.draw(new Line2D.Double(points[0], new Point2D.Double(points[0].getX()+this.radius,points[0].getY())));
        g.drawString(this.meauRadius+this.unit, (int)PUtil.mid(points[0], new Point2D.Double(points[0].getX()+this.radius,points[0].getY())).getX()-radiusLocation, (int)points[0].getY());
        g.drawString(this.area+this.unit+"^2", (int)points[0].getX()-areaLocation, (int)points[0].getY()-15);
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
        ArrayList<Point2D> controls = new ArrayList<>();
        controls.add(points[0]);//This point control the translate
        controls.add(new Point2D.Double(points[0].getX(), points[0].getY() - this.radius));
        controls.add(new Point2D.Double(points[0].getX(), points[0].getY() + this.radius));
        controls.add(new Point2D.Double(points[0].getX() - this.radius, points[0].getY()));
        controls.add(new Point2D.Double(points[0].getX() + this.radius, points[0].getY()));
        return controls;
    }

    @Override
    public void moveControlPoint(int control, Point2D pos) {
        if(control > 0) {
            this.points[1] = pos;
            this.initPoints[1] = pos;
        }
        else if (control == 0){
            Point2D vec = PUtil.sub(pos, this.points[0]);
            this.points[0] = PUtil.add(this.points[0], vec);
            this.points[1] = PUtil.add(this.points[1], vec);
        }
        this.initPoints[0] = this.points[0];
        this.initPoints[1] = this.points[1];
        this.radius = radius(this.initPoints[0],this.initPoints[1]);
    }

    @Override
    public void setOffset(Point2D pos) {
        this.offset = PUtil.add(pos, this.offset);
        this.points[0] = PUtil.add(pos,this.points[0]);
        this.points[1] = PUtil.add(pos,this.points[1]);
        this.initPoints[0] = this.points[0];
        this.initPoints[1] = this.points[1];
        this.radius = radius(this.initPoints[0],this.initPoints[1]);
    }

    @Override
    public void setZoom(double zoom, double centralX, double centralY, double offsetX, double offsetY, double zoomFactor, double gZoom) {
        for (int i = 0; i < 2; i++) {
            Point2D newI = MUtil.getPoint(this.initPoints[i],centralX,centralY, offsetX, offsetY, zoomFactor, gZoom);
            this.points[i] = MUtil.getLoc(PUtil.scale(newI, zoom/this.zoomFactor), centralX,centralY, offsetX, offsetY, zoomFactor, gZoom);
        }
        this.radius = radius(this.points[0],this.points[1]);
    }

    @Override
    public void setZoomFactor(double zoomFactor, double zoomFactorGlobal) {
        this.zoomFactor = zoomFactor/zoomFactorGlobal;
    }

    @Override
    public boolean isFinish() {
        return this.fromLoad || this.currentPoints == 1;
    }

    @Override
    public void addPoint(Point2D pos) {
        this.points[1] = pos;
        this.currentPoints++;
    }

    @Override
    public void drawUpdate(Point2D pos) {
        this.radius = radius(points[0],pos);
        this.points[1] = pos;
        this.initPoints[0] = this.points[0];
        this.initPoints[points.length-1] = this.points[points.length-1];
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
        this.meauRadius = (int)(MUtil.getPointValue(this.radius,zoomFactor,gZoom) * 1000 / this.unitScale) / 1000.0;
        this.area = (int)(Math.PI * this.meauRadius * this.meauRadius * 1000) / 1000.0;
    }

    private double radius(Point2D start, Point2D end){
        double edge1 = Math.abs(start.getX()-end.getX());
        double edge2 = Math.abs(start.getY()-end.getY());
        return Math.sqrt(edge1*edge1+edge2*edge2);
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
        String object = "CIRCLE\t";
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
        this.initPoints[0] = points[0];
        this.initPoints[1] = points[1];
        this.radius = radius(this.points[0],this.points[1]);
        this.fromLoad = true;
    }

    @Override
    public double[] getMeasurements() {
        return new double[]{this.meauRadius,this.area};
    }
}
