package org.pmf.graph.socialnetwork;

import org.pmf.graph.impl.AbstractDirectedGraph;
import org.pmf.graph.impl.AbstractDirectedGraphNode;
import org.pmf.util.AttributeMap;

public class SNNode extends AbstractDirectedGraphNode {
	
	private Object identifier;
	private SocialNetworkImpl graph;
	
	public SNNode(Object identifier, SocialNetworkImpl graph) {
		super();
		this.identifier = identifier;
		this.graph = graph;
		// getAttributeMap().put()...
		getAttributeMap().put(AttributeMap.LABEL, identifier.toString());
	}
	
	public Object getIdentifier() {
		return identifier;
	}
	
	public boolean equals(Object o) {
		return (o instanceof SNNode ? identifier.equals(((SNNode) o).identifier) : false);
	}
	
	public int hashCode() {
		return identifier.hashCode();
	}

	@Override
	public SocialNetworkImpl getGraph() {
		// TODO Auto-generated method stub
		return graph;
	}
	
	public int getInDegree() {
		return graph.getInDegree(this);
	}

	public int getOutDegree() {
		return graph.getOutDegree(this);
	}

	public int getDegree() {
		return graph.getDegree(this);
	}

	public double getInWeightDegree() {
		return graph.getInWeightDegree(this);
	}

	public double getOutWeightDegree() {
		return graph.getOutWeightDegree(this);
	}

	public double getWeightDegree() {
		return graph.getWeightDegree(this);
	}

}
