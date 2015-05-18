package org.pmf.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
import org.pmf.graph.socialnetwork.SocialNetwork;
import org.pmf.tools.snminer.mining.SNHOWMiner;
import org.pmf.tools.snminer.mining.SNMiningOptions;
import org.pmf.tools.snminer.mining.handover.HandOverCCIDCM;
import org.pmf.tools.snminer.mining.operation.BasicOperation;

/**
 * Servlet implementation class SNMTestServlet
 */
@WebServlet("/SNMTestServlet")
public class SNMTestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SNMTestServlet() {
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
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String logJsonStr = request.getParameter("logJson");
		System.out.println(logJsonStr);
		JSONObject logJson = JSONObject.fromObject(logJsonStr);
		int lid = logJson.getInt("logid");
		String logfile = "";
		switch (lid) {
		case 0:
			logfile = "repairExample.xes";
			break;
		case 1:
			logfile = "reviewing.xes";
			break;
		default:
			logfile = "teleclaims.xes";
			break;
		}
		XesXmlParser parser =  new XesXmlParser();
		InputStream file;
		try {
			file = new FileInputStream(new File(this.getServletContext().getRealPath("/WEB-INF/upload") + "/" + logfile));
			List<XLog> logs = parser.parse(file);
			if (logs != null && !logs.isEmpty()) {
				XLog log = logs.get(0);
				double beta = 0.5;
				int depth = 5;
				int type = -1;
//				type = SNMiningOptions.HANDOVER_OF_WORK + SNMiningOptions.CONSIDER_CAUSALITY + SNMiningOptions.CONSIDER_MULTIPLE_TRANSFERS;
//				type = SNMiningOptions.HANDOVER_OF_WORK + SNMiningOptions.CONSIDER_CAUSALITY;
				type = SNMiningOptions.HANDOVER_OF_WORK + SNMiningOptions.CONSIDER_MULTIPLE_TRANSFERS;
//				type = SNMiningOptions.HANDOVER_OF_WORK;
				SNHOWMiner snm = new SNHOWMiner();
				SocialNetwork sn = snm.doSNHOWMining(log, type, beta, depth);
				if (sn != null) {
					JSONObject json = new JSONObject();
					json.element("status", "OK");
					json.element("result", sn.buildJson());
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
				} else {
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSONObject json = new JSONObject();
			json.element("status", "ERROR");
			response.setContentType("application/json; charset=utf-8");		
			PrintWriter out = response.getWriter();
			out.print(json);
		}
	}

}
