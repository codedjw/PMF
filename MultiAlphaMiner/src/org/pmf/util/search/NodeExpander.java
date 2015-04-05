package org.pmf.util.search;

import java.util.Collection;

public interface NodeExpander<N> {
	
	public Collection<N> expandNode(N toExpand, Collection<N> unmodifiableResultCollection);
	
	public void processLeaf(N leaf, Collection<N> resultCollection);

}
