package defaultPackage;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The free polygon class, define the draw polygon, label and measure.
 * Polygon will be created by define as many as user want.
 *
 * @author Yanlong LI, u5890571*/
public class PolyElement extends Elements{
    private ArrayList<Point2D> points = new ArrayList<>();
    private ArrayList<Point2D> initPoints = new ArrayList<>();
    private ArrayList<Double> edgeLength = new ArrayList<>();
    private double length = 0;
    private double area = 0;
    private Point2D offset = new Point2D.Double(0,0);
    private double zoomFactor = 15/0.2;
    private ArrayList<Point2D> edgePoints = new ArrayList<>();
    private Color fillColor = Color.BLACK;
    private String describe = "Polygon";
    private boolean showLabel = false;
    private boolean showMeasure = false;
    private boolean showControl = false;
    private String unit = "mm";
    private double unitScale = 1;
    private int group = -1;
    private boolean fromLoad = false;

    public PolyElement(){
        this.points = new ArrayList<>();
        this.initPoints = new ArrayList<>();
    }
    public PolyElement(Point2D pos, Color fillColor){
        this.points = new ArrayList<>();
        this.fillColor = fillColor;
        this.addPoint(pos);
        this.addPoint(pos);
        this.initPoints = this.points;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(this.fillColor);
        for (int i = 0; i < this.points.size()-1; i++) {
            g.draw(new Line2D.Double(this.points.get(i), this.points.get((i+1))));
        }
        if(this.showControl)
            drawControl(g);
        if(this.showMeasure)
            drawMeasure(g);
        if(this.showLabel)
            drawLabel(g);
    }

    private void drawControl(Graphics2D g){
        g.setColor(Color.BLACK);
        ArrayList<Point2D> pt = this.controlPoints();
        for(Point2D p : pt){
            g.draw(new Ellipse2D.Double(p.getX() - 2, p.getY() - 2, 4, 4));
        }
    }
    private void drawLabel(Graphics2D g){
        Font font = g.getFont();
        FontMetrics fm = g.getFontMetrics(font);
        int textWidth = fm.stringWidth(this.describe)/2;
        double X = 0, locationX = 0, Y = 0;
        for (int i = 0; i < this.points.size()-1; i++) {
            for (int j = 0; j < this.points.size()-1; j++) {
                if(Math.abs(this.points.get(i).getX()-this.points.get(j).getX()) >= X){
                    X = Math.abs(this.points.get(i).getX()-this.points.get(j).getX());
                    if(this.points.get(j).getX()<this.points.get(i).getX())
                        locationX = this.points.get(j).getX()+X/2;
                    else locationX = this.points.get(i).getX()+X/2;
                }
            }
            if(this.points.get(i).getY()>=Y)
                Y=this.points.get(i).getY();
        }
        g.draw(new Line2D.Double(new Point2D.Double(locationX,Y+5),new Point2D.Double(locationX,Y+15)));
        g.drawString(this.describe,(int)locationX-textWidth, (int)Y+25);

    }

    private void drawMeasure(Graphics2D g){
        for (int i = 0; i < this.points.size()-1; i++) {
            g.drawString(this.edgeLength.get(i)+this.unit, (int)PUtil.mid(this.points.get(i),this.points.get(i+1)).getX(), (int)PUtil.mid(this.points.get(i),this.points.get(i+1)).getY());
        }
    }
    @Override
    public ArrayList<Point2D> controlPoints() {
        ArrayList<Point2D> controls = new ArrayList<>();
        double distance = 0;
        int indexI = 0, indexJ = 0;
        for (int i = 0; i < this.points.size()-1; i++) {
            for (int j = 0; j < this.points.size()-1; j++) {
                if(distance <= distance(this.points.get(i), this.points.get(j))){
                    distance = distance(this.points.get(i), this.points.get(j));
                    indexI = i;
                    indexJ = j;
                }
            }
        }
        controls.add(PUtil.mid(this.points.get(indexI), this.points.get(indexJ)));
        controls.addAll(this.points);
        return controls;
    }

    @Override
    public void moveControlPoint(int control, Point2D pos) {
        Point2D[] pts = new Point2D[this.points.size()];
        this.points.toArray(pts);
        if(control == 0){
            Point2D vec = PUtil.sub(pos, this.controlPoints().get(0));
            for (int i = 0; i < pts.length; i++) {
                pts[i] = PUtil.add(pts[i], vec);
            }
        }else{
            pts[control-1] = pos;
            if(control == 1){
                pts[pts.length-1] = pos;
            }
        }
        this.points = new ArrayList<>(Arrays.asList(pts));
        this.initPoints = new ArrayList<>(Arrays.asList(pts));
    }

    @Override
    public void setOffset(Point2D pos) {
        this.offset = PUtil.add(pos, this.offset);
        Point2D[] pts = new Point2D[this.points.size()];
        this.points.toArray(pts);
        for (int i = 0; i < pts.length; i++) {
            pts[i] = PUtil.add(pos,pts[i]);
        }
        this.points = new ArrayList<>(Arrays.asList(pts));
        this.initPoints = new ArrayList<>(Arrays.asList(pts));
    }

    @Override
    public void setZoom(double zoom, double centralX, double centralY, double offsetX, double offsetY, double zoomFactor, double gZoom) {
        Point2D[] pts = new Point2D[this.initPoints.size()];
        this.initPoints.toArray(pts);
        for (int i = 0; i < pts.length; i++) {
            Point2D newI = MUtil.getPoint(pts[i],centralX,centralY, offsetX, offsetY, zoomFactor, gZoom);
            pts[i] = MUtil.getLoc(PUtil.scale(newI, zoom/this.zoomFactor), centralX,centralY, offsetX, offsetY, zoomFactor, gZoom);
        }
        this.points = new ArrayList<>(Arrays.asList(pts));
    }

    @Override
    public void setZoomFactor(double zoomFactor, double zoomFactorGlobal) {
        this.zoomFactor = zoomFactor/zoomFactorGlobal;
    }

    @Override
    public boolean isFinish() {
        if(this.fromLoad)
            return true;
        else if(Math.abs(this.points.get(0).getX()-this.points.get(this.points.size()-1).getX())<=5 &&
                Math.abs(this.points.get(0).getY()-this.points.get(this.points.size()-1).getY())<=5 && this.points.size()>2){
            this.points.remove(this.points.size()-1);
            this.points.add(this.points.get(0));
            return true;
        }
        else return false;
    }

    @Override
    public void addPoint(Point2D pos) {
        this.points.add(pos);
    }

    @Override
    public void drawUpdate(Point2D pos) {
        Point2D[] pts = new Point2D[this.points.size()];
        this.points.toArray(pts);
        pts[this.points.size()-1] = pos;
        this.points = new ArrayList<>(Arrays.asList(pts));
        this.initPoints = this.points;
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
        edgeLength = new ArrayList<>();
        for (int i = 0; i < this.points.size()-1; i++) {
            double length = (int)(distance(MUtil.getPoint(this.points.get(i),centralX,centralY,offsetX,offsetY,zoomFactor,gZoom),
                    MUtil.getPoint(this.points.get(i+1),centralX,centralY,offsetX,offsetY,zoomFactor,gZoom)) * 1000 / this.unitScale)/1000.0;
            edgeLength.add(length);

        }
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
        String object = "POLY\t";
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
        this.points = new ArrayList<>(Arrays.asList(points));
        this.initPoints = new ArrayList<>(Arrays.asList(points));
        this.fromLoad = true;
    }

    @Override
    public double[] getMeasurements() {
        double[] edge = new double[this.edgeLength.size()];
        for (int i = 0; i < this.edgeLength.size(); i++) {
            edge[i] = this.edgeLength.get(i);
        }
        return edge;
    }

    private double distance(Point2D p1, Point2D p2){
        double dx = (p1.getX()-p2.getX()) * (p1.getX()-p2.getX());
        double dy = (p1.getY()-p2.getY()) * (p1.getY()-p2.getY());
        return Math.sqrt(dx+dy);
    }
}
