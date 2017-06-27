package cz.dd4j.utils.astar;

import java.util.ArrayList;
import java.util.List;

import cz.dd4j.utils.astar.graph.INode;

public class Path<NODE extends INode<NODE>> {

	public List<NODE> path = new ArrayList<NODE>();

	public int getDistanceNodes() {
		return path.size() > 0 ? path.size() - 1 : 0;
	}
	
	@Override
	public String toString() {
		return "Path[" + (path == null ? "null" : "#size=" + path.size()) + "]";
	}
	
}
