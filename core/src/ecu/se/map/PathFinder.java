package ecu.se.map;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import ecu.se.Globals;

/**
 * The AStarSearch class, along with the AStarNode class, implements a generic
 * A* search algorithm. The AStarNode class should be subclassed to provide
 * searching capability.
 */
public class PathFinder {

	/**
	 * A simple priority list, also called a priority queue. Objects in the list
	 * are ordered by their priority, determined by the object's Comparable
	 * interface. The highest priority item is first in the list.
	 */
	public static class PriorityList extends LinkedList {

		public void add(Comparable object) {
			for (int i = 0; i < size(); i++) {
				if (object.compareTo(get(i)) <= 0) {
					add(i, object);
					return;
				}
			}
			addLast(object);
		}
	}

	/**
	 * Construct the path, not including the start node.
	 */
	protected LinkedList<Vector2> constructPath(PathNode node, PathNode goal) {
		LinkedList<Vector2> path = new LinkedList<Vector2>();
		System.out.println("Starting construction");
		while (node.pathParent != null) {
			System.out.println("Adding " + node.toString());
			path.addFirst(new Vector2(node.x * Globals.TILE_PIXEL_WIDTH + (Globals.TILE_PIXEL_WIDTH * 0.5f),
					node.y * Globals.TILE_PIXEL_HEIGHT + (Globals.TILE_PIXEL_HEIGHT * 0.5f)));
			System.out.println("Setting " + node.toString() + " to " + node.pathParent.toString());

			PathNode temp = node.pathParent;
			node.pathParent = null;
			node = temp;
		}
		return path;
	}

	private int counter = 0;

	/**
	 * Find the path from the start node to the end node. A list of AStarNodes
	 * is returned, or null if the path is not found.
	 */
	public LinkedList<Vector2> findPath(PathNode startNode, PathNode goalNode) {
		for (int i = 0; i < Map.getCurrentFloor().getMapWidth(); i++) {
			for (int j = 0; j < Map.getCurrentFloor().getMapHeight(); j++) {
				Map.getTileByIndex(i, j).pathNode.pathParent = null;
			}
		}

		System.out.println("Find Path");
		PriorityList openList = new PriorityList();
		LinkedList closedList = new LinkedList();

		startNode.costFromStart = 0;
		startNode.estimatedCostToGoal = startNode.getEstimatedCost(goalNode);
		startNode.pathParent = null;
		openList.add(startNode);
		System.out.println("Start Loop");
		counter = 0;

		if (Map.getTileByIndex(startNode.x, startNode.y) == null
				|| Map.getTileByIndex(startNode.x, startNode.y).isWall) {
			return null;
		}

		while (!openList.isEmpty()) {
			System.out.println("Loop iteration " + counter);
			counter++;

			if (counter > 1000)
				return null;
			
			PathNode node = (PathNode) openList.removeFirst();

			if (node == goalNode) {
				// if (node.equals(goalNode)) {
				// construct the path from start to goal
				System.err.println("Constructing node path");
				return constructPath(goalNode, startNode);
			}

			System.out.println(node.toString());
			List neighbors = node.getNeighbors();
			for (int i = 0; i < neighbors.size(); i++) {
				PathNode neighborNode = (PathNode) neighbors.get(i);
				boolean isOpen = openList.contains(neighborNode);
				// boolean isOpen = contains(neighborNode, openList);
				boolean isClosed = closedList.contains(neighborNode);
				// boolean isClosed = contains(neighborNode, closedList);
//				float costFromStart = node.costFromStart + node.getCost(neighborNode);
				float costFromStart = node.costFromStart + neighborNode.tileCost;
				// check if the neighbor node has not been
				// traversed or if a shorter path to this
				// neighbor node is found.
				System.out.println("isOpen=" + isOpen + "   isClosed=" + isClosed);

				System.out.println("\t" + neighborNode.toString());
				if ((!isOpen && !isClosed) || costFromStart < neighborNode.costFromStart) {
					neighborNode.pathParent = node;
					neighborNode.costFromStart = costFromStart;
					neighborNode.estimatedCostToGoal = neighborNode.getEstimatedCost(goalNode);
					if (isClosed) {
						boolean removed = closedList.remove(neighborNode);
						System.out.println("node removed=" + removed);
					}
					if (!isOpen) {
						openList.add(neighborNode);
					}
				}
			}
			// node.pathParent = null;
			closedList.add(node);
		}

		// no path found
		return null;
	}

}

/*
 * System.out.println("Find Path"); PriorityList openList = new PriorityList();
 * LinkedList closedList = new LinkedList();
 * 
 * startNode.costFromStart = 0; startNode.estimatedCostToGoal =
 * startNode.getEstimatedCost(goalNode); startNode.pathParent = null;
 * openList.add(startNode); System.out.println("Start Loop"); counter = 0; while
 * (!openList.isEmpty()) { System.out.println("Loop iteration " + counter);
 * counter++;
 * 
 * 
 * PathNode node = (PathNode) openList.removeFirst();
 * 
 * // if (node.x > 1000) // break;
 * 
 * // if (node == goalNode) { if (node.equals(goalNode)) { // construct the path
 * from start to goal return constructPath(goalNode); }
 * 
 * System.out.println(node.toString()); List neighbors = node.getNeighbors();
 * for (int i = 0; i < neighbors.size(); i++) { PathNode neighborNode =
 * (PathNode) neighbors.get(i); // boolean isOpen =
 * openList.contains(neighborNode); boolean isOpen = contains(neighborNode,
 * openList); // boolean isClosed = closedList.contains(neighborNode); boolean
 * isClosed = contains(neighborNode, closedList); float costFromStart =
 * node.costFromStart + node.getCost(neighborNode); // check if the neighbor
 * node has not been // traversed or if a shorter path to this // neighbor node
 * is found. System.out.println("\n" + node.toString()); if ((!isOpen &&
 * !isClosed) || costFromStart < neighborNode.costFromStart) {
 * neighborNode.pathParent = node; neighborNode.costFromStart = costFromStart;
 * neighborNode.estimatedCostToGoal = neighborNode.getEstimatedCost(goalNode);
 * if (isClosed) { // closedList.remove(neighborNode); remove(neighborNode,
 * closedList); } if (!isOpen) { // openList.add(neighborNode); //
 * remove(neighborNode, openList); openList.add(neighborNode); } } }
 * closedList.add(node); }
 * 
 * // no path found return null; }
 * 
 */
