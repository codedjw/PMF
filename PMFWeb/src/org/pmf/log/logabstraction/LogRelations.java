package org.pmf.log.logabstraction;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pmf.util.Pair;
import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.model.XLog;

public interface LogRelations {
	public XLog getLog();
	
	public XEventClasses getTransitions();
	
	public List<XEventClass> getTrans();
	
	public Pair<List<XEventClass>, int[][]> directlyFollowMatrix();
	
	public Pair<List<XEventClass>, int[][]> causalMatrix();
	
	public Map<Pair<XEventClass, XEventClass>, Integer> causalRelations();
	
	public Map<Pair<XEventClass, XEventClass>, Integer> parallelRelations();
	
	public Map<Pair<XEventClass, XEventClass>, Integer> directFollowRelations();
	
	public Map<XEventClass, Integer> startTraceInfo();
	
	public Map<XEventClass, Integer> endTraceInfo();
	
	public Map<XEventClass, Integer> lengthOneLoops();
	
	public Map<Pair<XEventClass, XEventClass>, Integer> lengthTwoLoops();
}
