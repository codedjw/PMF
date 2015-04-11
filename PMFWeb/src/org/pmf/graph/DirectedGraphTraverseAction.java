package org.pmf.graph;

public interface DirectedGraphTraverseAction {
	
	public void processNode(DirectedGraphNode node);
	
	public void processNodeWithStringBuffer(DirectedGraphNode node, StringBuffer str);

}
