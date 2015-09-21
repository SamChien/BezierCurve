package model;

import java.awt.geom.Point2D;

public class CustomMath {
	public static boolean isNearLine(Point2D p0, Point2D p1, Point2D p2, Point2D p3) {
		double a = p3.getY() - p0.getY();
		double b = -(p3.getX() - p0.getX());
		double c = -(p0.getX() * a + p0.getY() * b);
		double closeConstant = 0.03125;
		
		if (calDistance(a, b, c, p1) <= closeConstant && calDistance(a, b, c, p2) <= closeConstant) {
			return true;
		} else {
			return false;
		}
	}
	
	private static double calDistance(double a, double b, double c, Point2D point) {
		return Math.abs(a * point.getX() + b * point.getY() + c) / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
	}
}
