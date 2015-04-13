package org.pmf.graph.petrinet.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.pmf.graph.DirectedGraph;
import org.pmf.graph.DirectedGraphEdge;
import org.pmf.graph.DirectedGraphNode;
import org.pmf.graph.DirectedGraphTraverseAction;
import org.pmf.graph.impl.AbstractDirectedGraph;
import org.pmf.graph.petrinet.Arc;
import org.pmf.graph.petrinet.Petrinet;
import org.pmf.graph.petrinet.Place;
import org.pmf.graph.petrinet.Transition;
import org.pmf.util.AttributeMap;

public class PetrinetImpl extends AbstractDirectedGraph<PetrinetNode, PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> implements
		Petrinet, DirectedGraphTraverseAction {
	
	protected Set<Transition> transitions;
	protected Set<Place> places;
	protected Set<Arc> arcs;
	
	public PetrinetImpl(String label) {
		super();
		this.transitions = new LinkedHashSet<Transition>();
		this.places = new LinkedHashSet<Place>();
		this.arcs = new LinkedHashSet<Arc>();
		this.getAttributeMap().put(AttributeMap.LABEL, label);
	}

	@Override
	public synchronized Transition addTransition(String label) {
		// TODO Auto-generated method stub
		Transition t = new Transition(this, label);
		this.transitions.add(t);
		graphElementAdded(t);
		return t;
	}

	@Override
	public synchronized Transition removeTransition(Transition transition) {
		// TODO Auto-generated method stub
		this.removeSurroundingEdges(transition);
		return this.removeNodeFromCollection(this.transitions, transition);
	}

	@Override
	public synchronized Collection<Transition> getTransitions() {
		// TODO Auto-generated method stub
		return this.transitions;
	}

	@Override
	public synchronized Place addPlace(String label) {
		// TODO Auto-generated method stub
		Place p = new Place(this, label);
		this.places.add(p);
		graphElementAdded(p);
		return p;
	}

	@Override
	public synchronized Place removePlace(Place place) {
		// TODO Auto-generated method stub
		this.removeSurroundingEdges(place);
		return this.removeNodeFromCollection(this.places, place);
	}

	@Override
	public synchronized Collection<Place> getPlaces() {
		// TODO Auto-generated method stub
		return this.places;
	}
	
	protected synchronized Arc addArcPrivate(PetrinetNode src, PetrinetNode tgt, int weight) {
		synchronized (this.arcs) {
			if (this.checkAddEdge(src, tgt)) {
				Arc a = new Arc(src, tgt, weight);
				if (this.arcs.add(a)) {
					this.graphElementAdded(a);
					return a;
				} else {
					// update
					for (Arc arc : this.arcs) {
						if (arc.equals(a)) {
							arc.setWeight(arc.getWeight() + weight);
							return arc;
						}
					}
				}
			}
			return null;
		}
	}

	@Override
	public synchronized Arc addArc(Place p, Transition t, int weight) {
		// TODO Auto-generated method stub
		return this.addArcPrivate(p, t, weight);
	}

	@Override
	public synchronized Arc addArc(Place p, Transition t) {
		// TODO Auto-generated method stub
		return this.addArcPrivate(p, t, 1);
	}

	@Override
	public synchronized Arc addArc(Transition t, Place p, int weight) {
		// TODO Auto-generated method stub
		return this.addArcPrivate(t, p, weight);
	}

	@Override
	public synchronized Arc addArc(Transition t, Place p) {
		// TODO Auto-generated method stub
		return this.addArcPrivate(t, p, 1);
	}

	@Override
	public synchronized Arc removeArc(PetrinetNode source, PetrinetNode target) {
		// TODO Auto-generated method stub
		Arc a = this.removeFromEdges(source, target, this.arcs);
		return a;
	}

	@Override
	public synchronized Arc getArc(PetrinetNode source, PetrinetNode target) {
		// TODO Auto-generated method stub
		Collection<Arc> all = this.getEdges(source, target, this.arcs);
		return (all.isEmpty() ? null : all.iterator().next());
	}

	@Override
	public synchronized Set<PetrinetNode> getNodes() {
		// TODO Auto-generated method stub
		Set<PetrinetNode> nodes = new HashSet<PetrinetNode>();
		nodes.addAll(this.transitions);
		nodes.addAll(this.places);
		return nodes;
	}

	@Override
	public synchronized Set<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> getEdges() {
		// TODO Auto-generated method stub
		Set<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> edges = new HashSet<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>>();
		edges.addAll(this.arcs);
		return edges;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void removeEdge(DirectedGraphEdge edge) {
		// TODO Auto-generated method stub
		if (edge instanceof Arc) {
			this.arcs.remove(edge);
		}
		this.graphElementRemoved(edge);
	}

	@Override
	public synchronized void removeNode(DirectedGraphNode node) {
		// TODO Auto-generated method stub
		if (node instanceof Transition) {
			this.removeTransition((Transition) node);
		} else if (node instanceof Place) {
			this.removePlace((Place) node);
		}
		this.graphElementRemoved(node);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PetrinetImpl [transitions=" + transitions + ", places="
				+ places + ", arcs=" + arcs + "]";
	}

	@Override
	public synchronized Collection<Arc> getArcs() {
		// TODO Auto-generated method stub
		return this.arcs;
	}

	@Override
	public void processNode(DirectedGraphNode node) {
		// TODO Auto-generated method stub
		System.out.print(node.getLabel()+" ");
	}

	@Override
	public void printPetrinet(PetrinetNode startNode) {
		System.out.println("Transitions: "+this.transitions);
		System.out.println("Places: "+this.places);
		// TODO Auto-generated method stub
		System.out.println("Arcs: ");
		this.BFS(this, startNode);
	}

	@Override
	public void processNodeWithStringBuffer(DirectedGraphNode node, StringBuffer str) {
		// TODO Auto-generated method stub
		str.append(node.getLabel());
		if (node.getLabel().equals("End")) {
			System.out.println(str);
		} else {
			str.append("-->");
		}
	}

	@Override
	public void petrinet2SVG(PetrinetNode startNode) {
		// TODO Auto-generated method stub
		this.BFSTOSVG(startNode);
	}

}
