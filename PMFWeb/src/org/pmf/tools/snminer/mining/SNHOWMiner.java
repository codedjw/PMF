package org.pmf.tools.snminer.mining;

import org.deckfour.xes.model.XLog;
import org.pmf.graph.socialnetwork.SocialNetwork;
import org.pmf.tools.snminer.mining.operation.BasicOperation;
import org.pmf.tools.snminer.mining.operation.OperationFactory;
import org.pmf.tools.snminer.mining.operation.UtilOperation;

import cern.colt.matrix.tdouble.DoubleMatrix2D;

public class SNHOWMiner {
	
	private BasicOperation operation = null;

	public SocialNetwork doSNHOWMining(XLog log, int type, double beta, int depth) {
		this.operation = OperationFactory.getOperation(log, type);
		if (this.operation != null) {
			DoubleMatrix2D matrix = this.operation.calculation(beta, depth);
			return UtilOperation.generateSN(matrix, operation.getPerformersList());
		} else {
			return null;
		}
	}
	
}
