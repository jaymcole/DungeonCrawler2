package pathfinding;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;

import ecu.se.Globals;
import ecu.se.map.Direction;
import ecu.se.map.Map;

public class Pathfinder2 {

	private static Node[][] nodes;
	// private static LinkedList<Node> openList;
	private static Queue<Node> openList;

	private static void addAdjacent(Node parent, Node goal) {

		LinkedList<Node> adjacent = new LinkedList<Node>();
		for (Direction d : Direction.values()) {
			if (inBounds(parent.x + d.x, parent.y + d.y)) {
				if (!Direction.isExpanded(d)) {
					if (!Map.getTileByIndex(parent.x + d.x, parent.y + d.y).isWall) {
						if (nodes[parent.x + d.x][parent.y + d.y] == null) {
							nodes[parent.x + d.x][parent.y + d.y] = new Node(parent.x + d.x, parent.y + d.y);
							nodes[parent.x + d.x][parent.y + d.y].parent = parent;
							nodes[parent.x + d.x][parent.y + d.y].length = parent.length + 1;
							openList.addLast(nodes[parent.x + d.x][parent.y + d.y]);
						} else if (isEqual(nodes[parent.x + d.x][parent.y + d.y], goal)) {
							goal.parent = nodes[parent.x + Direction.oppositeDirection(d).x][parent.y
									+ Direction.oppositeDirection(d).y];
							openList.addFirst(goal);
							return;
						}
					}
				}
			}
		}

	}

	private static boolean isEqual(Node n1, Node n2) {
		System.out.println(n1.toString() + "==" + n2.toString() + "   " + (n1.x == n2.x && n1.y == n2.y));
		return n1.x == n2.x && n1.y == n2.y;
	}

	private static LinkedList<Vector2> constructPath(Node node) {
		LinkedList<Vector2> path = new LinkedList<Vector2>();
		while (node.parent != null) {
			path.addFirst(new Vector2(node.x * Globals.TILE_PIXEL_WIDTH + (Globals.TILE_PIXEL_WIDTH * 0.5f),
					node.y * Globals.TILE_PIXEL_HEIGHT + (Globals.TILE_PIXEL_HEIGHT * 0.5f)));
			node = node.parent;
		}
		return path;
	}

	public static LinkedList<Vector2> findPath(Vector2 start, Vector2 end) {
		nodes = new Node[Map.getCurrentFloor().getMapWidth()][Map.getCurrentFloor().getMapHeight()];
		openList = new Queue<Node>();
		int sx = (int) start.x / Globals.TILE_PIXEL_WIDTH;
		int sy = (int) start.y / Globals.TILE_PIXEL_HEIGHT;
		int ex = (int) end.x / Globals.TILE_PIXEL_WIDTH;
		int ey = (int) end.y / Globals.TILE_PIXEL_HEIGHT;

		Node startNode = new Node(sx, sy);
		startNode.parent = null;
		Node goalNode = new Node(ex, ey);

		nodes[sx][sy] = startNode;
		nodes[ex][ey] = goalNode;

		Node currentNode = startNode;
		openList.addLast(currentNode);
		while (openList != null && openList.first() != null) { // openList.first() != null) {
			// System.out.println("Current length: " + currentNode.length);
			currentNode = openList.first();
			openList.removeFirst();

			if (isEqual(currentNode, goalNode)) {
				// System.err.println("Constructing Path");
				return constructPath(currentNode);
			}
			if (currentNode.length > Globals.MAX_PATH_LENGTH) {
				// System.err.println("Returning null");
				return null;
			}
			addAdjacent(currentNode, goalNode);
		}
		// System.err.println("No path found");
		return null;
	}

	private static boolean inBounds(int x, int y) {
		return (x > 0 && x < nodes.length) && (y > 0 && y < nodes[0].length);
	}
}
