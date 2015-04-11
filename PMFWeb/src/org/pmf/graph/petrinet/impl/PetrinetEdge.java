package org.pmf.graph.petrinet.impl;

import org.pmf.graph.impl.AbstractDirectedGraphEdge;
import org.pmf.util.AttributeMap;

public abstract class PetrinetEdge<S extends PetrinetNode, T extends PetrinetNode>
		extends AbstractDirectedGraphEdge<S, T> {

	public PetrinetEdge(S source, T target, String label) {
		super(source, target);
		// TODO Auto-generated constructor stub
		getAttributeMap().put(AttributeMap.LABEL, label);
	}

}
