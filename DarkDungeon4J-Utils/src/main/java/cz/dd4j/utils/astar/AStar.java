package cz.dd4j.utils.astar;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import cz.dd4j.utils.astar.graph.ILink;
import cz.dd4j.utils.astar.graph.INode;

public class AStar<NODE extends INode<NODE>> {
	
	private class SearchNode {
		
		public NODE node;
		
		public int currentCost;
		
		public SearchNode previous;

		public SearchNode(NODE node, int currentCost, SearchNode previous) {
			super();
			this.node = node;
			this.currentCost = currentCost;
			this.previous = previous;
		}
		
	}
	
	private IAStarHeuristic<NODE> heuristic;

	public AStar(IAStarHeuristic<NODE> heuristic) {
		this.heuristic = heuristic;
	}
	
	public Path<NODE> findPath(NODE from, NODE to) {
		return findPath(from, to, null);
	}
	
	public Path<NODE> findPath(NODE from, NODE to, IAStarView view) {
		
		Map<NODE, SearchNode> nodes = new HashMap<NODE, SearchNode>();
		
		Queue<SearchNode> opened = new PriorityQueue<SearchNode>(new Comparator<SearchNode>() {
			@Override
			public int compare(AStar<NODE>.SearchNode o1, AStar<NODE>.SearchNode o2) {
				return o1.currentCost - o2.currentCost;
			}			
		});
		
		SearchNode initial = new SearchNode(from, 0, null);
		nodes.put(from, initial);
		opened.add(initial);
		
		while (opened.size() > 0) {
			SearchNode current = opened.remove();
			
			if (current == to) {
				return reconstructPath(from, current);
			}
			
			for (ILink<NODE> link : current.node.getLinks()) {
				NODE nextNode = link.getOther(current.node);

				if (view != null && !view.isOpened(nextNode)) {
					continue;
				}
				
				SearchNode next = nodes.get(nextNode);
				if (next == null) {
					next = new SearchNode(nextNode, Integer.MAX_VALUE, current);
					nodes.put(nextNode, next);
				}
				
				int newCost = current.currentCost + link.getCost() + heuristic.getEstimate(nextNode, to);
				if (newCost < next.currentCost) {
					if (next.currentCost != Integer.MAX_VALUE) {
						opened.remove(next);
					}
					next.previous = current;
					next.currentCost = newCost;				
					opened.add(next);
				}				
			}			
		}

		// PATH DOES NOT EXIST
		return null;
	}

	private Path<NODE> reconstructPath(NODE from, AStar<NODE>.SearchNode to) {
		Path<NODE> result = new Path<NODE>();
		
		while (to != null && to != from) {
			result.path.add(to.node);
			to = to.previous;
		}
		
		Collections.reverse(result.path);
		
		return result;
	}

}
