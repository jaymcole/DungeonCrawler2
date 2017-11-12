package ecu.se.map;

public class BuildNode {
	public int x, y;
	public Direction direction;
	
	public BuildNode(int x, int y, Direction direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
	
	public void add(Direction direction) {
		x += direction.x;
		y += direction.y;
	}
	
	public void forward(int amount) {
		x += direction.x * amount;
		y += direction.y * amount;
	}
	
	public void forward() {
		x += direction.x;
		y += direction.y;
	}
	
	public void turnLeft() {
		this.direction = Direction.nextDirectionCCW(direction);
	}
	
	public void turnRight() {
		this.direction = Direction.nextDirectionCW(direction);
	}
	
	public BuildNode copy() {
		return new BuildNode(x, y, direction);
	}
	
	public static BuildNode min(BuildNode n1, BuildNode n2) {
		if (n1.x <= n2.x && n1.y <= n2.y) 
			return n1;
		else
			return n2;	
	}
	
	public static BuildNode max(BuildNode n1, BuildNode n2) {
		if (n1.x >= n2.x && n1.y >= n2.y) 
			return n1;
		else
			return n2;	
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + direction.shorthand + ")";
	}
	
}
