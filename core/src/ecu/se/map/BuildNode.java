package ecu.se.map;

/**
 *
 * A utility class used for generating maps.
 *
 */
public class BuildNode {
	public int x, y;
	public Direction direction;
	
	public BuildNode(int x, int y, Direction direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
	
	/**
	 * Moves this node one space in the direction of direction
	 * @param direction
	 */
	public void add(Direction direction) {
		x += direction.x;
		y += direction.y;
	}
	
	/**
	 * Moves this node forward amount spaces
	 * @param amount
	 */
	public void forward(int amount) {
		x += direction.x * amount;
		y += direction.y * amount;
	}
	
	/**
	 * Moves this node forward one space
	 */
	public void forward() {
		x += direction.x;
		y += direction.y;
	}
	
	/**
	 * Turns the node to the left
	 */
	public void turnLeft() {
		this.direction = Direction.nextDirectionCCW(direction);
	}
	
	/**
	 * Turns the node to the right
	 */
	public void turnRight() {
		this.direction = Direction.nextDirectionCW(direction);
	}
	
	/**
	 * 
	 * @return a (new) copied node
	 */
	public BuildNode copy() {
		return new BuildNode(x, y, direction);
	}
	
	/**
	 * 
	 * @param n1
	 * @param n2
	 * @return the smaller of two nodes
	 */
	public static BuildNode min(BuildNode n1, BuildNode n2) {
		if (n1.x <= n2.x && n1.y <= n2.y) 
			return n1;
		else
			return n2;	
	}
	
	/**
	 * 
	 * @param n1
	 * @param n2
	 * @return the larger of two nodes
	 */
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
