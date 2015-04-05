package org.pmf.util.search;

import java.util.Collection;
import java.util.concurrent.Callable;

public class MultiThreadedSearchWorker<N> implements Callable<N> {
	
	private MultiThreadedSearcher<N> owner;

	private Collection<N> resultCollection;

	private ExpandCollection<N> stack;
	
	public MultiThreadedSearchWorker(MultiThreadedSearcher<N> owner, Collection<N> resultCollection) {
		this.owner = owner;
		this.resultCollection = resultCollection;
		this.stack = owner.getStack();
	}

	@Override
	public N call() throws Exception {
		// TODO Auto-generated method stub
		while (true) {
			N toExpand = getNodeToExpand();
			if (toExpand == null) {
				synchronized (stack) {
					stack.notifyAll();
				}
				break;
			}
			Collection<N> expandFurther = owner.getExpander().expandNode(toExpand, resultCollection);
			processNewNodes(toExpand, expandFurther, resultCollection);
		}
		return null;
	}

	private void processNewNodes(N toExpand, Collection<N> expandFurther,
			Collection<N> resultCollection) {
		// TODO Auto-generated method stub
		synchronized (stack) {
			if (expandFurther != null && !expandFurther.isEmpty()) {
				stack.add(expandFurther);
			} else {
				synchronized (resultCollection) {
					owner.getExpander().processLeaf(toExpand, resultCollection);
				}
			}
			stack.notifyAll();
		}
		
	}

	private N getNodeToExpand() throws InterruptedException {
		// TODO Auto-generated method stub
		synchronized (stack) {
			while(true) {
				if (stack.isEmpty()) {
					if (owner.setWaiting(this, true)) {
						return null;
					} else {
						stack.wait();
						continue;
					}
				} else {
					owner.setWaiting(this, false);
					N toExpand = stack.pop();
					stack.notifyAll();
					return toExpand;
				}
			}
		}
	}

}
