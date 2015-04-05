package org.pmf.util.search;

import java.util.Collection;
import java.util.Stack;

public class DepthFirstExpandCollection<N> implements ExpandCollection<N> {
	
	protected Stack<N> stack = new Stack<N>();

	@Override
	public N pop() {
		// TODO Auto-generated method stub
		return stack.pop();
	}

	@Override
	public void add(Collection<? extends N> newElements) {
		// TODO Auto-generated method stub
		stack.addAll(newElements);

	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return stack.isEmpty();
	}

}
