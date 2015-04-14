package org.pmf.util;

import java.io.Serializable;
import java.util.UUID;

public class NodeID implements Comparable<NodeID>, Serializable {

	private static final long serialVersionUID = -3003222606661967522L;
	
	private final UUID id = UUID.randomUUID();

	public int compareTo(NodeID node) {
		return id.compareTo(node.id);
	}

	public String toString() {
		return id.toString();
	}

	public boolean equals(Object o) {
		if (!(o instanceof NodeID)) {
			return false;
		}
		NodeID nodeID = (NodeID) o;
		return nodeID.id.equals(id);
	}

	public int hashCode() {
		return id.hashCode();
	}

}