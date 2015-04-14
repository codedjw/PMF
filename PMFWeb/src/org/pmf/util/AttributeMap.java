package org.pmf.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class AttributeMap {
	
	public enum Type {
		PN_TRANSITION, PN_ARC, PN_PLACE
	}
	
	private final static String PREFIX = "PMF_ATTR_";
	
	public final static String LABEL = PREFIX + "LABEL";
	
	public final static String TYPE = PREFIX + "TYPE";
	
	public final static String VISIBILITY = PREFIX + "VISIBILITY";
	
	private final Map<String, Object> mapping = new LinkedHashMap<String, Object>();
	
	public AttributeMap() {
	}

	public Object get(String key) {
		return mapping.get(key);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key, T defaultValue) {
		synchronized (mapping) {
			Object o = mapping.get(key);
			if (o != null) {
				return (T) o;
			}
			if (mapping.containsKey(key)) {
				return null;
			} else {
				return defaultValue;
			}
		}
	}

	public void clear() {
		mapping.clear();
	}

	public Set<String> keySet() {
		return mapping.keySet();
	}

	public boolean put(String key, Object value) {
		Object old;
		synchronized (mapping) {
			old = mapping.get(key);
			mapping.put(key, value);
		}
		if (value == old) {
			return false;
		}
		if ((value == null) || (old == null) || !value.equals(old)) {
			return true;
		}
		return false;
	}

	public void remove(String key) {
		synchronized (mapping) {
			mapping.remove(key);
		}
	}

	public boolean containsKey(String key) {
		return mapping.containsKey(key);
	}

}
