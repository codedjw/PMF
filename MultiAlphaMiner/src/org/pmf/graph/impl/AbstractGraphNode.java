package org.pmf.graph.impl;

import org.pmf.util.NodeID;

public class AbstractGraphNode extends AbstractGraphElement {
	
	protected final NodeID id = new NodeID();
	
	public AbstractGraphNode() {
		super();
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof AbstractGraphNode)) {
			return false;
		}
		AbstractGraphNode node = (AbstractGraphNode) o;
		return node.id.equals(id);
	}
	
	public int hashCode() {
		return id.hashCode();
	}
	
	public NodeID getId() {
		return id;
	}

}
