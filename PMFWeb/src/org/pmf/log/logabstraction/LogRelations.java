package org.pmf.log.logabstraction;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pmf.util.Pair;

public interface LogRelations {
	public Set<String> getLog();
	
	public Set<String> getTransitions();
	
	public List<String> getTrans();
	
	public Pair<List<String>, int[][]> directlyFollowMatrix();
	
	public Pair<List<String>, int[][]> causalMatrix();
	
	public Map<Pair<String, String>, Integer> causalRelations();
	
	public Map<Pair<String, String>, Integer> parallelRelations();
	
	public Map<Pair<String, String>, Integer> directFollowRelations();
	
	public Map<String, Integer> startTraceInfo();
	
	public Map<String, Integer> endTraceInfo();
	
	public Map<String, Integer> lengthOneLoops();
	
	public Map<Pair<String, String>, Integer> lengthTwoLoops();
}
