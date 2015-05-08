package org.pmf.tools.alphaminer;

import java.util.HashSet;
import java.util.Set;

import org.deckfour.xes.classification.XEventClass;

class Tuple {

	public Set<XEventClass> leftPart = new HashSet<XEventClass>();

	public Set<XEventClass> rightPart = new HashSet<XEventClass>();

	public int maxRightIndex = 0;
	public int maxLeftIndex = 0;

	public Tuple() {

	}

	public boolean isSmallerThan(Tuple tuple) {
		return tuple.leftPart.containsAll(leftPart) && tuple.rightPart.containsAll(rightPart);
	}

	public Tuple clone() {
		Tuple clone = new Tuple();
		clone.leftPart.addAll(leftPart);
		clone.rightPart.addAll(rightPart);
		clone.maxRightIndex = maxRightIndex;
		clone.maxLeftIndex = maxLeftIndex;
		return clone;
	}

	public int hashCode() {
		return leftPart.hashCode() + 37 * rightPart.hashCode() + maxRightIndex + maxLeftIndex;
	}

	public boolean equals(Object o) {
		if (o instanceof Tuple) {
			Tuple t = (Tuple) o;
			return (t.maxRightIndex == maxRightIndex) && (t.maxLeftIndex == maxLeftIndex)
					&& t.leftPart.equals(leftPart) && t.rightPart.equals(rightPart);
		}
		return false;
	}

	public String toString() {
		return "{" + leftPart.toString() + "} --> {" + rightPart.toString() + "}";
	}
}