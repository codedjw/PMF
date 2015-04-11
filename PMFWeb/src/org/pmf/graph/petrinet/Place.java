package org.pmf.graph.petrinet;

import org.pmf.graph.impl.AbstractDirectedGraph;
import org.pmf.graph.petrinet.impl.PetrinetEdge;
import org.pmf.graph.petrinet.impl.PetrinetNode;
import org.pmf.util.AttributeMap;
import org.pmf.util.NodeID;

public class Place extends PetrinetNode {

	public Place(
			AbstractDirectedGraph<PetrinetNode, PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> net,
			String label) {
		super(net, label);
		// TODO Auto-generated constructor stub
		getAttributeMap().put(AttributeMap.TYPE, AttributeMap.Type.PN_PLACE);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getLabel();
	}

}
