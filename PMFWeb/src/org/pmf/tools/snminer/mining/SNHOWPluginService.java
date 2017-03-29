package org.pmf.tools.snminer.mining;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XLog;
import org.pmf.graph.socialnetwork.SocialNetwork;
import org.pmf.plugin.service.PluginService;

public class SNHOWPluginService implements PluginService {

	@Override
	public JSONObject doPluginService(XLog log, Map<String, String> params) {
		// TODO Auto-generated method stub
		int depth = 1;
		double beta = 0.5;
		boolean cc = false, cm = false;
		String betaStr = params.get("beta");
		String depthStr = params.get("depth");
		if (params.containsKey("cm") && params.get("cm").equals("on")) {
			cm = true;
		}
		if (params.containsKey("cc") && params.get("cc").equals("on")) {
			cc = true;
		}
		int type = -1;
		type = SNMiningOptions.HANDOVER_OF_WORK;
		if (cm) {
			type+=SNMiningOptions.CONSIDER_MULTIPLE_TRANSFERS;
		}
		if (cc) {
			type+=SNMiningOptions.CONSIDER_CAUSALITY;
		}
		beta = Double.parseDouble(betaStr);
		depth = Integer.parseInt(depthStr);
		SNHOWMiner snm = new SNHOWMiner();
		SocialNetwork sn = snm.doSNHOWMining(log, type, beta, depth);
		if (sn != null) {
			JSONObject json = new JSONObject();
			json.element("status", "OK");
			json.element("result", sn.buildJson());
			// get EventClasses Info
			XLogInfo logInfo = XLogInfoFactory.createLogInfo(log);
			XEventClasses classes = logInfo.getResourceClasses();
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
			System.out.println("Retrun JSON:"+json);
			return json;
		} else {
			JSONObject json = new JSONObject();
			json.element("status", "ERROR");
			return json;
		}
	}
}
