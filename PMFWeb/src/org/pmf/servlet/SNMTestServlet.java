package org.pmf.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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
//		String logJsonStr = request.getParameter("logJson");
//		System.out.println(logJsonStr);
//		JSONObject logJson = JSONObject.fromObject(logJsonStr);
//		int lid = logJson.getInt("logid");
//		String logfile = "";
//		switch (lid) {
//		case 0:
//			logfile = "repairExample.xes";
//			break;
//		case 1:
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
		try {
			if (file == null) {
				JSONObject json = new JSONObject();
				json.element("status", "ERROR");
				response.setContentType("application/json; charset=utf-8");		
				PrintWriter out = response.getWriter();
				out.print(json);
				return;
			}
			inputfile = new FileInputStream(file);
			List<XLog> logs = parser.parse(inputfile);
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
