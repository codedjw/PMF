package org.pmf.plugin.service;

import java.util.Map;

import net.sf.json.JSONObject;

import org.deckfour.xes.model.XLog;

public interface PluginService {
	public JSONObject doPluginService(XLog log, Map<String, String> params);
}
