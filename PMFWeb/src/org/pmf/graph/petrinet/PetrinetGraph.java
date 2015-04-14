package org.pmf.graph.petrinet;

import java.util.Collection;

import org.pmf.graph.DirectedGraph;
import org.pmf.graph.petrinet.impl.PetrinetEdge;
import org.pmf.graph.petrinet.impl.PetrinetNode;

public interface PetrinetGraph extends DirectedGraph<PetrinetNode, PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> {
	
	String getLabel();
	
	// Transitions
	Transition addTransition(String label);
	
	Transition removeTransition(Transition transition);
	
	Collection<Transition> getTransitions();
	
	// Places
	Place addPlace(String label);
	
	Place removePlace(Place place);
	
	Collection<Place> getPlaces();

	// Arcs
	Arc addArc(Place p, Transition t, int weight);

	Arc addArc(Place p, Transition t);

	Arc addArc(Transition t, Place p, int weight);

	Arc addArc(Transition t, Place p);
	
	Arc removeArc(PetrinetNode source, PetrinetNode target);

	Arc getArc(PetrinetNode source, PetrinetNode target);
	
	Collection<Arc> getArcs();
	
}
