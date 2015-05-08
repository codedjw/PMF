package org.pmf.log.logabstraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pmf.util.Pair;
import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;

public abstract class AbstractLogRelations implements LogRelations {
	
	protected XLog log;
	protected XLogInfo logInfo;
	protected XEventClasses transitions;
	
	/* Matrices filled by this class */
	protected List<XEventClass> trans;
	protected int[][] absoluteDirectlyFollowMatrix;
    protected int[][] absoluteLengthTwoLoopMatrix;
    protected int[] starts;
    protected int[] ends;
    
    /* Matrices filled by the child classes */
    protected int[][] causalMatrix;
    protected final Map<Pair<XEventClass, XEventClass>, Set<XTrace>> dfrTraceMap = new HashMap<Pair<XEventClass, XEventClass>, Set<XTrace>>();
    
    /*private void parseLog() {
    	if (this.log != null && !this.log.isEmpty()) {
    		this.transitions = new HashSet<String>();
    		for (String trace : this.log) {
    			for (int i=0; i<trace.length(); i++) {
    				String t = "";
        			t += trace.charAt(i);
        			this.transitions.add(t);
    			}
    		}
    	}
    }
    
    private void setupTrans() {
    	if (this.transitions != null && !this.transitions.isEmpty()) {
    		this.trans = new ArrayList<String>();
    		for (String t : this.transitions) {
    			this.trans.add(t);
    		}
    	}
    }*/
    
    public AbstractLogRelations(XLog log) {
    	this.log = log;
    	this.logInfo = XLogInfoFactory.createLogInfo(log);
//    	this.parseLog();
//    	this.setupTrans();
    	this.transitions = this.logInfo.getEventClasses();
    	this.trans = new ArrayList<XEventClass>(this.transitions.size());
    	this.trans.addAll(this.transitions.getClasses());
    	
    	this.absoluteDirectlyFollowMatrix = new int[this.trans.size()][this.trans.size()];
    	this.absoluteLengthTwoLoopMatrix = new int[this.trans.size()][this.trans.size()];
    	
    	this.starts = new int[this.trans.size()];
    	this.ends = new int[this.trans.size()];
    	
    	this.causalMatrix = new int[this.trans.size()][this.trans.size()];
    	
    	this.calculateLogRelations();
    }
    
    protected abstract void calculateLogRelations();
    
    protected abstract void calculateMetrics();
    
    /**
     * Makes dfrPairs succession relations, as well as two-loop relations, i.e.
     * searches through the log for AB patterns and ABA patterns
     */
    protected void fillDirectSuccessionMatrices() {
        if (this.log != null && !this.log.isEmpty()) {
        	for (XTrace trace : this.log) {
        		if (trace != null && !trace.isEmpty()) {
        			this.starts[this.trans.indexOf(this.transitions.getClassOf(trace.get(0)))]++;
                    for (int i=0; i < trace.size()-1; i++) {
                        XEventClass from = this.transitions.getClassOf(trace.get(i));
                        XEventClass to = this.transitions.getClassOf(trace.get(i+1));
                        if (traceContainsLengthTwoConstruct(trace, i, from, to)) {
                            this.absoluteLengthTwoLoopMatrix[this.trans.indexOf(from)][this.trans.indexOf(to)]++;
                        }
                        storePair(this.trans.indexOf(from), this.trans.indexOf(to), trace);
                    }
                    this.ends[this.trans.indexOf(this.transitions.getClassOf(trace.get(trace.size()-1)))]++;
        		}
            }
        }
    }

	protected void storePair(int fromIndex, int toIndex, XTrace trace) {
		// TODO Auto-generated method stub
		this.absoluteDirectlyFollowMatrix[fromIndex][toIndex]++;

        Pair<XEventClass, XEventClass> pair = new Pair<XEventClass, XEventClass>(this.trans.get(fromIndex), this.trans.get(toIndex));
        Set<XTrace> traces = (this.dfrTraceMap.containsKey(pair) ? this.dfrTraceMap.get(pair) : new HashSet<XTrace>());
        traces.add(trace);
        this.dfrTraceMap.put(pair, traces);
	}

	protected boolean traceContainsLengthTwoConstruct(XTrace trace, int fromIndex,
			XEventClass from, XEventClass to) {
		// TODO Auto-generated method stub
		return fromIndex < trace.size()-2 && !(from.equals(to)) && from.equals(this.transitions.getClassOf(trace.get(fromIndex+2)));
	}

	@Override
	public XLog getLog() {
		// TODO Auto-generated method stub
		return this.log;
	}

	@Override
	public XEventClasses getTransitions() {
		// TODO Auto-generated method stub
		return this.transitions;
	}
	
	@Override
	public List<XEventClass> getTrans() {
		// TODO Auto-generated method stub
		return this.trans;
	}

	@Override
	public Pair<List<XEventClass>, int[][]> directlyFollowMatrix() {
		// TODO Auto-generated method stub
		return new Pair<List<XEventClass>, int[][]>(this.trans, this.absoluteDirectlyFollowMatrix);
	}

	@Override
	public Pair<List<XEventClass>, int[][]> causalMatrix() {
		// TODO Auto-generated method stub
		return new Pair<List<XEventClass>, int[][]>(this.trans, this.causalMatrix);
	}

	@Override
	public Map<Pair<XEventClass, XEventClass>, Integer> causalRelations() {
		// TODO Auto-generated method stub
		Map<Pair<XEventClass, XEventClass>, Integer> result = new HashMap<Pair<XEventClass, XEventClass>, Integer>();
		for (int i=0; i<this.causalMatrix.length; i++) {
			for (int j=0; j<this.causalMatrix[i].length; j++) {
				if (i != j && this.causalMatrix[i][j] > 0) {
					XEventClass from = this.trans.get(i);
					XEventClass to = this.trans.get(j);
					result.put(new Pair<XEventClass, XEventClass>(from, to), this.causalMatrix[i][j]); 
				}
			}
		}
		return result;
	}

	@Override
	public Map<Pair<XEventClass, XEventClass>, Integer> directFollowRelations() {
		// TODO Auto-generated method stub
		Map<Pair<XEventClass, XEventClass>, Integer> result = new HashMap<Pair<XEventClass, XEventClass>, Integer>();
		for (int i=0; i<this.absoluteDirectlyFollowMatrix.length; i++) {
			for (int j=0; j<this.absoluteDirectlyFollowMatrix[i].length; j++) {
				if (this.absoluteDirectlyFollowMatrix[i][j] > 0) {
					XEventClass from = this.trans.get(i);
					XEventClass to = this.trans.get(j);
					result.put(new Pair<XEventClass, XEventClass>(from, to), this.absoluteDirectlyFollowMatrix[i][j]); 
				}
			}
		}
		return result;
	}

	@Override
	public Map<XEventClass, Integer> startTraceInfo() {
		// TODO Auto-generated method stub
		Map<XEventClass, Integer> result = new HashMap<XEventClass, Integer>();
		for (int i=0; i<this.starts.length; i++) {
			if (this.starts[i] > 0) {
				XEventClass t = this.trans.get(i);
				result.put(t, this.starts[i]);
			}
		}
		return result;
	}

	@Override
	public Map<XEventClass, Integer> endTraceInfo() {
		// TODO Auto-generated method stub
		Map<XEventClass, Integer> result = new HashMap<XEventClass, Integer>();
		for (int i=0; i<this.ends.length; i++) {
			if (this.ends[i] > 0) {
				XEventClass t = this.trans.get(i);
				result.put(t, this.ends[i]);
			}
		}
		return result;
	}

	@Override
	public Map<XEventClass, Integer> lengthOneLoops() {
		// TODO Auto-generated method stub
		Map<XEventClass, Integer> result = new HashMap<XEventClass, Integer>();
		for (int i=0; i<this.trans.size(); i++) {
			if (this.absoluteDirectlyFollowMatrix[i][i] > 0) {
				result.put(this.trans.get(i), this.absoluteDirectlyFollowMatrix[i][i]);
			}
		}
		return result;
	}

	@Override
	public Map<Pair<XEventClass, XEventClass>, Integer> lengthTwoLoops() {
		// TODO Auto-generated method stub
		Map<Pair<XEventClass, XEventClass>, Integer> result = new HashMap<Pair<XEventClass, XEventClass>, Integer>();
		for (int i=0; i<this.absoluteLengthTwoLoopMatrix.length; i++) {
			for (int j=0; j<this.absoluteLengthTwoLoopMatrix[i].length; j++) {
				if (this.absoluteLengthTwoLoopMatrix[i][j] > 0) {
					XEventClass from = this.trans.get(i);
					XEventClass to = this.trans.get(j);
					result.put(new Pair<XEventClass, XEventClass>(from, to), this.absoluteLengthTwoLoopMatrix[i][j]); 
				}
			}
		}
		return result;
	}
    
}
