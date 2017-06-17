package cz.dd4j.utils.astar.graph;

public interface ILink<NODE> {

	public LinkType getType();
	
	public NODE getNode1();
	
	public NODE getNode2();
	
	public int getCost();
	
	public boolean mayTravelFrom(NODE node);
	
	public NODE getOther(NODE node);
	
}
