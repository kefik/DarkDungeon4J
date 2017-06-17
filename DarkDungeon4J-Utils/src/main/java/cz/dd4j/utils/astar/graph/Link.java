package cz.dd4j.utils.astar.graph;

public class Link<NODE> implements ILink<NODE> {

	private NODE n1;
	private NODE n2;
	private LinkType type;
	private int cost;

	public Link(NODE n1, NODE n2, LinkType type, int cost) {
		this.n1 = n1;
		this.n2 = n2;
		this.type = type;
		this.cost = cost;
	}
	
	@Override
	public LinkType getType() {
		return type;
	}

	@Override
	public NODE getNode1() {
		return n1;
	}

	@Override
	public NODE getNode2() {
		return n2;
	}

	@Override
	public int getCost() {
		return cost;
	}

	@Override
	public boolean mayTravelFrom(NODE node) {
		return node == n1 || type == LinkType.BOTH_WAYS && node == n2;
	}

	@Override
	public NODE getOther(NODE node) {
		return node == n1 ? n2 : (node == n2 ? n1 : null);
	}

}
