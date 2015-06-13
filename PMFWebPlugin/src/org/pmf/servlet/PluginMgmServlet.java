package org.pmf.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.pmf.eao.InvokeEao;
import org.pmf.eao.PluginEao;
import org.pmf.entity.Plugin;

/**
 * Servlet implementation class PluginMgmServlet
 */
@WebServlet("/PluginMgmServlet")
public class PluginMgmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private PluginEao pluginEao;
	
	@EJB
	private InvokeEao invokeEao;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PluginMgmServlet() {
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
		String opstr = request.getParameter("op");
		System.out.println("op="+opstr);
		int op = Integer.parseInt(opstr);
		switch (op) {
			case 0:
				this.uploadPlugin(request, response);
				break;
			case 1:
				this.queryPlugin(request, response);
				break;
			case 2:
				this.updatePluginByApiKey(request, response);
				break;
			case 3:
				this.removePluginByApiKey(request, response);
				break;
			case 4:
				this.queryPluginHtml(request, response);
				break;
			default:
				break;
		}
	}
	
	private void uploadPlugin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String apiKey =  request.getParameter("apiKey");
		if (apiKey == null) {
			JSONObject json = new JSONObject();
			json.element("status", "ERROR");
			JSONArray jsonarray = new JSONArray();
			JSONObject inner = new JSONObject();
			inner.element("awarning", "Invalid Api Key, Please input another one.");
			jsonarray.element(inner);
			json.element("msgs", jsonarray);
			response.setContentType("application/json; charset=utf-8");		
			PrintWriter out = response.getWriter();
			out.print(json);
			return;
		}
		System.out.println("apiKey="+apiKey);
		if (pluginEao.findAvailablePluginByApiKey(apiKey) != null) {
			JSONObject json = new JSONObject();
			json.element("status", "ERROR");
			JSONArray jsonarray = new JSONArray();
			JSONObject inner = new JSONObject();
			inner.element("awarning", "Duplicated Api Key, Please input another one.");
			jsonarray.element(inner);
			json.element("msgs", jsonarray);
			response.setContentType("application/json; charset=utf-8");		
			PrintWriter out = response.getWriter();
			out.print(json);
			return;
        }
		//得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
        String confPath = this.getServletContext().getRealPath("/WEB-INF/conf");
        File confDir = new File(confPath);
        //判断上传文件的保存目录是否存在
        if (!confDir.exists() && !confDir.isDirectory()) {
            System.out.println(confPath+"目录不存在，需要创建");
            //创建目录
            confDir.mkdir();
        }
        
        //消息提示
        String message = "";
        String filename = "";
        String filesuffix = "";
        File file = null;
        
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String> suffix_path = new HashMap<String,String>();
        suffix_path.put("jar", this.getServletContext().getRealPath("/WEB-INF/plugin"));
        suffix_path.put("html", this.getServletContext().getRealPath("/"));
        for (String key : suffix_path.keySet()) {
        	String path = suffix_path.get(key);
            File dir = new File(path);
            //判断上传文件的保存目录是否存在
            if (!dir.exists() && !dir.isDirectory()) {
                System.out.println(path+"目录不存在，需要创建");
                //创建目录
                dir.mkdir();
            }
        }
        try{
            //使用Apache文件上传组件处理文件上传步骤：
            //1、创建一个DiskFileItemFactory工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //2、创建一个文件上传解析器
            ServletFileUpload upload = new ServletFileUpload(factory);
             //解决上传文件名的中文乱码
            upload.setHeaderEncoding("UTF-8"); 
            //3、判断提交上来的数据是否是上传表单的数据
            if(!ServletFileUpload.isMultipartContent(request)){
                //按照传统方式获取数据
                return;
            }
            //4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
            List<FileItem> list = upload.parseRequest(request);
            for(FileItem item : list){
                //如果fileitem中封装的是普通输入项的数据
                if(item.isFormField()){
                    String name = item.getFieldName();
                    //解决普通输入项的数据的中文乱码问题
                    String value = item.getString("UTF-8");
                    params.put(name, value);
                    System.out.println(name + "=" + value);
                }else{//如果fileitem中封装的是上传文件
                    //得到上传的文件名称，
                    filename = item.getName();
                    System.out.println(filename);
                    if(filename==null || filename.trim().equals("")){
                        continue;
                    }
                    filesuffix = filename.substring(filename.lastIndexOf(".") + 1);  
                    //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                    //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                    boolean typeok = false;
                    for (String key : suffix_path.keySet()) {
                    	if (key.equals(filesuffix)) {
                    		typeok = true;
                    		break;
                    	}
                    }
                    if (!typeok) {
                    	message= "仅支持.jar和.html文件";
                        JSONObject json = new JSONObject();
            			json.element("status", "ERROR");
            			response.setContentType("application/json; charset=utf-8");		
            			PrintWriter out = response.getWriter();
            			out.print(json);
            			return;
                    }
                    filename = UUID.randomUUID().toString().replaceAll("-", "") + "." + filesuffix;
                    if (filesuffix.equals("jar")) {
                    	params.put("jarName", filename);
                    } else if (filesuffix.equals("html")) {
                    	params.put("pageName", filename);
                    }
                    file = new File(suffix_path.get(filesuffix),filename);
                    item.write(file);
                    //删除处理文件上传时生成的临时文件
                    item.delete();
                    message = "文件上传成功！";
                }
            }
            Plugin.Category category = Plugin.Category.values()[Integer.parseInt(params.get("category"))];
            Plugin plugin = new Plugin(params.get("pluginName"), params.get("apiKey"), params.get("developer"), category, params.get("description"), params.get("serviceClass"), params.get("jarName"), params.get("pageName"), 1);
            pluginEao.save(plugin);
        	this.queryPlugin(request, response);
			return;
        }catch (Exception e) {
            message= "文件上传失败！";
            e.printStackTrace();
            JSONObject json = new JSONObject();
			json.element("status", "ERROR");
			response.setContentType("application/json; charset=utf-8");		
			PrintWriter out = response.getWriter();
			out.print(json);
			return;
        }
	}
	
	private void queryPlugin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int interval = 7;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final long MS_IN_DAY = 24*60*60*1000;
		
		// get all available plugins;
		Date now = new Date();
		now = new Date(now.getTime() + MS_IN_DAY);
		String nowStr = sdf.format(now);
		try {
			now = sdf.parse(nowStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date sevenDaysAgo = new Date(now.getTime() - (interval * MS_IN_DAY));
		List<Plugin> plugins = pluginEao.findAllAvailablePlugins();
		List invokeCount = invokeEao.queryInvokeByPlugin();
		List invokeArea = invokeEao.queryInvokeByPluginByPeriod(sevenDaysAgo, now);
		response.setContentType("application/json; charset=utf-8");		
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		json.element("status", "OK");
		// deal with invokeArea (available plugin)
		Map<String, List<Integer>> areas = new HashMap<String, List<Integer>>();
		if (invokeArea != null & !invokeArea.isEmpty()) {
			for (int i=0; i<invokeArea.size(); i++) {
				Object[] row = (Object[]) invokeArea.get(i);
				Plugin p = (Plugin) row[0];
				Date t = (Date) row[1];
				int c = (new Long((long) row[2])).intValue();
				if (p.getIsAvailable() > 0) {
					if (!areas.containsKey(p.getPluginName())) {
						List<Integer> list = new ArrayList<Integer>();
						for (int j=0; j<interval; j++) {
							list.add(0);
						}
						areas.put(p.getPluginName(), list);
					}
					long period = t.getTime() - sevenDaysAgo.getTime();
					int idx = 0;
					if (period >= 0) {
						idx = new Long(period / MS_IN_DAY).intValue();
					}
					idx = (idx > (interval-1)) ? (interval-1) : idx;
					System.out.println("idx:"+idx);
					int cnt = areas.get(p.getPluginName()).get(idx);
					cnt += c;
					areas.get(p.getPluginName()).set(idx, cnt);
				}
			}
		}
		if (!areas.isEmpty()) {
			// series
			JSONArray series = new JSONArray();
			for (String s : areas.keySet()) {
				JSONArray inner = new JSONArray();
				List<Integer> list = areas.get(s);
				for (int i=0; i<list.size(); i++) {
					inner.element(list.get(i));
				}
				JSONObject item = new JSONObject();
				item.element("name", s);
				item.element("data", inner);
				series.element(item);
			}
			// categories
			JSONArray cats = new JSONArray();
			for (int i=0; i<interval; i++) {
				Date tmp = new Date(sevenDaysAgo.getTime()+i*MS_IN_DAY);
				String tmpStr = sdf.format(tmp);
				cats.element(tmpStr);
			}
			JSONObject areaJson = new JSONObject();
			areaJson.element("categories", cats);
			areaJson.element("series", series);
			json.element("invokeArea", areaJson);
		}
		// deal with invokeCount (available plugin)
		Map<String, Integer> counts = new HashMap<String, Integer>(); 
		for (int i=0; i<invokeCount.size(); i++) {
			Object[] row = (Object [])invokeCount.get(i);
			Plugin p = (Plugin) row[0];
			int c = (new Long((long) row[1])).intValue();
			if (p.getIsAvailable() > 0) {
				counts.put(p.getPluginName(), c);
			}
		}
		if (!counts.isEmpty()) {
			JSONArray array = new JSONArray();
			int i = 0;
			for (String s :counts.keySet()) {
				if (i != (counts.size()-1)) {
					JSONArray inner = new JSONArray();
					inner.element(s);
					inner.element(counts.get(s));
					array.element(inner);
				} else {
					JSONObject inner = new JSONObject();
					inner.element("name", s);
					inner.element("y", counts.get(s));
					inner.element("sliced", true);
					inner.element("selected", true);
					array.element(inner);
				}
				i++;
			}
			json.element("invokeCount", array);
		}
		// deal with available plugin
		if (plugins != null && !plugins.isEmpty()) {
			JSONArray array = new JSONArray();
			// 设置JSON-LIB让其过滤掉引起循环的字段：
			JsonConfig config = new JsonConfig(); // 建立配置文件
			config.setIgnoreDefaultExcludes(false); // 设置默认忽略
			config.setExcludes(new String[] {"pid", "category", "serviceClass", "jarName", "pageName", "isAvailable", "invokes", "users"}); // 设置忽略的key
			for (int i=0; i<plugins.size(); i++) {
				Plugin plugin = plugins.get(i);
				JSONObject j = JSONObject.fromObject(plugin, config);
				j.element("ID", (i+1));
				String s = "INVALID";
				Plugin.Category cat = plugin.getCategory();
				s = Plugin.cateNames[cat.ordinal()];
				j.element("category", s);
				array.element(j);
			}
			json.element("list", array);
		}
		System.out.println(json);
		out.print(json);
	}
	
	private void removePluginByApiKey(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String apiKey =  request.getParameter("apiKey");
		Plugin plugin = pluginEao.findAvailablePluginByApiKey(apiKey);
		if (plugin != null) {
			plugin.setIsAvailable(0);
			pluginEao.update(plugin);
		}
		this.queryPlugin(request, response);
	}
	
	private void updatePluginByApiKey(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String apiKey =  request.getParameter("apiKey");
		Plugin plugin = pluginEao.findAvailablePluginByApiKey(apiKey);
		if (plugin == null) {
			JSONObject json = new JSONObject();
			json.element("status", "ERROR");
			response.setContentType("application/json; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(json);
			return;
		}
		String updateJsonStr = request.getParameter("updateJson");
		JSONObject updateJson = JSONObject.fromObject(updateJsonStr);
		String pluginName = updateJson.getString("pluginName");
		Plugin.Category category = Plugin.Category.values()[Integer.parseInt(updateJson.getString("category"))];
		String developer = updateJson.getString("developer");
		String description = updateJson.getString("description");
		plugin.setPluginName(pluginName);
		plugin.setCategory(category);
		plugin.setDeveloper(developer);
		plugin.setDescription(description);
		pluginEao.update(plugin);
		this.queryPlugin(request, response);
	}
	
	private void queryPluginHtml(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Plugin> plugins = pluginEao.findAllAvailablePlugins();
		response.setContentType("application/json; charset=utf-8");		
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		json.element("status", "OK");
		if (plugins != null && !plugins.isEmpty()) {
			Map<Plugin.Category, Set<Plugin>> catPlugin = new HashMap<Plugin.Category, Set<Plugin>>();
			for (Plugin plugin : plugins) {
				if (!catPlugin.containsKey(plugin.getCategory())) {
					Set<Plugin> set = new HashSet<Plugin>();
					catPlugin.put(plugin.getCategory(), set);
				}
				catPlugin.get(plugin.getCategory()).add(plugin);
			}
			if (!catPlugin.isEmpty()) {
				JSONArray array = new JSONArray();
				for(Plugin.Category cat : catPlugin.keySet()) {
					Set<Plugin> set = catPlugin.get(cat);
					JSONObject jso = new JSONObject();
					JSONArray inner = new JSONArray();
					for (Plugin p : set) {
						JSONObject j = new JSONObject();
						j.element("pluginName", p.getPluginName());
						j.element("description", p.getDescription());
						j.element("pageName", p.getPageName());
						inner.element(j);
					}
					jso.element(Plugin.cateNamesInShort[cat.ordinal()], inner);
					array.element(jso);
				}
				json.element("cateTree", array);
			}
		}
		System.out.println(json);
		out.print(json);
	}

}
