package defaultPackage;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * The triangle class, define by 3 points.
 *
 * @author Yanlong LI, u5890571
 * */
public class TriangleElement extends Elements{
    private Point2D[] points = new Point2D[3];
    private Point2D[] initPoints = new Point2D[3];
    private double length = 0;
    private double area = 0;
    private Point2D offset = new Point2D.Double(0,0);
    private double zoomFactor = 15/0.2;
    private int currentPoints = 0;
    private ArrayList<Point2D> edgePoints = new ArrayList<>();
    private Color fillColor = Color.BLACK;
    private String describe = "Triangle";
    private boolean showLabel = false;
    private boolean showMeasure = false;
    private boolean showControl = false;
    private double[] edgeLength = new double[]{0,0,0};
    private String unit = "mm";
    private double unitScale = 1;
    private int group = -1;
    private boolean fromLoad = false;

    public TriangleElement(){}

    public TriangleElement(Point2D start, Color fillColor){
        this.points = new Point2D[]{start,start,start};
        this.initPoints = new Point2D[]{start,start,start};
        this.fillColor =fillColor;
    }

    @Override
    public void draw(Graphics2D g) {
        edgePoints = new ArrayList<>();
        g.setColor(fillColor);
        Line l1 = new Line(points[0], points[1]);
        Line l2 = new Line(points[1], points[2]);
        Line l3 = new Line(points[2], points[0]);
        g.draw(new Line2D.Double(points[0], points[1]));
        g.draw(new Line2D.Double(points[1], points[2]));
        g.draw(new Line2D.Double(points[2], points[0]));
        edgePoints.addAll(l1.getPoints());
        edgePoints.addAll(l2.getPoints());
        edgePoints.addAll(l3.getPoints());
        if(this.showMeasure)
            drawMeasure(g);
        if(this.showControl)
            drawControl(g);
        if(showLabel)
            drawLabel(g);
    }

    private void drawLabel(Graphics2D g) {
        Font font = g.getFont();
        FontMetrics fm = g.getFontMetrics(font);
        int textWidth = fm.stringWidth(this.describe)/2;
        double lowEdge = Double.max(Double.max(points[0].getY(), points[1].getY()), points[2].getY());
        double longEdge = (Double.max(Double.max(points[0].getX(), points[1].getX()), points[2].getX())-Double.min(Double.min(points[0].getX(), points[1].getX()), points[2].getX()))/2+Double.min(Double.min(points[0].getX(), points[1].getX()), points[2].getX());
        Point2D edge = new Point2D.Double(longEdge,lowEdge);
        double midPointOnEdge = 0;
        for(Point2D pts : edgePoints){
            if(pts.getX() <= edge.getX()+5&&pts.getX() >= edge.getX()-5 && pts.getY()>= midPointOnEdge)
                midPointOnEdge = pts.getY();
        }
        g.draw(new Line2D.Double(new Point2D.Double(edge.getX(),midPointOnEdge+5),edge));
        g.drawString(this.describe,(int)edge.getX()-textWidth, (int)edge.getY()+10);
    }

    private void drawMeasure(Graphics2D g){
        for (int i = 0; i < 3; i++) {
            g.drawString(this.edgeLength[i]+this.unit, (int)PUtil.mid(this.points[i%3],this.points[(i+1)%3]).getX(), (int)PUtil.mid(this.points[i%3],this.points[(i+1)%3]).getY());
        }
        g.drawString(this.area+this.unit+"^2", (int)PUtil.mid(PUtil.mid(points[0], points[1]),points[2]).getX(), (int)PUtil.mid(PUtil.mid(points[0], points[1]),points[2]).getY()+15);
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
        controls.add(PUtil.mid(PUtil.mid(points[0], points[1]),points[2]));
        controls.add(points[0]);
        controls.add(points[1]);
        controls.add(points[2]);
        return controls;
    }

    @Override
    public void moveControlPoint(int control, Point2D pos) {
        if (control == 1) // start
            points[0] = pos;
        else if (control == 2) // end
            points[1] = pos;
        else if (control == 3) // mid
            points[2] = pos;
        else if (control == 0){
            Point2D vec = PUtil.sub(pos, PUtil.mid(PUtil.mid(points[0], points[1]),points[2]));
            points[0] = PUtil.add(points[0], vec);
            points[1] = PUtil.add(points[1], vec);
            points[2] = PUtil.add(points[2], vec);
        }
        this.initPoints[0] = this.points[0];
        this.initPoints[1] = this.points[1];
        this.initPoints[2] = this.points[2];
    }

    @Override
    public void setOffset(Point2D pos) {
        this.offset = PUtil.add(pos, this.offset);
        this.points[0] = PUtil.add(pos,this.points[0]);
        this.points[1] = PUtil.add(pos,this.points[1]);
        this.points[2] = PUtil.add(pos,this.points[2]);
        this.initPoints[0] = this.points[0];
        this.initPoints[1] = this.points[1];
        this.initPoints[2] = this.points[2];
    }

    @Override
    public void setZoom(double zoom, double centralX, double centralY, double offsetX, double offsetY, double zoomFactor, double gZoom) {
        for (int i = 0; i < 3; i++) {
            Point2D newPi = MUtil.getPoint(this.initPoints[i],centralX,centralY, offsetX, offsetY, zoomFactor, gZoom);
            this.points[i] = MUtil.getLoc(PUtil.scale(newPi, zoom/this.zoomFactor), centralX,centralY, offsetX, offsetY, zoomFactor, gZoom);
        }
    }

    @Override
    public void setZoomFactor(double zoomFactor, double zoomFactorGlobal) {
        this.zoomFactor = zoomFactor/zoomFactorGlobal;
    }

    @Override
    public boolean isFinish() {
        return this.fromLoad || this.currentPoints == 2;
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
        this.initPoints[2] = this.points[2];
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

    /**
     * This method will return the length for each edge and total length for this element,
     * also return the area for this element, based on the x-y axis, default unit is mm.
     *
     * length = sqrt((p1.getX-p2.getX)^2 +(p1.getY-p2.getY)^2) (Pythagorean Theorem)
     * area = Heron's formula
     *      = sqrt(s(s-a)(s-b)(s-c)), where s = (a+b+c)/2
     * */
    @Override
    public void updateMeasurements(double centralX, double centralY, double offsetX, double offsetY, double zoomFactor, double gZoom) {
        int i = 0;

        for(int j = 0; j < 3; j++){
            double edge = Math.sqrt(
                    (MUtil.getPoint(this.points[i%3],centralX,centralY,offsetX,offsetY,zoomFactor,gZoom).getX() - MUtil.getPoint(this.points[(i+1)%3],centralX,centralY,offsetX,offsetY,zoomFactor,gZoom).getX()) *
                            (MUtil.getPoint(this.points[i%3],centralX,centralY,offsetX,offsetY,zoomFactor,gZoom).getX() - MUtil.getPoint(this.points[(i+1)%3],centralX,centralY,offsetX,offsetY,zoomFactor,gZoom).getX()) +
                            (MUtil.getPoint(this.points[i%3],centralX,centralY,offsetX,offsetY,zoomFactor,gZoom).getY() - MUtil.getPoint(this.points[(i+1)%3],centralX,centralY,offsetX,offsetY,zoomFactor,gZoom).getY()) *
                                    (MUtil.getPoint(this.points[i%3],centralX,centralY,offsetX,offsetY,zoomFactor,gZoom).getY() - MUtil.getPoint(this.points[(i+1)%3],centralX,centralY,offsetX,offsetY,zoomFactor,gZoom).getY())
            );
            this.edgeLength[j] = (int)(edge*1000/ this.unitScale)/1000.0 ;
            this.length += (edge / this.unitScale);
            i++;
        }

        double s = ( this.edgeLength[0]+ this.edgeLength[1]+ this.edgeLength[2]) / 2;
        this.area = (int)(Math.sqrt(s * (s - this.edgeLength[0]) * (s - this.edgeLength[1]) * (s - this.edgeLength[2])) * 1000)/ 1000.0;

        this.length = (int) (this.length*1000) / 1000.0;

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
        String object = "TRIANGLE\t";
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
        this.initPoints[2] = this.points[2];
        this.fromLoad = true;
    }

    @Override
    public double[] getMeasurements() {
        double[] out = new double[this.edgeLength.length+1];
        for (int i = 0; i < this.edgeLength.length; i++) {
            out[i] = this.edgeLength[i];
        }
        out[this.edgeLength.length] = this.area;
        return out;
    }

}
