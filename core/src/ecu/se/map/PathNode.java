package ecu.se.map;

import java.util.ArrayList;
import java.util.LinkedList;

public class PathNode {
	
	private int x, y;
	private LinkedList<Edge> edges;
	
	public PathNode(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void addEdge(PathNode n1, PathNode n2) {
		edges.add(new Edge(n1, n2));
	}
	
	public void removeEdge(Edge e) {
		edges.remove(e);
	}
	
	public LinkedList<Edge> getEdges() {
		return edges;
	}
	
}
