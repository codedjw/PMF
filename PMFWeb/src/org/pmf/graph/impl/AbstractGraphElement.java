package org.pmf.graph.impl;

import org.pmf.util.AttributeMap;
import org.pmf.util.AttributeMapOwner;

public abstract class AbstractGraphElement implements AttributeMapOwner {
	
	private final AttributeMap map;

	public AbstractGraphElement() {
		map = new AttributeMap();
	}

	public String getLabel() {
		return map.get(AttributeMap.LABEL, "no label");
	}

	public String toString() {
		return getLabel();
	}

	@Override
	public AttributeMap getAttributeMap() {
		// TODO Auto-generated method stub
		return map;
	}

}
