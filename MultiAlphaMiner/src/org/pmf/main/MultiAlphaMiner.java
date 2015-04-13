package org.pmf.main;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.pmf.graph.petrinet.Arc;
import org.pmf.graph.petrinet.Petrinet;
import org.pmf.graph.petrinet.Place;
import org.pmf.graph.petrinet.Transition;
import org.pmf.graph.petrinet.impl.PetrinetNode;
import org.pmf.log.logabstraction.*;
import org.pmf.tools.alphaminer.AlphaMiner;

public class MultiAlphaMiner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Set<String> log = new HashSet<String>();
//		log.add("ABCD");
//		log.add("ACBD");
//		log.add("AED");
//		Set<String> log2 = new HashSet<String>();
//		log2.add("ACD");
//		log2.add("BCE");
		Set<String> log3 = new HashSet<String>();
		log3.add("ABCD");
		log3.add("ACBD");
		log3.add("ABCEFBCD");
		log3.add("ABCEFCBD");
		log3.add("ACBEFBCD");
		log3.add("ACBEFBCEFCBD");
//		Set<String> log4 = new HashSet<String>();
//		log4.add("ABC");
//		log4.add("ABBC");
//		log4.add("ABBBC");
		LogRelations logRelations = new AlphaMinerLogRelationImpl(log3);
		printAllRelations(logRelations);
		AlphaMiner alpha = new AlphaMiner();
		Petrinet net;
		try {
			net = alpha.doMiningWithRelation(logRelations);
//			net = alpha.doMining(log4);
//			System.out.println(net);
			performNet2SVG(net);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void printAllRelations(LogRelations relations) {
		if (relations != null) {
			System.out.println("logs: "+relations.getLog());
			System.out.println("transitions: "+relations.getTrans());
			System.out.println("directFollowRelations: "+relations.directFollowRelations());
			System.out.println("causalRelations: "+relations.causalRelations());
			System.out.println("parallelRelations: "+relations.parallelRelations());
			System.out.println("lengthOneLoops: "+relations.lengthOneLoops());
			System.out.println("lengthTwoLoops: "+relations.lengthTwoLoops());
			System.out.println("startTraceInfo: "+relations.startTraceInfo());
			System.out.println("endTraceInfo: "+relations.endTraceInfo());
		}
	}
	
	public static void printNet(Petrinet net) {
		if (net != null) {
			Collection<Place> places = net.getPlaces();
			Place start = null;
			for (Place place : places) {
				if (place.getLabel().equals("Start")) {
					start = place;
					break;
				}
			}
			if (start != null) {
				net.printPetrinet(start);
			}
		}
	}
	
	public static void performNet2SVG(Petrinet net) {
		if (net != null) {
			Collection<Place> places = net.getPlaces();
			Place start = null;
			for (Place place : places) {
				if (place.getLabel().equals("Start")) {
					start = place;
					break;
				}
			}
			if (start != null) {
				net.petrinet2SVG(start);
			}
		}
	}

}
