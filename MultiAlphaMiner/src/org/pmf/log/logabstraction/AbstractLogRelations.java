package org.pmf.log.logabstraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pmf.util.Pair;

public abstract class AbstractLogRelations implements LogRelations {
	
	protected Set<String> log;
	protected Set<String> transitions;
	
	/* Matrices filled by this class */
	protected List<String> trans;
	protected int[][] absoluteDirectlyFollowMatrix;
    protected int[][] absoluteLengthTwoLoopMatrix;
    protected int[] starts;
    protected int[] ends;
    
    /* Matrices filled by the child classes */
    protected int[][] causalMatrix;
    protected final Map<Pair<String, String>, Set<String>> dfrTraceMap = new HashMap<Pair<String, String>, Set<String>>();
    
    private void parseLog() {
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
    }
    
    public AbstractLogRelations(Set<String> log) {
    	this.log = log;
    	this.parseLog();
    	this.setupTrans();
    	
    	this.absoluteDirectlyFollowMatrix = new int[this.trans.size()][this.trans.size()];
    	this.absoluteLengthTwoLoopMatrix = new int[this.trans.size()][this.trans.size()];
    	
    	this.starts = new int[this.trans.size()];
    	this.ends = new int[this.trans.size()];
    	
    	this.causalMatrix = new int[this.trans.size()][this.trans.size()];
    	
    	this.calculateLogRelations();
    }
    
    protected abstract void calculateLogRelations();
    
    protected abstract void calculateMatrices();
    
    /**
     * Makes dfrPairs succession relations, as well as two-loop relations, i.e.
     * searches through the log for AB patterns and ABA patterns
     */
    protected void fillDirectSuccessionMatrices() {
        if (this.log != null && !this.log.isEmpty()) {
        	for (String trace : this.log) {
        		this.starts[this.trans.indexOf(""+trace.charAt(0))]++;
                for (int i=0; i < trace.length()-1; i++) {
                    String from = "" + trace.charAt(i);
                    String to = "" + trace.charAt(i+1);
                    if (traceContainsLengthTwoConstruct(trace, i, from, to)) {
                        this.absoluteLengthTwoLoopMatrix[this.trans.indexOf(from)][this.trans.indexOf(to)]++;
                    }
                    storePair(this.trans.indexOf(from), this.trans.indexOf(to), trace);
                }
                this.ends[this.trans.indexOf(""+trace.charAt(trace.length()-1))]++;
            }
        }
    }

	protected void storePair(int fromIndex, int toIndex, String trace) {
		// TODO Auto-generated method stub
		this.absoluteDirectlyFollowMatrix[fromIndex][toIndex]++;

        Pair<String, String> pair = new Pair<String, String>(this.trans.get(fromIndex), this.trans.get(toIndex));
        Set<String> traces = (this.dfrTraceMap.containsKey(pair) ? this.dfrTraceMap.get(pair) : new HashSet<String>());
        traces.add(trace);
        this.dfrTraceMap.put(pair, traces);
	}

	protected boolean traceContainsLengthTwoConstruct(String trace, int fromIndex,
			String from, String to) {
		// TODO Auto-generated method stub
		return fromIndex < trace.length()-2 && !(from.equals(to)) && from.equals(""+trace.charAt(fromIndex+2));
	}

	@Override
	public Set<String> getLog() {
		// TODO Auto-generated method stub
		return this.log;
	}

	@Override
	public Set<String> getTransitions() {
		// TODO Auto-generated method stub
		return this.transitions;
	}
	
	@Override
	public List<String> getTrans() {
		// TODO Auto-generated method stub
		return this.trans;
	}

	@Override
	public Pair<List<String>, int[][]> directlyFollowMatrix() {
		// TODO Auto-generated method stub
		return new Pair<List<String>, int[][]>(this.trans, this.absoluteDirectlyFollowMatrix);
	}

	@Override
	public Pair<List<String>, int[][]> causalMatrix() {
		// TODO Auto-generated method stub
		return new Pair<List<String>, int[][]>(this.trans, this.causalMatrix);
	}

	@Override
	public Map<Pair<String, String>, Integer> causalRelations() {
		// TODO Auto-generated method stub
		Map<Pair<String, String>, Integer> result = new HashMap<Pair<String, String>, Integer>();
		for (int i=0; i<this.causalMatrix.length; i++) {
			for (int j=0; j<this.causalMatrix[i].length; j++) {
				if (i != j && this.causalMatrix[i][j] > 0) {
					String from = this.trans.get(i);
					String to = this.trans.get(j);
					result.put(new Pair<String, String>(from, to), this.causalMatrix[i][j]); 
				}
			}
		}
		return result;
	}

	@Override
	public Map<Pair<String, String>, Integer> directFollowRelations() {
		// TODO Auto-generated method stub
		Map<Pair<String, String>, Integer> result = new HashMap<Pair<String, String>, Integer>();
		for (int i=0; i<this.absoluteDirectlyFollowMatrix.length; i++) {
			for (int j=0; j<this.absoluteDirectlyFollowMatrix[i].length; j++) {
				if (this.absoluteDirectlyFollowMatrix[i][j] > 0) {
					String from = this.trans.get(i);
					String to = this.trans.get(j);
					result.put(new Pair<String, String>(from, to), this.absoluteDirectlyFollowMatrix[i][j]); 
				}
			}
		}
		return result;
	}

	@Override
	public Map<String, Integer> startTraceInfo() {
		// TODO Auto-generated method stub
		Map<String, Integer> result = new HashMap<String, Integer>();
		for (int i=0; i<this.starts.length; i++) {
			if (this.starts[i] > 0) {
				String t = this.trans.get(i);
				result.put(t, this.starts[i]);
			}
		}
		return result;
	}

	@Override
	public Map<String, Integer> endTraceInfo() {
		// TODO Auto-generated method stub
		Map<String, Integer> result = new HashMap<String, Integer>();
		for (int i=0; i<this.ends.length; i++) {
			if (this.ends[i] > 0) {
				String t = this.trans.get(i);
				result.put(t, this.ends[i]);
			}
		}
		return result;
	}

	@Override
	public Map<String, Integer> lengthOneLoops() {
		// TODO Auto-generated method stub
		Map<String, Integer> result = new HashMap<String, Integer>();
		for (int i=0; i<this.trans.size(); i++) {
			if (this.absoluteDirectlyFollowMatrix[i][i] > 0) {
				result.put(this.trans.get(i), this.absoluteDirectlyFollowMatrix[i][i]);
			}
		}
		return result;
	}

	@Override
	public Map<Pair<String, String>, Integer> lengthTwoLoops() {
		// TODO Auto-generated method stub
		Map<Pair<String, String>, Integer> result = new HashMap<Pair<String, String>, Integer>();
		for (int i=0; i<this.absoluteLengthTwoLoopMatrix.length; i++) {
			for (int j=0; j<this.absoluteLengthTwoLoopMatrix[i].length; j++) {
				if (this.absoluteLengthTwoLoopMatrix[i][j] > 0) {
					String from = this.trans.get(i);
					String to = this.trans.get(j);
					result.put(new Pair<String, String>(from, to), this.absoluteLengthTwoLoopMatrix[i][j]); 
				}
			}
		}
		return result;
	}
    
}
