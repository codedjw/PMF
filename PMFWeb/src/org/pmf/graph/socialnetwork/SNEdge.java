package org.pmf.graph.socialnetwork;

import org.pmf.graph.impl.AbstractDirectedGraphEdge;
import org.pmf.util.AttributeMap;

public class SNEdge extends AbstractDirectedGraphEdge<SNNode, SNNode> {

	private Object identifier;
	
	public SNEdge(SNNode source, SNNode target, Object identifier) {
		super(source, target);
		// TODO Auto-generated constructor stub
		this.identifier = identifier;
		getAttributeMap().put(AttributeMap.LABEL, identifier.toString());
	}
	
	public boolean equals(Object o) {
		return super.equals(o) && identifier.equals(((SNEdge) o).identifier);
	}

	public Object getIdentifier() {
		return identifier;
	}

	public double getWeight() {
		String str = getLabel();
		return Double.valueOf(str.substring(str.indexOf(":") + 1, str.length()));
	}

}
