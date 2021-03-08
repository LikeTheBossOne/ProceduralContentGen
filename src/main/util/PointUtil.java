package main.util;

import java.awt.Point;

public class PointUtil {
	public static boolean isInside(Point P, Point A, Point B, Point C) {
		double ABC = area(A, B, C);
		double PBC = area(P, B, C);
		double PAC = area(P, A, C);
		double PAB = area(P, A, B);
		
		return (ABC == PBC + PAC + PAB);
	}
	
	private static double area(Point p1, Point p2, Point p3) {
		return Math.abs((p1.x * (p2.y - p3.y) + p2.x * (p3.y - p1.y) + p3.x * (p1.y - p2.y))/2.0); 
	}
	
	public static double distance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
	}
}
