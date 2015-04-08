package org.pmf.tools.alphaminer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ExecutionException;

import org.pmf.graph.petrinet.*;
import org.pmf.log.logabstraction.AlphaMinerLogRelationImpl;
import org.pmf.log.logabstraction.LogRelations;
import org.pmf.util.Pair;
import org.pmf.util.search.*;

public class AlphaMiner implements NodeExpander<Tuple> {
	
	private LogRelations relations;
	private List<String> trans;
	
	public Petrinet doMining(Set<String> log) throws InterruptedException, ExecutionException {
		if (log != null && !log.isEmpty()) {
			LogRelations logRelations = new AlphaMinerLogRelationImpl(log);
			if (logRelations != null) {
				return doMiningPrivateWithRelation(logRelations);
			}
		}
		return null;
	}
	
	public Petrinet doMiningWithRelation(LogRelations logRelations) throws InterruptedException, ExecutionException {
		if (logRelations != null) {
			return doMiningPrivateWithRelation(logRelations);
		}
		return null;
	}
	
	private Petrinet doMiningPrivateWithRelation(LogRelations logRelations) throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		this.relations = logRelations;
		this.trans = logRelations.getTrans();
//		this.trans.removeAll(this.relations.lengthOneLoops().keySet());
		
		Stack<Tuple> stack = new Stack<Tuple>();
		for (Pair<String, String> causal : this.relations.causalRelations().keySet()) {
			String from = causal.getFirst();
			String to = causal.getSecond();
			if (this.trans.contains(from) && this.trans.contains(to)) {
				Tuple tuple = new Tuple();
				tuple.leftPart.add(from);
				tuple.rightPart.add(to);
				tuple.maxLeftIndex = this.trans.indexOf(from);
				tuple.maxRightIndex = this.trans.indexOf(to);
				stack.push(tuple);
			}
		}
		
		// Expand the tuples
		List<Tuple> result = new ArrayList<Tuple>();
		MultiThreadedSearcher<Tuple> searcher = new MultiThreadedSearcher<Tuple>(this,
				MultiThreadedSearcher.BREADTHFIRST);
		searcher.addInitialNodes(stack);
		// not checked
		searcher.startSearch(result);
		searcher.close();
		
		System.out.println("tuple2place: "+result);
		
		// new Petrinet
		Petrinet net = PetrinetFactory.newPetrinet("Test for Alpha");
		
		// add transitions
		Map<String, Transition> class2transition = new HashMap<String, Transition>();
		for (String t : this.trans) {
			Transition transition = net.addTransition(t);
			class2transition.put(t, transition);
		}
		
		// add places for each tuple
		Map<Tuple, Place> tuple2place = new HashMap<Tuple, Place>();
		int idx = 0;
		for (Tuple t : result) {
			Place place = net.addPlace("P"+idx/*+"("+t.toString()+")"*/);
			idx++;
			for (String source : t.leftPart) {
				Transition st = class2transition.get(source);
				Arc s2p = net.addArc(st, place);
			}
			for (String target : t.rightPart) {
				Transition tt = class2transition.get(target);
				Arc p2t = net.addArc(place, tt);
			}
			tuple2place.put(t, place);
		}
		
		// add initial and end place
		Place pstart = net.addPlace("Start");
		Set<String> starts = this.relations.startTraceInfo().keySet();
		if (starts != null && !starts.isEmpty()) {
			for (String sc : starts) {
				Transition st = class2transition.get(sc);
				Arc pstart2t = net.addArc(pstart, st);
			}
		}
		Place pend = net.addPlace("End");
		Set<String> ends = this.relations.endTraceInfo().keySet();
		if (ends != null && !ends.isEmpty()) {
			for (String ec : ends) {
				Transition et = class2transition.get(ec);
				Arc t2pend = net.addArc(et, pend);
			}
		}
		
		// connect 1-loops http://0agr.ru/wiki/index.php/Alpha_Algorithm
		// not check
		Set<String> oneLoops = this.relations.lengthOneLoops().keySet();
		if (oneLoops != null && !oneLoops.isEmpty()) {
			for (String oneLoop : oneLoops) {
				Tuple t = new Tuple(); // one loop place
				t.leftPart.add(oneLoop);
				t.rightPart.add(oneLoop);
				if (!result.contains(t)) {
					Place tp = net.addPlace("P"+idx/*"one loop with " + oneLoop*/);
					idx++;
					net.addArc(class2transition.get(oneLoop), tp);
					net.addArc(tp, class2transition.get(oneLoop));
				}
			}
		}
		
		return net;
	}

	@Override
	public Collection<Tuple> expandNode(Tuple toExpand,
			Collection<Tuple> unmodifiableResultCollection) {
		// TODO Auto-generated method stub
		Collection<Tuple> tuples = new HashSet<Tuple>();

		int startIndex = toExpand.maxLeftIndex + 1;
		for (int i = startIndex; i < this.trans.size(); i++) {

			String toAdd = this.trans.get(i);

			if (canExpandLeft(toExpand, toAdd)) {
				// Ok, it is safe to add toAdd
				// to the left part of the tuple
				Tuple newTuple = toExpand.clone();
				newTuple.leftPart.add(toAdd);
				newTuple.maxLeftIndex = i;
				tuples.add(newTuple);
			}
		}

		startIndex = toExpand.maxRightIndex + 1;
		for (int i = startIndex; i < this.trans.size(); i++) {

			String toAdd = this.trans.get(i);

			if (canExpandRight(toExpand, toAdd)) {
				// Ok, it is safe to add toAdd
				// to the right part of the tuple
				Tuple newTuple = toExpand.clone();
				newTuple.rightPart.add(toAdd);
				newTuple.maxRightIndex = i;
				tuples.add(newTuple);
			}

		}

		return tuples;
	}

	@Override
	public void processLeaf(Tuple toAdd, Collection<Tuple> resultCollection) {
		// TODO Auto-generated method stub
		synchronized (resultCollection) {
			Iterator<Tuple> it = resultCollection.iterator();
			boolean largerFound = false;
			while (!largerFound && it.hasNext()) {
				Tuple t = it.next();
				if (t.isSmallerThan(toAdd)) {
					it.remove();
					continue;
				}
				largerFound = toAdd.isSmallerThan(t);
			}
			if (!largerFound) {
				resultCollection.add(toAdd);
			}
		}
		
	}
	
	private boolean canExpandLeft(Tuple toExpand, String toAdd) {
		// Check if the event class in toAdd has a causal depencendy 
		// with all elements of the rightPart of the tuple.
		for (String right : toExpand.rightPart) {
			if (!hasCausalRelation(toAdd, right)) {
				return false;
			}
		}

		// Check if the event class in toAdd does not have a relation 
		// with any of the elements of the leftPart of the tuple.
		for (String left : toExpand.leftPart) {
			if (hasRelation(toAdd, left)) {
				return false;
			}
		}

		return true;
	}

	private boolean canExpandRight(Tuple toExpand, String toAdd) {
		// Check if the event class in toAdd has a causal depencendy 
		// from all elements of the leftPart of the tuple.
		for (String left : toExpand.leftPart) {
			if (!hasCausalRelation(left, toAdd)) {
				return false;
			}
		}

		// Check if the event class in toAdd does not have a relation 
		// with any of the elements of the rightPart of the tuple.
		for (String right : toExpand.rightPart) {
			if (hasRelation(right, toAdd)) {
				return false;
			}
		}

		return true;
	}

	private boolean hasRelation(String from, String to) {
		if (!from.equals(to)) {
			if (hasCausalRelation(from, to)) {
				return true;
			}
			if (hasCausalRelation(to, from)) {
				return true;
			}
		}
		if (relations.parallelRelations().containsKey(new Pair<String, String>(from, to))) {
			return true;
		}
		return false;

	}

	private boolean hasCausalRelation(String from, String to) {
		if (relations.causalRelations().containsKey(new Pair<String, String>(from, to))) {
			return true;
		}
		return false;

	}

}
