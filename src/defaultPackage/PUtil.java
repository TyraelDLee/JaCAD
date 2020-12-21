package defaultPackage;

import java.awt.geom.Point2D;

/**
 * PUtil - a utility class for calculations on points
 * Extend from Eric's code
 *
 * @author Yanlong LI, u5890571
 */

public class PUtil {

	public static Point2D mid (Point2D p1, Point2D p2) {
		return new Point2D.Double((p1.getX() + p2.getX())/2.0,(p1.getY() + p2.getY())/2.0) ;
	}
	
	public static Point2D sub (Point2D p1, Point2D p2) {
		return new Point2D.Double((p1.getX() - p2.getX()),(p1.getY() - p2.getY())) ;
	}
	
	public static Point2D add (Point2D p1, Point2D p2) {
		return new Point2D.Double((p1.getX() + p2.getX()),(p1.getY() + p2.getY())) ;
	}

	public static Point2D div(Point2D p1, double d){
		return new Point2D.Double((p1.getX() / d),(p1.getY() / d)) ;
	}
	
	public static Point2D scale (Point2D p1, double s) {
		return new Point2D.Double((p1.getX() * s),(p1.getY() * s)) ;
	}

}
