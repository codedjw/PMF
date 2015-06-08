package org.pmf.log.logabstraction;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.model.XLog;
import org.pmf.util.Pair;

public class AlphaMinerLogRelationImpl extends AbstractLogRelations implements
		LogRelations {
	
	protected int[][] parallelMatrix;

	public AlphaMinerLogRelationImpl(XLog log) {
		super(log);
	}

	@Override
	public Map<Pair<XEventClass, XEventClass>, Integer> parallelRelations() {
		// TODO Auto-generated method stub
		Map<Pair<XEventClass, XEventClass>, Integer> result = new HashMap<Pair<XEventClass, XEventClass>, Integer>();
        for (int i = 0; i < this.parallelMatrix.length; i++) {
            for (int j = 0; j < this.parallelMatrix[i].length; j++) {
                if (this.parallelMatrix[i][j] > 0) {
                	XEventClass from = this.trans.get(i);
                	XEventClass to = this.trans.get(j);
                    result.put(new Pair<XEventClass, XEventClass>(from, to), this.parallelMatrix[i][j]);
                }
            }
        }
        return result;
	}

	@Override
	protected void calculateLogRelations() {
		// TODO Auto-generated method stub
		this.parallelMatrix = new int[this.transitions.size()][this.transitions.size()];
		this.fillDirectSuccessionMatrices();
		this.calculateMatrices();
	}

	@Override
	protected void calculateMatrices() {
		// TODO Auto-generated method stub
		for (int fromIndex = 0; fromIndex < this.absoluteDirectlyFollowMatrix.length; fromIndex++) {
            for (int toIndex = 0; toIndex < this.absoluteDirectlyFollowMatrix.length; toIndex++) {
                if (this.absoluteDirectlyFollowMatrix[fromIndex][toIndex] > 0) {
                    if (this.absoluteDirectlyFollowMatrix[toIndex][fromIndex] > 0) {
                    	// from Theorem 3.4
                        if (this.absoluteLengthTwoLoopMatrix[fromIndex][toIndex] > 0 || this.absoluteLengthTwoLoopMatrix[toIndex][fromIndex] > 0) {
                            this.causalMatrix[fromIndex][toIndex] = 1;
                            this.causalMatrix[toIndex][fromIndex] = 1;
                        } else {
                            this.parallelMatrix[fromIndex][toIndex] = 1;
                            this.parallelMatrix[toIndex][fromIndex] = 1;
                        }
                    } else {
                        this.causalMatrix[fromIndex][toIndex] = 1;
                    }
                }
            }
        }
	}

}
