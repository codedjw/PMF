package org.pmf.graph;

public interface DirectedGraphEdge<S extends DirectedGraphNode, T extends DirectedGraphNode> 
	extends DirectedGraphElement {

	S getSource();

	T getTarget();
	
}
