package org.pmf.graph.petrinet;

import net.sf.json.JSONObject;

import org.pmf.graph.petrinet.impl.PetrinetNode;

public interface Petrinet extends PetrinetGraph {
	
	public String toString();
	
	public void printPetrinet(PetrinetNode startNode);
	
	public JSONObject buildJson();

}
