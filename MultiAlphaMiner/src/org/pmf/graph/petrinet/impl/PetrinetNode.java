package org.pmf.graph.petrinet.impl;

import org.pmf.graph.impl.AbstractDirectedGraph;
import org.pmf.graph.impl.AbstractDirectedGraphNode;
import org.pmf.util.AttributeMap;

public abstract class PetrinetNode extends AbstractDirectedGraphNode {
	
	private AbstractDirectedGraph<PetrinetNode, PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> graph;
	
	public PetrinetNode(AbstractDirectedGraph<PetrinetNode, PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> net, String label) {
		super();
		this.graph = net;
		this.getAttributeMap().put(AttributeMap.LABEL, label);
	}

	@Override
	public AbstractDirectedGraph<PetrinetNode, PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> getGraph() {
		// TODO Auto-generated method stub
		return this.graph;
	}

}
