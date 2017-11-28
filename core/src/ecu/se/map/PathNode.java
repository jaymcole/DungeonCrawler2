package ecu.se.map;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class PathNode {
	public PathNode pathParent;
	public float costFromStart;
	public float estimatedCostToGoal;
	public float tileCost;
	public int x, y;

	public PathNode(int x, int y, PathNode parentNode, float costFromStart) {
		this.pathParent = parentNode;
		this.x = x;
		this.y = y;
		this.tileCost = 1;
	}

	public float getCost() {
		return costFromStart + estimatedCostToGoal;
	}

	public int compareTo(Object other) {
		float thisValue = this.getCost();
		float otherValue = ((PathNode) other).getCost();
		float v = thisValue - otherValue;
		return (v > 0) ? 1 : (v < 0) ? -1 : 0; // sign function
	}

//	/**
//	 * Gets the cost between this node and the specified adjacent (AKA
//	 * "neighbor" or "child") node.
//	 */
//	public float getCost(PathNode node) {
//		return 1.0f;
//	}

	/**
	 * Gets the estimated cost between this node and the specified node. The
	 * estimated cost should never exceed the true cost. The better the
	 * estimate, the more effecient the search.
	 */
	public float getEstimatedCost(PathNode node) {
//		return 1.0f;
		return (float)Math.sqrt(Math.pow(node.x-x, 2)+Math.pow(node.y-y, 2)) * 2;
//		return Vector2.dst(x, y, node.x, node.y);
//		return (Math.abs(x - node.x)) + (y - node.y);
	}

	/**
	 * Gets the children (AKA "neighbors" or "adjacent nodes") of this node.
	 */
	public List<PathNode> getNeighbors() {
		List<PathNode> nodes = new LinkedList<PathNode>();
		for (int i = 0; i < Direction.values().length; i++) {
//			if (!Direction.isExpanded(Direction.values()[i])) {
//				PathNode neighbor = new PathNode(x + Direction.values()[i].x, y + Direction.values()[i].y, this,
//						costFromStart + 1);

				Tile tile = Map.getTileByIndex(x + Direction.values()[i].x, y + Direction.values()[i].y);
				if (tile != null && !tile.isWall) {
					if (tile.pathNode != null) {
						tile.pathNode.pathParent = this;
						tile.pathNode.costFromStart = costFromStart + tile.pathNode.getCost();
						
						nodes.add(tile.pathNode);
						System.out.println("Adding new pathnode: " + tile.pathNode );
					} else {
						System.err.println("PathNode is null");
					}
//				}
			}
		}
		return nodes;
	}

	// public boolean equals (PathNode node) {
	// System.out.println("Comparing: " + x + "=" + node.x + ", " + y + "=" +
	// node.y + " == " + (x == node.x && node.y == y));
	// return x == node.x && node.y == y;
	// }

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

}