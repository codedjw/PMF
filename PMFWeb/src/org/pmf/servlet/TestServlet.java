package org.pmf.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.pmf.graph.petrinet.Petrinet;
import org.pmf.graph.petrinet.Place;
import org.pmf.log.logabstraction.AlphaMinerLogRelationImpl;
import org.pmf.log.logabstraction.LogRelations;
import org.pmf.tools.alphaminer.AlphaMiner;

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
		Set<String> log = new HashSet<String>();
		log.add("ABCD");
		log.add("ACBD");
		log.add("ABCEFBCD");
		log.add("ABCEFCBD");
		log.add("ACBEFBCD");
		log.add("ACBEFBCEFCBD");
//		log.add("ABC");
//		log.add("ABBC");
//		log.add("ABBBC");
		AlphaMiner alpha = new AlphaMiner();
		Petrinet net;
		try {
			net = alpha.doMining(log);
			JSONObject json = new JSONObject();
			json.element("status", "OK");
			json.element("result", net.buildJson());
			response.setContentType("application/json; charset=utf-8");		
			PrintWriter out = response.getWriter();
			out.print(json);
//			this.printNet(net);
//			System.out.println(net.buildJson());
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
