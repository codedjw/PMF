package org.pmf.graph.socialnetwork;

import java.util.Collection;

import net.sf.json.JSONObject;

import org.pmf.graph.DirectedGraph;

public interface SocialNetwork extends DirectedGraph<SNNode, SNEdge> {

	String getLabel();
	
	// edge
	boolean addSNEdge(SNNode from, SNNode to, Object identifier);

	boolean addSNEdge(Object fromID, Object toID, Object identifier);

	Object removeSNEdge(SNNode from, SNNode to, Object identifier);

	Collection<SNEdge> getSNEdges();

	double getMaxFlowValue();

	double getMinFlowValue();

	// node
	boolean addSNNode(Object identifier);

	Object removeSNNode(Object identifier);

	Collection<SNNode> getSNNodes();
	
	public JSONObject buildJson();

}
