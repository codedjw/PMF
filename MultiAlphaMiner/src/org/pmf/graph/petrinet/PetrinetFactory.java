package org.pmf.graph.petrinet;

import org.pmf.graph.petrinet.impl.PetrinetImpl;

public class PetrinetFactory {
	
	private PetrinetFactory() {
		
	}
	
	public static Petrinet newPetrinet(String label) {
		return new PetrinetImpl(label);
	}

}
