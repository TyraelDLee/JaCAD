package defaultPackage;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * This class is Using  Bresenham’s Line Algorithm.
 * Return the points on the line.
 * @author Yanlong LI, u5890571
 * */
class Line {
    private ArrayList<Point2D> points = new ArrayList<>();

    Line(Point2D start, Point2D end){
        drawLine(start,end);
    }

    private void drawLine(Point2D start, Point2D end){
        int x0 = (int)start.getX(), y0 = (int)start.getY(), xn = (int)end.getX(), yn = (int)end.getY();
        int x, y, dx, dy, incx, incy;
        int d;
        x = x0;
        y = y0;
        dx = xn - x;
        dy = yn - y;
        if (dx >= 0)
            incx = 1;
        else {
            dx = -dx;
            incx = -1;
        }

        if (dy >= 0)
            incy = 1;
        else {
            dy = -dy;
            incy = -1;
        }

        if (dx >= dy) {
            d = dy * 2 - dx;
            for (int i = 0; i < Math.abs(x0 - xn); i++) {
                this.points.add(new Point2D.Double(x,y));
                if (d >= 0) {
                    y += incy;
                    d -= dx * 2;
                }
                d += dy * 2;
                x += incx;
            }
            this.points.add(new Point2D.Double(x,y));
        } else {
            d = dx * 2 - dy;
            for (int i = 0; i < Math.abs(y0 - yn); i++) {
                this.points.add(new Point2D.Double(x,y));
                if (d >= 0) {
                    x += incx;
                    d -= dy * 2;
                }
                d += dx * 2;
                y += incy;
            }
            this.points.add(new Point2D.Double(x,y));
        }
    }

    public ArrayList<Point2D> getPoints(){
        return this.points;
    }
}
