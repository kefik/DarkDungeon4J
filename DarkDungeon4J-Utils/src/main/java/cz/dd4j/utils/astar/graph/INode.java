package cz.dd4j.utils.astar.graph;

import java.util.Collection;

public interface INode<NODE extends INode> {

	public Collection<ILink<NODE>> getLinks();
	
}
