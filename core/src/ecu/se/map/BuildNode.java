package ecu.se.map;

public class BuildNode {
	public int x, y;
	public Direction direction;
	
	public BuildNode(int x, int y, Direction direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
	
	public String toString() {
		return "(" + x + ", " + y + ", " + direction.shorthand + ")" ;
		
	}
}
