package cz.dd4j.utils.astar;

public interface IAStarHeuristic<NODE> {

	public int getEstimate(NODE n1, NODE n2);
	
}
