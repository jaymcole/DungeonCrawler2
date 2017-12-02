package ecu.se.pathfinding;

public class Node {
	public int x, y;
	public Node parent;
	public int length;
	
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		parent = null;
		length = 0;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
