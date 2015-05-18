package org.pmf.graph.socialnetwork;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.pmf.graph.DirectedGraph;
import org.pmf.graph.DirectedGraphEdge;
import org.pmf.graph.DirectedGraphNode;
import org.pmf.graph.impl.AbstractDirectedGraph;
import org.pmf.util.AttributeMap;

public class SocialNetworkImpl extends AbstractDirectedGraph<SNNode, SNEdge> implements
		SocialNetwork {
	
	public final Set<SNNode> SNNodes = new LinkedHashSet<SNNode>();
	public final Set<SNEdge> SNEdges = new LinkedHashSet<SNEdge>();
	
	public SocialNetworkImpl(String label) {
		super();
		this.getAttributeMap().put(AttributeMap.LABEL, label);
	}
	
	public synchronized void removeEdge(DirectedGraphEdge edge) {
		if (edge instanceof SNEdge) {
			SNEdge e = (SNEdge) edge;
			this.removeSNEdge(e);
		}
	}
	
	public synchronized void removeNode(DirectedGraphNode node) {
		if (node instanceof SNNode) {
			SNNode n = (SNNode) node;
			this.removeSNNodePrivate(n);
		}
	}
	
	public synchronized Collection<SNEdge> getSNEdges() {
		Collection<SNEdge> edges = new ArrayList<SNEdge>(SNEdges.size());
		for (SNEdge edge : SNEdges) {
			edges.add(edge);
		}
		return edges;
	}
	
	public synchronized Set<SNEdge> getEdges() {
		return this.SNEdges;
	}
	
	public synchronized Object removeSNEdge(SNEdge edge) {
		SNEdge result = this.removeNodeFromCollection(this.SNEdges, edge);
		return (result == null ? null : result.getIdentifier());
	}
	
	public synchronized Collection<SNNode> getSNNodes() {
		Collection<SNNode> nodes = new ArrayList<SNNode>(SNNodes.size());
		for (SNNode node : SNNodes) {
			nodes.add(node);
		}
		return nodes;
	}
	
	public synchronized Set<SNNode> getNodes() {
		return this.SNNodes;
	}
	
	public synchronized boolean addSNNode(Object identifier) {
		SNNode node = new SNNode(identifier, this);
		if (SNNodes.add(node)) {
			this.graphElementAdded(node);
			return true;
		} else {
			return false;
		}
	}
	
	public synchronized boolean addSNEdge(Object fromID, Object toID, Object identifier) {
		SNNode source = new SNNode(fromID, this);
		SNNode target = new SNNode(toID, this);
		if (this.checkAddEdge(source, target)) {
			SNEdge edge = new SNEdge(source, target, identifier);
			if (this.SNEdges.add(edge)) {
				this.graphElementAdded(edge);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public synchronized boolean addSNEdge(SNNode from, SNNode to, Object identifier) {
		if (this.checkAddEdge(from, to)) {
			SNEdge edge = new SNEdge(from, to, identifier);
			if (this.SNEdges.add(edge)) {
				this.graphElementAdded(edge);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	
	
	public synchronized Object removeSNNode(Object identifier) {
		return this.removeSNNodePrivate(this.findSNNode(identifier));
	}
	
	public synchronized Object removeSNNodePrivate(SNNode node) {
		SNNode result = this.removeNodeFromCollection(this.SNNodes, node);
		return (result == null ? null : result.getIdentifier());
	}
	
	public synchronized SNNode findSNNode(Object identifier) {
		for (SNNode n : this.SNNodes) {
			if (n.getIdentifier().equals(identifier)) {
				return n;
			}
		}
		return null;
	}
	
	public synchronized Object removeSNEdge(SNNode from, SNNode to, Object identifier) {
		return this.removeSNEdge(new SNEdge(this.findSNNode(from.getIdentifier()), this.findSNNode(to.getIdentifier()), identifier));
	}

	@Override
	public double getMaxFlowValue() {
		// TODO Auto-generated method stub
		double maxVal = Double.MIN_VALUE;
		for (SNEdge edge : this.SNEdges) {
			if (edge.getWeight() > maxVal) {
				maxVal = edge.getWeight();
			}
		}
		return maxVal;
	}

	@Override
	public double getMinFlowValue() {
		// TODO Auto-generated method stub
		double minVal = Double.MAX_VALUE;
		for (SNEdge edge : this.SNEdges) {
			if (edge.getWeight() < minVal) {
				minVal = edge.getWeight();
			}
		}
		return minVal;
	}

	public int getInDegree(SNNode snNode) {
		// TODO Auto-generated method stub
		return this.getInEdges(snNode).size();
	}

	public int getOutDegree(SNNode snNode) {
		// TODO Auto-generated method stub
		return this.getOutEdges(snNode).size();
	}

	public int getDegree(SNNode snNode) {
		// TODO Auto-generated method stub
		return this.getInDegree(snNode)+this.getOutDegree(snNode);
	}

	public double getInWeightDegree(SNNode snNode) {
		// TODO Auto-generated method stub
		Collection<SNEdge> inEdges = this.getInEdges(snNode);
		double weightDegree = 0.0;
		for (SNEdge edge : inEdges) {
			weightDegree += edge.getWeight();
		}
		return weightDegree;
	}

	public double getOutWeightDegree(SNNode snNode) {
		// TODO Auto-generated method stub
		Collection<SNEdge> outEdges = this.getOutEdges(snNode);
		double weightDegree = 0.0;
		for (SNEdge edge : outEdges) {
			weightDegree += edge.getWeight();
		}
		return weightDegree;
	}

	public double getWeightDegree(SNNode snNode) {
		// TODO Auto-generated method stub
		return this.getInWeightDegree(snNode)+this.getOutWeightDegree(snNode);
	}
	
	@Override
	public JSONObject buildJson() {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		JSONArray nodes = null;
		Map<SNNode, Integer> nodeIdx = new HashMap<SNNode, Integer>();
		int idx = 0;
		if (this.SNNodes != null && !this.SNNodes.isEmpty()) {
			nodes = (nodes == null) ? new JSONArray() : nodes;
			for (SNNode node : this.SNNodes) {
				JSONObject tjson = new JSONObject();
				String[] lbls = node.getLabel().split("\\+");
				String lbl = (lbls[0] == null) ? "" : lbls[0];
				tjson.element("label", lbl);
				tjson.element("detail", node.getLabel());
				nodes.add(tjson);
				nodeIdx.put(node, idx);
				idx++;
			}
		}
		json.element("nodes", nodes);
		if (this.SNEdges != null && !this.SNEdges.isEmpty()) {
			JSONArray links = new JSONArray();
			for (SNEdge edge : this.SNEdges) {
				JSONObject arc_json = new JSONObject();
				arc_json.element("source", nodeIdx.get(edge.getSource()));
				arc_json.element("target", nodeIdx.get(edge.getTarget()));
				String str = edge.getLabel();
				arc_json.element("detail", str.substring(str.indexOf(":") + 1, str.length()));
				links.add(arc_json);
			}
			json.element("links", links);
		}
		System.out.println("json:"+json);
		return json;
	}

}
