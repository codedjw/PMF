package org.pmf.servlet;

import java.io.IOException;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pmf.eao.InvokeEao;
import org.pmf.eao.PluginEao;
import org.pmf.eao.UserEao;
import org.pmf.entity.Invoke;
import org.pmf.entity.Plugin;
import org.pmf.entity.User;

/**
 * Servlet implementation class TestDBServlet
 */
@WebServlet("/TestDBServlet")
public class TestDBServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private InvokeEao invokeEao;
	
	@EJB
	private PluginEao pluginEao;
	
	@EJB
	private UserEao userEao;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestDBServlet() {
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
		// TODO Auto-generated method stub
		User user0 = new User("test0", "test0", User.Role.USER);
		User user1 = new User("test1", "test1", User.Role.USER);
		
		userEao.save(user0);
		userEao.save(user1);
		
		Plugin plugin0 = new Plugin("p0","p0", "p0", Plugin.Category.CFD, "p0", "p0", "p0", "p0", 1);
		Plugin plugin1 = new Plugin("p1","p1", "p1", Plugin.Category.CFD, "p1", "p1", "p1", "p1", 1);
		
		pluginEao.save(plugin0);
		pluginEao.save(plugin1);
		
		plugin0.getUsers().add(user0);
		plugin0.getUsers().add(user1);
		
		plugin0.addInvoke(new Invoke(new Date()));
		plugin0.addInvoke(new Invoke(new Date()));
		
		// 应在最后执行，不应该执行多次，会出现commit，roll back错误
		pluginEao.update(plugin0);
	}

}
