package org.pmf.main;

import java.util.HashSet;
import java.util.Set;

import org.pmf.alphaminer.AlphaMiner;
import org.pmf.log.logabstraction.*;

public class MultiAlphaMiner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Set<String> log = new HashSet<String>();
		log.add("ABCD");
		log.add("ACBD");
		log.add("AED");
//		LogRelations logRelations = new AlphaMinerLogRelationImpl(log);
//		printAllRelations(logRelations);
		AlphaMiner alpha = new AlphaMiner();
		try {
			alpha.doMining(log);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void printAllRelations(LogRelations relations) {
		if (relations != null) {
			System.out.println("logs: "+relations.getLog());
			System.out.println("directFollowRelations: "+relations.directFollowRelations());
			System.out.println("causalRelations: "+relations.causalRelations());
			System.out.println("parallelRelations: "+relations.parallelRelations());
			System.out.println("lengthOneLoops: "+relations.lengthOneLoops());
			System.out.println("lengthTwoLoops: "+relations.lengthTwoLoops());
			System.out.println("startTraceInfo: "+relations.startTraceInfo());
			System.out.println("endTraceInfo: "+relations.endTraceInfo());
		}
	}

}
