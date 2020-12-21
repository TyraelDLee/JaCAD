package defaultPackage.testing;

import defaultPackage.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.junit.Assert.assertEquals;
/**
 * @author Yanlong LI, u5890571
 */

public class MeasureTest {
    private LineElement line = new LineElement();
    private ArcElement arc = new ArcElement();
    private TriangleElement triangle = new TriangleElement();
    private RectangleElement rectangle = new RectangleElement();
    private PolyElement polygon = new PolyElement();
    private CircleElement circle = new CircleElement();
    private double centralX = 210, centralY = 360, offsetX = 0, offsetY = 0, zoomFactor = 15, gZoom = 0.2;
    @Before
    public void setup(){
        line.load(zoomFactor / gZoom, centralX,centralY,offsetX,offsetY,zoomFactor,gZoom,"text", Color.BLACK, new Point2D[]{new Point2D.Double(100,100), new Point2D.Double(700,700)}, -1);
        line.setZoomFactor(zoomFactor,gZoom);
        line.updateMeasurements(centralX,centralY,offsetX, offsetY,zoomFactor,gZoom);
        arc.load(zoomFactor / gZoom, centralX,centralY,offsetX,offsetY,zoomFactor,gZoom,"text", Color.BLACK, new Point2D[]{new Point2D.Double(100,100), new Point2D.Double(700,700)}, -1);
        triangle.load(zoomFactor / gZoom, centralX,centralY,offsetX,offsetY,zoomFactor,gZoom,"text", Color.BLACK, new Point2D[]{new Point2D.Double(100,100), new Point2D.Double(700,700), new Point2D.Double(600,600)}, -1);
        triangle.setZoomFactor(zoomFactor,gZoom);
        triangle.updateMeasurements(centralX,centralY,offsetX, offsetY,zoomFactor,gZoom);
        rectangle.load(zoomFactor / gZoom, centralX,centralY,offsetX,offsetY,zoomFactor,gZoom,"text", Color.BLACK, new Point2D[]{new Point2D.Double(100,150), new Point2D.Double(500,700)}, -1);
        rectangle.setZoomFactor(zoomFactor,gZoom);
        rectangle.updateMeasurements(centralX,centralY,offsetX, offsetY,zoomFactor,gZoom);
        polygon.load(zoomFactor / gZoom, centralX,centralY,offsetX,offsetY,zoomFactor,gZoom,"text", Color.BLACK, new Point2D[]{new Point2D.Double(100,100), new Point2D.Double(700,700), new Point2D.Double(600,600)}, -1);
        polygon.setZoomFactor(zoomFactor,gZoom);
        polygon.updateMeasurements(centralX,centralY,offsetX, offsetY,zoomFactor,gZoom);
        circle.load(zoomFactor / gZoom, centralX,centralY,offsetX,offsetY,zoomFactor,gZoom,"text", Color.BLACK, new Point2D[]{new Point2D.Double(700,700), new Point2D.Double(600,600)}, -1);
        circle.setZoomFactor(zoomFactor,gZoom);
        circle.updateMeasurements(centralX,centralY,offsetX, offsetY,zoomFactor,gZoom);
    }

    @Test
    public void testLine(){
        double actualLength = (int)(Math.sqrt(600*600+600*600)*1000)/1000.0;
        double lineLength = line.getMeasurements()[0];
        assertEquals("Expect " + actualLength + " but get " + lineLength, actualLength, lineLength, 0.0);
    }

    @Test
    public void testTriangle(){
        double[] actualLength = new double[]{(int)(Math.sqrt(600*600+600*600)*1000)/1000.0, (int)(Math.sqrt(100*100+100*100)*1000)/1000.0, (int)(Math.sqrt(500*500+500*500)*1000)/1000.0};
        for (int i = 0; i < triangle.getMeasurements().length-1; i++) {
            assertEquals("Expect " + actualLength[i] + " but get " + triangle.getMeasurements()[i], actualLength[i], triangle.getMeasurements()[i], 0.0);
        }
    }

    @Test
    public void testRectangle(){
        double[] actualLength = new double[]{500-100,700-150, (500-100)*(700-150)};
        for (int i = 0; i < rectangle.getMeasurements().length; i++) {
            assertEquals("Expect " + actualLength[i] + " but get " + rectangle.getMeasurements()[i], actualLength[i], rectangle.getMeasurements()[i], 0.0);
        }
    }

    @Test
    public void testPoly(){
        double[] actualLength = new double[]{(int)(Math.sqrt(600*600+600*600)*1000)/1000.0, (int)(Math.sqrt(100*100+100*100)*1000)/1000.0, (int)(Math.sqrt(500*500+500*500)*1000)/1000.0};
        for (int i = 0; i < polygon.getMeasurements().length; i++) {
            assertEquals("Expect " + actualLength[i] + " but get " + polygon.getMeasurements()[i], actualLength[i], polygon.getMeasurements()[i], 0.0);
        }
    }

    @Test
    public void testCircle(){
        double[] actualLength = new double[]{(int)(Math.sqrt(100*100*2)*1000)/1000.0, (int)(Math.PI*(Math.sqrt(100*100*2)*Math.sqrt(100*100*2))*1000)/1000.0};
        for (int i = 0; i < circle.getMeasurements().length; i++) {
            assertEquals("Expect " + actualLength[i] + " but get " + circle.getMeasurements()[i], actualLength[i], circle.getMeasurements()[i], 1.0);
        }
    }
}
