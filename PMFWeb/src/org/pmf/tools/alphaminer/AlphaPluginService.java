package org.pmf.tools.alphaminer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XLog;
import org.pmf.graph.petrinet.Petrinet;
import org.pmf.plugin.service.PluginService;

public class AlphaPluginService implements PluginService {

	public JSONObject doPluginService(XLog log, Map<String, String> params) {
		// TODO Auto-generated method stub
		AlphaMiner alpha = new AlphaMiner();
		Petrinet net;
		try {
			net = alpha.doMining(log);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSONObject json = new JSONObject();
			json.element("status", "ERROR");
			return json;
		}
		JSONObject json = new JSONObject();
		json.element("status", "OK");
		json.element("result", net.buildJson());
		// get EventClasses Info
		XLogInfo logInfo = XLogInfoFactory.createLogInfo(log);
		XEventClasses classes = logInfo.getEventClasses();
		List<XEventClass> eventClasses = new ArrayList<XEventClass>(classes.size());
		eventClasses.addAll(classes.getClasses());
		JSONArray logarray = new JSONArray();
		for (XEventClass ec : eventClasses) {
			JSONObject logitem = new JSONObject();
			logitem.element("EventClass", ec.toString());
			logitem.element("Frequency", ec.size());
			logarray.element(logitem);
		}
		json.element("log", logarray);	
		return json;
	}

}
