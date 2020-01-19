package ecu.se.map;

import java.awt.geom.Line2D;

public class LineSegment {
	public float x1;
	public float y1;
	public float x2;
	public float y2;
	
	public LineSegment(float x1, float y1, float x2, float y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public boolean DoIntersect(LineSegment line) {
		return Line2D.linesIntersect(x1, y1, x2, y2, line.x1, line.y1, line.x2, line.y2);
	}
}
