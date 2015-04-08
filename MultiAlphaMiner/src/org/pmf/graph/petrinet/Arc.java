package org.pmf.graph.petrinet;

import org.pmf.graph.petrinet.impl.PetrinetEdge;
import org.pmf.graph.petrinet.impl.PetrinetNode;
import org.pmf.util.AttributeMap;

public class Arc extends PetrinetEdge<PetrinetNode, PetrinetNode> {

	private int weight;
	
	public Arc(PetrinetNode source, PetrinetNode target) {
		this(source, target, 1);
	}
	
	public Arc(PetrinetNode source, PetrinetNode target, int weight) {
		super(source, target, "" + weight);
		this.weight = weight;
	}

	/**
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(int weight) {
		this.weight = weight;
		getAttributeMap().put(AttributeMap.LABEL, "" + weight);
		getGraph().graphElementChanged(this);
	}

	@Override
	public String toString() {
		return ""+ this.source + "-->" + this.target + "(" + this.weight + ")";
	}

}
