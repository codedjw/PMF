package org.pmf.graph.impl;

public abstract class AbstractGraphEdge<S, T> extends AbstractGraphElement  implements Comparable<AbstractGraphEdge<S, T>> {
	
	protected S source;
	protected T target;
	protected int hash;
	
	public AbstractGraphEdge(S source, T target) {
		super();
		this.source = source;
		this.target = target;
		this.hash = source.hashCode() + 37 * target.hashCode();
	}
	
	public int hashCode() {
		return this.hash;
	}
	
	public boolean equals(Object o) {
		if (!(this.getClass().equals(o.getClass()))) {
			return false;
		}
		AbstractGraphEdge<?, ?> edge = (AbstractGraphEdge<?, ?>) o;

		return edge.source.equals(source) && edge.target.equals(target);
	}

	/**
	 * @return the source
	 */
	public S getSource() {
		return source;
	}

	/**
	 * @return the target
	 */
	public T getTarget() {
		return target;
	}
	
}
