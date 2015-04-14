package org.pmf.graph.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.pmf.graph.DirectedGraph;
import org.pmf.graph.DirectedGraphEdge;
import org.pmf.graph.DirectedGraphNode;
import org.pmf.graph.DirectedGraphTraverseAction;

public abstract class AbstractDirectedGraph<N extends DirectedGraphNode, E extends DirectedGraphEdge<? extends N, ? extends N>> extends AbstractGraph
	implements DirectedGraph<N, E> {
	
	private final Map<DirectedGraphNode, Collection<E>> inEdgeMap = new LinkedHashMap<DirectedGraphNode, Collection<E>>();;
	private final Map<DirectedGraphNode, Collection<E>> outEdgeMap = new LinkedHashMap<DirectedGraphNode, Collection<E>>();;

	public AbstractDirectedGraph() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	public abstract void removeEdge(DirectedGraphEdge edge);
	
	protected void removeSurroundingEdges(N node) {
		for (E edge : getInEdges(node)) {
			removeEdge(edge);
		}
		for (E edge : getOutEdges(node)) {
			removeEdge(edge);
		}
	}
	
	protected boolean checkAddEdge(N source, N target) {
		Collection<N> nodes = this.getNodes();
		if (!nodes.contains(source) || !nodes.contains(target)) {
			return false;
		}
		return true;
	}
	
	public Collection<E> getInEdges(DirectedGraphNode node) {
		Collection<E> col = inEdgeMap.get(node);
		if (col == null) {
			return Collections.emptyList();
		} else {
			return new ArrayList<E>(col);
		}
	}

	public Collection<E> getOutEdges(DirectedGraphNode node) {
		Collection<E> col = outEdgeMap.get(node);
		if (col == null) {
			return Collections.emptyList();
		} else {
			return new ArrayList<E>(col);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void graphElementAdded(Object element) {
		if (element instanceof DirectedGraphNode) {
			DirectedGraphNode node = (DirectedGraphNode) element;
			synchronized (inEdgeMap) {
				inEdgeMap.put(node, new LinkedHashSet<E>());
			}
			synchronized (outEdgeMap) {
				outEdgeMap.put(node, new LinkedHashSet<E>());
			}
		}
		if (element instanceof DirectedGraphEdge<?, ?>) {
			E edge = (E) (element);
			synchronized (inEdgeMap) {
				Collection<E> collection = inEdgeMap.get(edge.getTarget());
				collection.add(edge);
			}
			synchronized (outEdgeMap) {
				Collection<E> collection = outEdgeMap.get(edge.getSource());
				collection.add(edge);
			}
		}
		super.graphElementAdded(element);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void graphElementRemoved(Object element) {
		if (element instanceof DirectedGraphNode) {
			DirectedGraphNode node = (DirectedGraphNode) element;
			synchronized (inEdgeMap) {
				inEdgeMap.remove(node);
			}
			synchronized (outEdgeMap) {
				outEdgeMap.remove(node);
			}
		}
		if (element instanceof DirectedGraphEdge<?, ?>) {
			E edge = (E) (element);
			synchronized (inEdgeMap) {
				Collection<E> collection = inEdgeMap.get(edge.getTarget());
				collection.remove(element);
			}
			synchronized (outEdgeMap) {
				Collection<E> collection = outEdgeMap.get(edge.getSource());
				collection.remove(element);
			}
		}
		super.graphElementRemoved(element);
	}
	
	@Override
	public void graphElementChanged(Object element) {
		super.graphElementChanged(element);
	}
	
	public int compareTo(DirectedGraph<N, E> o) {
		if (!(o instanceof AbstractDirectedGraph<?, ?>)) {
			return getLabel().compareTo(o.getLabel());
		}
		AbstractDirectedGraph<?, ?> graph = (AbstractDirectedGraph<?, ?>) o;
		return id.compareTo(graph.id);
	}
	
	public void BFSTOSVG(DirectedGraphNode startNode) {
		if (startNode != null && this.getNodes() != null && !this.getNodes().isEmpty()) {
			int[] parents = new int[this.getNodes().size()];
			for (int i=0; i<parents.length; i++) {
				parents[i] = -1;
			}
			DirectedGraphNode[] nodes = new DirectedGraphNode[this.getNodes().size()];
			Map<DirectedGraphNode, Integer> nodesIdx = new HashMap<DirectedGraphNode, Integer>();
			// fill nodes
			int idx = 1;
			for (DirectedGraphNode node : this.getNodes()) {
				if (startNode.equals(node)) {
					nodes[0] = node;
					nodesIdx.put(node, 0);
				} else {
					nodes[idx] = node;
					nodesIdx.put(node, idx);
					idx++;
				}
			}
			for (int i=0; i<nodes.length; i++) {
				if (parents[i] == -1) {
					this.bfsToSvg(nodes[i], parents, nodesIdx);
					System.out.println();
				}
			}
		}
	}
	
	private void bfsToSvg(DirectedGraphNode node, int[] parents, Map<DirectedGraphNode, Integer> nodesIdx) {
		Queue<DirectedGraphNode> queue = new LinkedList<DirectedGraphNode>();
		queue.offer(node);
		parents[nodesIdx.get(node)] = nodesIdx.get(node);
		while (!queue.isEmpty()) {
			DirectedGraphNode source = queue.poll();
			Collection<E> outEdges = this.getOutEdges(source);
			if (outEdges != null && !outEdges.isEmpty()) {
				for (E edge : outEdges) {
					DirectedGraphNode target = edge.getTarget();
					if (parents[nodesIdx.get(target)] == -1) {
						queue.offer(target);
						parents[nodesIdx.get(target)] = parents[nodesIdx.get(source)]+1;
					}
				}
			}
		}
	}
	
	@Override
	public void BFS(DirectedGraphTraverseAction action, DirectedGraphNode startNode) {
		if (startNode != null && this.getNodes() != null && !this.getNodes().isEmpty() && this.getNodes().contains(startNode)) {
			boolean[] visited = new boolean[this.getNodes().size()];
			DirectedGraphNode[] nodes = new DirectedGraphNode[this.getNodes().size()];
			Map<DirectedGraphNode, Integer> nodesIdx = new HashMap<DirectedGraphNode, Integer>();
			// fill nodes
			int idx = 1;
			for (DirectedGraphNode node : this.getNodes()) {
				if (startNode.equals(node)) {
					nodes[0] = node;
					nodesIdx.put(node, 0);
				} else {
					nodes[idx] = node;
					nodesIdx.put(node, idx);
					idx++;
				}
			}
			for (int i=0; i<nodes.length; i++) {
				if (!visited[i]) {
					this.bfs(nodes[i], action, visited, nodesIdx);
					System.out.println();
				}
			}
		}
	}
	
	private void bfs(DirectedGraphNode node, DirectedGraphTraverseAction action, boolean[] visited, Map<DirectedGraphNode, Integer> nodesIdx) {
//		StringBuffer str = new StringBuffer("");
		Queue<DirectedGraphNode> queue = new LinkedList<DirectedGraphNode>();
		queue.offer(node);
		visited[nodesIdx.get(node)] = true;
		action.processNode(node);
//		action.processNodeWithStringBuffer(node, str);
		while (!queue.isEmpty()) {
			DirectedGraphNode source = queue.poll();
			Collection<E> outEdges = this.getOutEdges(source);
			if (outEdges != null && !outEdges.isEmpty()) {
				for (E edge : outEdges) {
					DirectedGraphNode target = edge.getTarget();
					if (!visited[nodesIdx.get(target)]) {
						queue.offer(target);
						visited[nodesIdx.get(target)] = true;
						action.processNode(target);
					}
				}
			}
		}
	}
	
	@Override
	public void DFS(DirectedGraphTraverseAction action, DirectedGraphNode startNode) {
		if (startNode != null && this.getNodes() != null && !this.getNodes().isEmpty() && this.getNodes().contains(startNode)) {
			boolean[] visited = new boolean[this.getNodes().size()];
			DirectedGraphNode[] nodes = new DirectedGraphNode[this.getNodes().size()];
			Map<DirectedGraphNode, Integer> nodesIdx = new HashMap<DirectedGraphNode, Integer>();
			// fill nodes
			int idx = 1;
			for (DirectedGraphNode node : this.getNodes()) {
				if (startNode.equals(node)) {
					nodes[0] = node;
					nodesIdx.put(node, 0);
				} else {
					nodes[idx] = node;
					nodesIdx.put(node, idx);
					idx++;
				}
			}
			for (int i=0; i<nodes.length; i++) {
				if (!visited[i]) {
					this.dfs(nodes[i], action, visited, nodesIdx);
					System.out.println();
				}
			}
		}
	}
	
	private void dfs(DirectedGraphNode node, DirectedGraphTraverseAction action, boolean[] visited, Map<DirectedGraphNode, Integer> nodesIdx) {
		visited[nodesIdx.get(node)] = true;
		action.processNode(node);
//		action.processNodeWithStringBuffer(node, str);
		Collection<E> outEdges = this.getOutEdges(node);
		if (outEdges != null && !outEdges.isEmpty()) {
			for (E edge : outEdges) {
				DirectedGraphNode target = edge.getTarget();
				if (!visited[nodesIdx.get(target)]) {
					dfs(target, action, visited, nodesIdx);
				}
			}
		}
	}

}
