package org.pmf.util.search;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class MultiThreadedSearcher<N> {

	/**
	 * Constant representing a DEPTH-FIRST search.
	 */
	public final static int DEPTHFIRST = 0;

	/**
	 * Constant representing a BREADTH-FIRST search.
	 */
	public final static int BREADTHFIRST = 1;
	
	private NodeExpander<N> expander;
	private int threads;
	private Map<MultiThreadedSearchWorker<N>, Boolean> waiting;
	private ExpandCollection<N> stack;
	private ExecutorService executor;

	public MultiThreadedSearcher(int numberOfThreads, NodeExpander<N> expander, int searchType) {
		this.threads = numberOfThreads;
		this.executor = Executors.newFixedThreadPool(this.threads);
		this.expander = expander;
		if (searchType == DEPTHFIRST) {
			this.stack = new DepthFirstExpandCollection<N>();
		} else if (searchType == BREADTHFIRST) {
			this.stack = new BreadthFirstExpandCollection<N>();
		} else {
			throw new IllegalArgumentException("Wrong search type specified.");
		}
	}
	
	public MultiThreadedSearcher(NodeExpander<N> expander, int searchType) {
		this(Runtime.getRuntime().availableProcessors(), expander, searchType);
	}
	
	public MultiThreadedSearcher(int numberOfThreads, NodeExpander<N> expander, ExpandCollection<N> expandCollection) {
		this.threads = numberOfThreads;
		this.executor = Executors.newFixedThreadPool(this.threads);
		this.expander = expander;
		this.stack = expandCollection;
	}
	
	public MultiThreadedSearcher(NodeExpander<N> expander, ExpandCollection<N> expandCollection) {
		this(Runtime.getRuntime().availableProcessors(), expander, expandCollection);
	}
	
	public void addInitialNodes(Collection<N> initialNodes) {
		synchronized (stack) {
			stack.add(initialNodes);
		}
	}
	
	public void startSearch(final Collection<N> resultCollection) throws InterruptedException, ExecutionException {
		// Set the number of waiting threads to 0;
		waiting = new HashMap<MultiThreadedSearchWorker<N>, Boolean>();
		
		MultiThreadedSearchWorker<N> worker = null;
		
		FutureTask<N> future = null;
		
		// Syncrhonize on the object "waiting" to make sure that first all
		// workers are registered to this map, before any of them accesses it.
		synchronized (waiting) {
			for (int i = 0; i < threads; i++) {
				worker = new MultiThreadedSearchWorker<N>(this,
						resultCollection);
				waiting.put(worker, false);
			}
		}

		for (MultiThreadedSearchWorker<N> w : waiting.keySet()) {
			future = new FutureTask<N>(w);
			if (!this.executor.isShutdown()) {
				executor.submit(future);
			}
		}

		// Just synchronize on the last worker. It stops only when all workers
		// are finished.
		future.get();
	}
	
	NodeExpander<N> getExpander() {
		return expander;
	}

	ExpandCollection<N> getStack() {
		return stack;
	}

	boolean setWaiting(MultiThreadedSearchWorker<N> worker, Boolean state) {
		synchronized (waiting) {
			waiting.put(worker, state);
			return !waiting.containsValue(false);
		}
	}
	
	public void close() {
		this.executor.shutdown();
	}

}
