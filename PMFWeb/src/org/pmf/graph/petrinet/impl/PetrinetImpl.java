package org.pmf.graph.petrinet.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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

//	@Override
//	public JSONObject buildJson() {
//		// TODO Auto-generated method stub
//		JSONObject json = new JSONObject();
//		if (transitions != null && !transitions.isEmpty()) {
//			JSONArray tarray = new JSONArray();
//			for (Transition t : transitions) {
//				JSONObject tjson = new JSONObject();
//				tjson.element("tid", t.getId().toString());
//				tjson.element("label", t.getLabel());
//				tjson.element("type", t.getAttributeMap().get(AttributeMap.TYPE));
//				tarray.add(tjson);
//			}
//			json.element("Transitions", tarray);
//		}
//		if (places != null && !places.isEmpty()) {
//			JSONArray parray = new JSONArray();
//			int idx = 0;
//			for (Place p : places) {
//				JSONObject pjson = new JSONObject();
//				pjson.element("pid", p.getId().toString());
//				if (p.getLabel().equals("Start") || p.getLabel().equals("End")) {
//					pjson.element("label", p.getLabel());
//				} else {
//					pjson.element("label", "P"+idx);
//					idx++;
//				}
//				pjson.element("type", p.getAttributeMap().get(AttributeMap.TYPE));
//				parray.add(pjson);
//			}
//			json.element("Places", parray);
//		}
//		if (arcs != null && !arcs.isEmpty()) {
//			JSONArray arc_array = new JSONArray();
//			for (Arc a : arcs) {
//				JSONObject arc_json = new JSONObject();
//				arc_json.element("start", a.getSource().getId().toString());
//				arc_json.element("target", a.getTarget().getId().toString());
//				arc_array.add(arc_json);
//			}
//			json.element("Arcs", arc_array);
//		}
//		return json;
//	}
	
	@Override
	public JSONObject buildJson() {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		JSONArray nodes = null;
		Map<PetrinetNode, Integer> nodeIdx = new HashMap<PetrinetNode, Integer>();
		int idx = 0;
		if (transitions != null && !transitions.isEmpty()) {
			nodes = (nodes == null) ? new JSONArray() : nodes;
			for (Transition t : transitions) {
				JSONObject tjson = new JSONObject();
				tjson.element("label", t.getLabel());
				tjson.element("type", t.getAttributeMap().get(AttributeMap.TYPE));
				nodes.add(tjson);
				nodeIdx.put(t, idx);
				idx++;
			}
		}
		if (places != null && !places.isEmpty()) {
			nodes = (nodes == null) ? new JSONArray() : nodes;
			int pidx = places.size()-2-1;
			for (Place p : places) {
				JSONObject pjson = new JSONObject();
				if (p.getLabel().equals("Start") || p.getLabel().equals("End")) {
					pjson.element("label", p.getLabel());
				} else {
					pjson.element("label", "P"+pidx);
					pidx--;
				}
				pjson.element("type", p.getAttributeMap().get(AttributeMap.TYPE));
				nodes.add(pjson);
				nodeIdx.put(p, idx);
				idx++;
			}
		}
		json.element("nodes", nodes);
		if (arcs != null && !arcs.isEmpty()) {
			JSONArray links = new JSONArray();
			for (Arc a : arcs) {
				JSONObject arc_json = new JSONObject();
				arc_json.element("source", nodeIdx.get(a.getSource()));
				arc_json.element("target", nodeIdx.get(a.getTarget()));
				arc_json.element("type", 1);
				links.add(arc_json);
			}
			json.element("links", links);
		}
		System.out.println("json:"+json);
		return json;
	}

}
