package org.pmf.graph;

import org.pmf.util.NodeID;

public interface DirectedGraphNode extends DirectedGraphElement, Comparable<DirectedGraphNode> {
	
	NodeID getId();

}
