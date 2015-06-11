package org.pmf.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.model.XLog;
import org.pmf.plugin.service.PluginService;

/**
 * Servlet implementation class AlphaTestServlet
 */
@WebServlet("/AlphaTestServlet")
public class AlphaTestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AlphaTestServlet() {
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
//		String logJsonStr = request.getParameter("logJson");
//		System.out.println(logJsonStr);
//		JSONObject logJson = JSONObject.fromObject(logJsonStr);
//		int lid = logJson.getInt("logid");
//		int lid = 0;
//		String logfile = "";
//		switch (lid) {
//		case 0:
//			logfile = "L1.xes";
//			break;
//		case 1:
//			logfile = "L2.xes";
//			break;
//		case 2:
//			logfile = "L3.xes";
//			break;
//		case 3:
//			logfile = "L4.xes";
//			break;
//		case 4:
//			logfile = "L5.xes";
//			break;
//		case 5:
//			logfile = "L6.xes";
//			break;
//		case 6:
//			logfile = "L7.xes";
//			break;
//		case 7:
//			logfile = "L8.xes";
//			break;
//		case 8:
//			logfile = "running-two-oneloops-pmf.xes";
//			break;
//		case 9:
//			logfile = "bigger-example.xes";
//			break;
//		case 10:
//			logfile = "repairExample.xes";
//			break;
//		case 11:
//			logfile = "reviewing.xes";
//			break;
//		default:
//			logfile = "teleclaims.xes";
//			break;
//		}
		
		//得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
        String savePath = this.getServletContext().getRealPath("/WEB-INF/upload");
        File saveDir = new File(savePath);
        //判断上传文件的保存目录是否存在
        if (!saveDir.exists() && !saveDir.isDirectory()) {
            System.out.println(savePath+"目录不存在，需要创建");
            //创建目录
            saveDir.mkdir();
        }
        //消息提示
        String message = "";
        String filename = "";
        File file = null;
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
                    //value = new String(value.getBytes("iso8859-1"),"UTF-8");
                    System.out.println(name + "=" + value);
                }else{//如果fileitem中封装的是上传文件
                    //得到上传的文件名称，
                    filename = item.getName();
                    System.out.println(filename);
                    if(filename==null || filename.trim().equals("")){
                        continue;
                    }
                    //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                    //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                    if (!filename.endsWith(".xes")) {
                    	message= "仅支持.xes文件";
                        JSONObject json = new JSONObject();
            			json.element("status", "ERROR");
            			response.setContentType("application/json; charset=utf-8");		
            			PrintWriter out = response.getWriter();
            			out.print(json);
            			return;
                    }
                    filename = UUID.randomUUID().toString().replaceAll("-", "") + ".xes";
                    file = new File(savePath,filename);
                    item.write(file);
                    //删除处理文件上传时生成的临时文件
                    item.delete();
                    message = "文件上传成功！";
                }
            }
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
		
		XesXmlParser parser =  new XesXmlParser();
		InputStream inputfile;
		if (file == null) {
			JSONObject json = new JSONObject();
			json.element("status", "ERROR");
			response.setContentType("application/json; charset=utf-8");		
			PrintWriter out = response.getWriter();
			out.print(json);
			return;
		}
		inputfile = new FileInputStream(file);
		List<XLog> logs;
		try {
			logs = parser.parse(inputfile);
			if (logs != null && !logs.isEmpty()) {
				XLog log = logs.get(0);
				String pluginPath = this.getServletContext().getRealPath("/WEB-INF/plugin");
				ClassLoader cl = new URLClassLoader(new URL[] {new URL("file:///"+pluginPath+"/AlphaPlusMiner_fat.jar")}, this.getClass().getClassLoader());
				Class<?> c = Class.forName("org.pmf.tools.alphaminer.AlphaPluginService", true, cl);
				Class<? extends PluginService> pluginClass = c.asSubclass(PluginService.class);
//				Object o = c.newInstance();
//				if (o instanceof PluginService) {
//					System.out.println("yes");
//				} else {
//					System.out.println("no");
//				}
				PluginService service = pluginClass.newInstance();
				JSONObject json = service.doPluginService(log, null);
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
	
	
	
	

}
