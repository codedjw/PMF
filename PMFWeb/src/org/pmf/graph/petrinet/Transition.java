package org.pmf.graph.petrinet;

import java.util.Arrays;
import java.util.Collection;

import org.pmf.graph.impl.AbstractDirectedGraph;
import org.pmf.graph.impl.GraphIterator;
import org.pmf.graph.impl.GraphIterator.EdgeAcceptor;
import org.pmf.graph.impl.GraphIterator.NodeAcceptor;
import org.pmf.graph.petrinet.impl.PetrinetEdge;
import org.pmf.graph.petrinet.impl.PetrinetNode;
import org.pmf.util.AttributeMap;
import org.pmf.util.NodeID;

public class Transition extends PetrinetNode {

	private boolean isInvisible;

	public Transition(
			AbstractDirectedGraph<PetrinetNode, PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> net,
			String label) {
		super(net, label);
		// TODO Auto-generated constructor stub
		getAttributeMap().put(AttributeMap.TYPE,
				AttributeMap.Type.PN_TRANSITION);
		getAttributeMap().put(AttributeMap.VISIBILITY, true);
	}

	public void setInvisible(boolean invisible) {
		this.isInvisible = invisible;
		if (this.isInvisible) {
			getAttributeMap().put(AttributeMap.VISIBILITY, false);
		} else {
			getAttributeMap().put(AttributeMap.VISIBILITY, true);
		}
	}

	public boolean isInvisible() {
		return this.isInvisible;
	}

	public Collection<Transition> getVisiblePredecessors() {

		final NodeAcceptor<PetrinetNode> nodeAcceptor = new NodeAcceptor<PetrinetNode>() {
			public boolean acceptNode(PetrinetNode node, int depth) {
				return ((depth != 0) && (node instanceof Transition) && !((Transition) node)
						.isInvisible());
			}
		};

		Collection<PetrinetNode> transitions = GraphIterator
				.getDepthFirstPredecessors(
						this,
						getGraph(),
						new EdgeAcceptor<PetrinetNode, PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>>() {

							public boolean acceptEdge(
									PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge,
									int depth) {
								return !nodeAcceptor.acceptNode(
										edge.getTarget(), depth);
							}
						}, nodeAcceptor);
		return Arrays.asList(transitions.toArray(new Transition[0]));

	}

	public Collection<Transition> getVisibleSuccessors() {

		final NodeAcceptor<PetrinetNode> nodeAcceptor = new NodeAcceptor<PetrinetNode>() {
			public boolean acceptNode(PetrinetNode node, int depth) {
				return ((depth != 0) && (node instanceof Transition) && !((Transition) node)
						.isInvisible());
			}
		};

		Collection<PetrinetNode> transitions = GraphIterator
				.getDepthFirstSuccessors(
						this,
						getGraph(),
						new EdgeAcceptor<PetrinetNode, PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>>() {

							public boolean acceptEdge(
									PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge,
									int depth) {
								return !nodeAcceptor.acceptNode(
										edge.getSource(), depth);
							}
						}, nodeAcceptor);

		return Arrays.asList(transitions.toArray(new Transition[0]));
	}

	@Override
	public String toString() {
		return this.getLabel();
	}

}
