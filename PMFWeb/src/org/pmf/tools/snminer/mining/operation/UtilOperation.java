package org.pmf.tools.snminer.mining.operation;

import java.util.ArrayList;
import java.util.List;

import org.pmf.graph.socialnetwork.SocialNetwork;
import org.pmf.graph.socialnetwork.SocialNetworkFactory;

import cern.colt.function.tdouble.IntIntDoubleFunction;
import cern.colt.matrix.tdouble.DoubleFactory2D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;

public class UtilOperation {

	public static DoubleMatrix2D normalize(DoubleMatrix2D m, int n) {
		for (int r = 0; r < m.rows(); r++) {
			for (int c = 0; c < m.columns(); c++) {
				m.set(r, c, m.get(r, c) / n);
			}
		}
		return m;
	}

	public static DoubleMatrix2D normalize(DoubleMatrix2D m, double n) {
		for (int r = 0; r < m.rows(); r++) {
			for (int c = 0; c < m.columns(); c++) {
				m.set(r, c, m.get(r, c) / n);
			}
		}
		return m;
	}

	public static SocialNetwork generateSN(DoubleMatrix2D m, List<String> orgList) {
		SocialNetwork socialnetwork = SocialNetworkFactory.newSocialNetwork("Social network");

		if (orgList != null) {
			for (String str : orgList) {
				socialnetwork.addSNNode(str);
			}
			int k = 0;
			for (int i = 0; i < orgList.size() - 1; i++) {
				for (int j = i + 1; j < orgList.size(); j++) {
					if (m.get(i, j) > 0.0) {
						socialnetwork.addSNEdge(orgList.get(i), orgList.get(j), "e" + k + ":" + m.get(i, j));
						k++;
					}
					if (m.get(j, i) > 0.0) {
						socialnetwork.addSNEdge(orgList.get(j), orgList.get(i), "e" + k + ":" + m.get(j, i));
						k++;
					}
				}
			}
		}
		
		return socialnetwork;
	}

	public static DoubleMatrix2D euclidiandistance(DoubleMatrix2D m) {

		int row = m.rows();
		int column = m.columns();
		DoubleMatrix2D D = DoubleFactory2D.sparse.make(row, row, 0);

		for (int i = 0; i < row - 1; i++) {
			for (int j = i + 1; j < row; j++) {

				double temp = 0;

				for (int k = 0; k < column; k++) {
					temp += ((m.get(i, k) - m.get(j, k)) * (m.get(i, k) - m.get(j, k)));
				}

				temp = Math.sqrt(temp);

				D.set(i, j, temp);
				D.set(j, i, temp);
			}
		}

		return D;
	}

	public static DoubleMatrix2D hammingdistance(DoubleMatrix2D m) {

		int row = m.rows();
		int column = m.columns();
		DoubleMatrix2D D = DoubleFactory2D.sparse.make(row, row, 0);

		m.forEachNonZero(new IntIntDoubleFunction() {
			public double apply(int row, int column, double value) {
				value = 1;
				return value;
			}
		});

		for (int i = 0; i < row - 1; i++) {
			for (int j = i + 1; j < row; j++) {

				double temp = 0;

				for (int k = 0; k < column; k++) {
					if (m.get(i, k) != m.get(j, k)) {
						temp++;
					}
				}

				D.set(i, j, (column - temp) / column);
				D.set(j, i, (column - temp) / column);
			}
		}
		return D;
	}

	public static DoubleMatrix2D similaritycoefficient(DoubleMatrix2D m) {

		int row = m.rows();
		int column = m.columns();
		DoubleMatrix2D D = DoubleFactory2D.sparse.make(row, row, 0);

		m.forEachNonZero(new IntIntDoubleFunction() {
			public double apply(int row, int column, double value) {
				value = 1;
				return value;
			}
		});

		for (int i = 0; i < row - 1; i++) {
			for (int j = i + 1; j < row; j++) {

				double temp1 = 0;
				double temp2 = 0;

				for (int k = 0; k < column; k++) {
					if ((m.get(i, k) == 1) && (m.get(j, k) == 1)) {
						temp1++;
					} else {
						temp2++;
					}
				}

				if (temp2 != 0) {
					D.set(i, j, temp1 / temp2);
					D.set(j, i, temp1 / temp2);
				} else {
					D.set(i, j, Double.MAX_VALUE);
					D.set(j, i, Double.MAX_VALUE);
				}
			}
		}
		return D;
	}

	public static DoubleMatrix2D correlationcoefficient(DoubleMatrix2D m) {

		int row = m.rows();
		int column = m.columns();
		DoubleMatrix2D D = DoubleFactory2D.sparse.make(row, row, 0);

		for (int i = 0; i < row - 1; i++) {
			for (int j = i + 1; j < row; j++) {

				double temp1 = 0.0, temp2 = 0.0;
				double upper = 0.0, below1 = 0.0, below2 = 0.0;

				for (int k = 0; k < column; k++) {
					temp1 += m.get(i, k);
					temp2 += m.get(j, k);
				}

				temp1 = temp1 / column;
				temp2 = temp2 / column;

				for (int k = 0; k < column; k++) {
					upper += (m.get(i, k) - temp1) * (m.get(j, k) - temp2);
					below1 += (m.get(i, k) - temp1) * (m.get(i, k) - temp1);
					below2 += (m.get(j, k) - temp2) * (m.get(j, k) - temp2);
				}

				D.set(i, j, upper / Math.sqrt(below1 * below2));
				D.set(j, i, upper / Math.sqrt(below1 * below2));
			}
		}
		return D;
	}
}
