package org.pmf.graph.impl;

import org.pmf.graph.DirectedGraphNode;

public abstract class AbstractDirectedGraphNode extends AbstractGraphNode implements DirectedGraphNode {

	public AbstractDirectedGraphNode() {
		super();
	}

	public abstract AbstractDirectedGraph<?, ?> getGraph();

	public int compareTo(DirectedGraphNode node) {
		int comp = getId().compareTo(node.getId());
		return comp;

	}

}
