package defaultPackage;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The arc class, define the draw arcs, label and measure.
 * Arc will be created by define 2 points. Each arc has 2
 * top points and 3 control points. Used Bezier curve.
 *
 * @author Yanlong LI, u5890571
 * */
public class ArcElement extends Elements {
    private Point2D[] points = new Point2D[2];
    private Point2D[] initPoints = new Point2D[4];
    private Point2D offset = new Point2D.Double(0,0);
    private double zoomFactor = 15/0.2;
    private int currentPoints = 0;
    private Color fillColor = Color.BLACK;
    private String describe = "Arc";
    private boolean showLabel = false;
    private boolean showMeasure = false;
    private boolean showControl = false;
    private String unit = "mm";
    private double unitScale = 1;
    private int group = -1;
    private boolean fromLoad = false;
    private double length = 0;

    public ArcElement(){}

    public ArcElement(Point2D start, Color fillColor){
        this.points = new Point2D[]{start,start, start, start};
        this.points[1] = new Point2D.Double(2 * 40.0 + points[0].getX(),1 * 40.0 + points[0].getY());
        this.points[2] = new Point2D.Double(1 * 40.0 + points[3].getX(),2 * 40.0 + points[3].getY());
        this.initPoints = new Point2D[]{start, this.points[1], this.points[2], start};
        this.fillColor = fillColor;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(this.fillColor);
        Path2D pa = new Path2D.Double();
        pa.moveTo(points[0].getX(), points[0].getY());
        for (int i = 1; i < points.length - 2; i += 3)
            pa.curveTo(points[i].getX(), points[i].getY(), points[i + 1].getX(), points[i + 1].getY(), points[i + 2].getX(), points[i + 2].getY());
        g.draw(pa);
        if(this.showLabel)
            drawLabel(g);
        if(this.showControl)
            drawControl(g);
        if(this.showMeasure)
            drawMeasure(g);
    }

    private void drawMeasure(Graphics2D g){
        Point2D mid = PUtil.mid(this.points[1], this.points[2]);
        g.drawString(this.length+this.unit, (int)mid.getX(), (int)mid.getY());
    }

    private void drawLabel(Graphics2D g){
        Point2D anchor = PUtil.mid(points[0],points[points.length-1]);
        g.draw(new Line2D.Double(new Point2D.Double(anchor.getX(),anchor.getY()+5), new Point2D.Double(anchor.getX(), anchor.getY()+20)));
        g.drawString(this.describe, (int)anchor.getX(),(int)anchor.getY()+30);
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
        ArrayList<Point2D> controlPoints = new ArrayList<>();
        controlPoints.add(PUtil.mid(points[0],points[points.length-1]));//This point control the translate
        controlPoints.addAll(Arrays.asList(points));
        return controlPoints;
    }

    @Override
    public void moveControlPoint(int control, Point2D pos) {
        if(control == 0){
            Point2D vec = PUtil.sub(pos, PUtil.mid(points[0], points[3]));
            for (int i = 0; i < 4; i++) {
                this.points[i] = PUtil.add(this.points[i], vec);
            }
        }else{
            this.points[control-1] = pos;
        }
        for (int i = 0; i < 4; i++) {
            this.initPoints[i] = this.points[i];
        }
    }

    @Override
    public void setOffset(Point2D pos) {
        this.offset = PUtil.add(pos, this.offset);
        for (int i = 0; i < this.points.length; i++) {
            this.points[i] = PUtil.add(pos,this.points[i]);
            this.initPoints[i] = this.points[i];
        }
    }


    @Override
    public void setZoom(double zoom, double centralX, double centralY, double offsetX, double offsetY, double zoomFactor, double gZoom) {
        Point2D[] newPoints = new Point2D[4];
        for (int i = 0; i < 4; i++) {
            newPoints[i] = MUtil.getPoint(this.initPoints[i],centralX,centralY, offsetX, offsetY, zoomFactor, gZoom);
            this.points[i] = MUtil.getLoc(PUtil.scale(newPoints[i], zoom/this.zoomFactor), centralX,centralY, offsetX, offsetY, zoomFactor, gZoom);
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
        Point2D[] pointsOnXY = new Point2D[4];
        for (int i = 0; i < this.points.length; i++) {
            pointsOnXY[i] = MUtil.getPoint(this.points[i], centralX,centralY,offsetX, offsetY,zoomFactor,gZoom);
        }
        Point2D[] close = new Point2D[100];
        for (int i = 0; i < 100; i++) {
            double t = i/100.0;
            close[i] = new Point2D.Double(Math.pow((1-t), 3)*pointsOnXY[0].getX()+3*(1-t)*t*t*pointsOnXY[1].getX()+3*(1-t)*t*t*pointsOnXY[2].getX()+t*t*t*pointsOnXY[3].getX(),
                    Math.pow((1-t), 3)*pointsOnXY[0].getY()+3*(1-t)*t*t*pointsOnXY[1].getY()+3*(1-t)*t*t*pointsOnXY[2].getY()+t*t*t*pointsOnXY[3].getY());
        }
        double l = 0;
        for(int i = 0; i < close.length-1;i++){
            l+=distance(close[i], close[i+1]);
        }
        this.length = (int)(l / this.unitScale * 1000)/1000.0;
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
        String object = "ARC\t";
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
        for (int i = 0; i < this.points.length; i++) {
            this.initPoints[i] = this.points[i];
        }
        this.fromLoad = true;
    }

    @Override
    public double[] getMeasurements() {
        return new double[]{0};
    }

    private double distance(Point2D p1, Point2D p2){
        double dx = (p1.getX()-p2.getX()) * (p1.getX()-p2.getX());
        double dy = (p1.getY()-p2.getY()) * (p1.getY()-p2.getY());
        return Math.sqrt(dx+dy);
    }
}
