package org.pmf.util.search;

import java.util.Collection;

public interface ExpandCollection<N> {
	
	public N pop();
	
	public void add(Collection<? extends N> newElements);
	
	public boolean isEmpty();

}
