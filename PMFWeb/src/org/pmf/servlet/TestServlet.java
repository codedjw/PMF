package org.pmf.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XLog;
import org.pmf.graph.petrinet.Petrinet;
import org.pmf.graph.petrinet.Place;
import org.pmf.log.logabstraction.AlphaMinerLogRelationImpl;
import org.pmf.log.logabstraction.LogRelations;
import org.pmf.tools.alphaminer.AlphaMiner;
import org.pmf.util.AttributeMap;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.processRequest(request, response);
	}
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String logJsonStr = request.getParameter("logJson");
		System.out.println(logJsonStr);
		JSONObject logJson = JSONObject.fromObject(logJsonStr);
		int lid = logJson.getInt("logid");
		String logfile = "";
		switch (lid) {
		case 0:
			logfile = "L1.xes";
			break;
		case 1:
			logfile = "L2.xes";
			break;
		case 2:
			logfile = "L3.xes";
			break;
		case 3:
			logfile = "L4.xes";
			break;
		case 4:
			logfile = "L5.xes";
			break;
		case 5:
			logfile = "L6.xes";
			break;
		case 6:
			logfile = "L7.xes";
			break;
		case 7:
			logfile = "L8.xes";
			break;
		case 8:
			logfile = "running-two-oneloops-pmf.xes";
			break;
		default:
			logfile = "bigger-example.xes";
			break;
		}
		
		XesXmlParser parser =  new XesXmlParser();
		InputStream file;
		file = new FileInputStream(new File(this.getServletContext().getRealPath("/WEB-INF/upload") + "/" + logfile));
		List<XLog> logs;
		try {
			logs = parser.parse(file);
			if (logs != null && !logs.isEmpty()) {
				XLog log = logs.get(0);
				AlphaMiner alpha = new AlphaMiner();
				Petrinet net;
				try {
					net = alpha.doMining(log);
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
					System.out.println("Retrun JSON:"+json);
					response.setContentType("application/json; charset=utf-8");		
					PrintWriter out = response.getWriter();
					out.print(json);
//					this.printNet(net);
//					System.out.println(net.buildJson());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					JSONObject json = new JSONObject();
					json.element("status", "ERROR");
					response.setContentType("application/json; charset=utf-8");		
					PrintWriter out = response.getWriter();
					out.print(json);
				}
			} else {
				JSONObject json = new JSONObject();
				json.element("status", "ERROR");
				response.setContentType("application/json; charset=utf-8");		
				PrintWriter out = response.getWriter();
				out.print(json);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			JSONObject json = new JSONObject();
			json.element("status", "ERROR");
			response.setContentType("application/json; charset=utf-8");		
			PrintWriter out = response.getWriter();
			out.print(json);
		}
	}
	
//	protected void processRequestBack(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String logJsonStr = request.getParameter("logJson");
//		System.out.println(logJsonStr);
//		JSONObject logJson = JSONObject.fromObject(logJsonStr);
//		int lid = logJson.getInt("logid");
//		Set<String> log = new HashSet<String>();
//		Map<String, String> detailMap = new HashMap<String, String>();
//		switch (lid) {
//		case 0:
//			log.add("ABCD");
//			log.add("ACBD");
//			log.add("AED");
//			break;
//		case 1:
//			log.add("ABCEFBCD");
//			log.add("ABCEFCBD");
//			log.add("ACBEFBCD");
//			log.add("ACBEFBCEFCBD");
//			break;
//		case 2:
//			log.add("ABC");
//			log.add("ABBC");
//			log.add("ABBBC");
//			break;
//		case 3:
//			log.add("ABCDEFBDCEG");
//			log.add("ABDCEG");
//			log.add("ABCDEFBCDEFBDCEG");
//			break;
//		case 4:
//			log.add("ABEF");
//			log.add("ABECDBF");
//			log.add("ABCEDBF");
//			log.add("ABCDEBF");
//			log.add("AEBCDBF");
//			break;
//		case 5:
//			detailMap.put("A", "register request");
//			detailMap.put("B", "examine thoroughly");
//			detailMap.put("C", "examine casually");
//			detailMap.put("D", "check ticket");
//			detailMap.put("E", "decide");
//			detailMap.put("F", "reinitiate request");
//			detailMap.put("G", "pay compensation");
//			detailMap.put("H", "reject request");
//			log.add("ABDEH");
//			log.add("ADCEG");
//			log.add("ACDEFBDEG");
//			log.add("ADBEH");
//			log.add("ACDEFDCEFCDEH");
//			log.add("ACDEG");
//			break;
//		default:
//			log.add("ABCD");
//			log.add("ACBD");
//			log.add("AED");
//			break;
//		}
//		
//		AlphaMiner alpha = new AlphaMiner();
//		Petrinet net;
//		try {
//			net = alpha.doMining(log);
//			JSONObject json = new JSONObject();
//			json.element("status", "OK");
//			if (detailMap == null || detailMap.isEmpty()) {
//				json.element("result", net.buildJson());
//				JSONArray logarray = new JSONArray();
//				for (String l : log) {
//					logarray.add(l);
//					json.element("log", logarray);
//				}
//			} else {
//				JSONObject result = net.buildJson();
//				JSONArray nodes = result.getJSONArray("nodes");
//				for (int i=0; i<nodes.size(); i++) {
//					JSONObject node = nodes.getJSONObject(i);
//					if (node.getString("type") != null && AttributeMap.Type.PN_TRANSITION.toString().equals(node.getString("type"))) {
//						if (detailMap.containsKey(node.getString("label"))) {
//							node.element("detail", detailMap.get(node.getString("label")));
//						}
//					}
//				}
//				result.element("nodes", nodes);
//				System.out.println("new: "+ result);
//				json.element("result", result);
//				JSONArray logarray = new JSONArray();
//				for (String l : log) {
//					String trace = "";
//					for (int j=0; j<l.length(); j++) {
//						String label = "" + l.charAt(j);
//						String detail = (detailMap.get(label) == null) ? label : detailMap.get(label);
//						if (j != 0) {
//							trace += ", ";
//						}
//						trace += detail;
//					}
//					logarray.add(trace);
//					json.element("log", logarray);
//				}
//			}
//			response.setContentType("application/json; charset=utf-8");		
//			PrintWriter out = response.getWriter();
//			out.print(json);
////			this.printNet(net);
////			System.out.println(net.buildJson());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			JSONObject json = new JSONObject();
//			json.element("status", "ERROR");
//			response.setContentType("application/json; charset=utf-8");		
//			PrintWriter out = response.getWriter();
//			out.print(json);
//		}
//	}
	
	
	
	private void printNet(Petrinet net) {
		if (net != null) {
			Collection<Place> places = net.getPlaces();
			Place start = null;
			for (Place place : places) {
				if (place.getLabel().equals("Start")) {
					start = place;
					break;
				}
			}
			if (start != null) {
				net.printPetrinet(start);
			}
		}
	}

}
