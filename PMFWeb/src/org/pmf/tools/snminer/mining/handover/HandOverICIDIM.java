package org.pmf.tools.snminer.mining.handover;

import org.deckfour.xes.extension.std.XExtendedEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.pmf.tools.snminer.mining.operation.BasicOperation;
import org.pmf.tools.snminer.mining.operation.UtilOperation;

import cern.colt.matrix.tdouble.DoubleFactory2D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;

// The second Matrix
public class HandOverICIDIM extends BasicOperation {

	public HandOverICIDIM(XLog log) {
		super(log);
		// TODO Auto-generated constructor stub
	}

	public DoubleMatrix2D calculation(double beta, int depth) {
		int numOfPerformers = this.performersList.size();
		DoubleMatrix2D D = DoubleFactory2D.sparse.make(numOfPerformers, numOfPerformers, 0);
		double normal = 0; // 归一系数
		for (XTrace trace : this.log) {
			// calculate k
			int maxN  = ((trace.size() < depth) ? trace.size() : (depth+1));
			if (maxN < 2) {
				maxN = 2;
			}
			for (int n=1; n<maxN; n++) {
				normal += Math.pow(beta, n-1);
				DoubleMatrix2D m = DoubleFactory2D.sparse.make(numOfPerformers, numOfPerformers, 0);
				for (int i=0; i<trace.size()-n; i++) {
					XExtendedEvent x1 = XExtendedEvent.wrap(trace.get(i));
					XExtendedEvent x2 = XExtendedEvent.wrap(trace.get(i+n));
					if (x1.getResource() == null || x2.getResource() == null) {
						continue;
					}
					int row = this.performersList.indexOf(x1.getResource());
					int col = this.performersList.indexOf(x2.getResource());
					if ((row != -1) && (col != -1)) {
						m.set(row, col, 1.0);
					}
				}
				for (int i=0; i<numOfPerformers; i++) {
					for (int j=0; j<numOfPerformers; j++) {
						D.set(i, j, D.get(i, j)+m.get(i, j)*Math.pow(beta, n-1));
					}
				}
			}
		}
		return UtilOperation.normalize(D, normal);
	}
}
