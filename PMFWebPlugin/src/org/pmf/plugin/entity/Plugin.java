package org.pmf.plugin.entity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Plugin {
	
	public enum Category {
		CFD, SND
	}
	
	private String pluginName;
	
	private String apiKey;
	
	private String developer;
	
	private Category category;
	
	private String description;
	
	private String serviceClass;
	
	private String jarName;
	
	private String pageName;
	
	public Plugin() {
		
	}

	public Plugin(String pluginName, String apiKey, String developer,
			Category category, String description, String serviceClass, String jarName, String pageName) {
		super();
		this.pluginName = pluginName;
		this.apiKey = apiKey;
		this.developer = developer;
		this.category = category;
		this.description = description;
		this.serviceClass = serviceClass;
		this.jarName = jarName;
		this.pageName = pageName;
	}

	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}
	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	public String getJarName() {
		return jarName;
	}

	public void setJarName(String jarName) {
		this.jarName = jarName;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	@Override
	public String toString() {
		return "Plugin [pluginName=" + pluginName + ", apiKey=" + apiKey
				+ ", developer=" + developer + ", category=" + category
				+ ", description=" + description + ", serviceClass="
				+ serviceClass + ", jarName=" + jarName + ", pageName="
				+ pageName + "]";
	}

	public static void storePlugin(Plugin plugin, String path) {
		if (plugin == null || plugin.getApiKey() == null || plugin.getApiKey().equals("")) {
			return;
		}
		Properties prop = new Properties();
		prop.put("pluginName", plugin.getPluginName());
		prop.put("apiKey", plugin.getApiKey());
		prop.put("developer", plugin.getDeveloper());
		prop.put("category", plugin.getCategory().toString());
		prop.put("description", plugin.getDescription());
		prop.put("serviceClass", plugin.getServiceClass());
		prop.put("jarName", plugin.getJarName());
		prop.put("pageName", plugin.getPageName());
		try {
			String wholename = path+plugin.getApiKey()+".properties";
			System.out.println(wholename);
			FileOutputStream out = new FileOutputStream(wholename);
			prop.store(out, plugin.getPluginName());
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Plugin loadPlugin(String apiKey, String path) {
		if (apiKey == null || apiKey.equals("")) {
			return null;
		}
		try {
			String wholename = path+apiKey+".properties";
			System.out.println(wholename);
			FileInputStream in = new FileInputStream(wholename);
			Properties prop = new Properties();
			prop.load(in);
			in.close();
			String pluginName = (prop.getProperty("pluginName") == null) ? "" : prop.getProperty("pluginName");
			String apiKey2 = (prop.getProperty("apiKey") == null) ? "" : prop.getProperty("apiKey");
			String developer = (prop.getProperty("developer") == null) ? "" : prop.getProperty("developer");
			String category = (prop.getProperty("category") == null) ? "" : prop.getProperty("category");
			String description = (prop.getProperty("description") == null) ? "" : prop.getProperty("description");
			String serviceClass = (prop.getProperty("serviceClass") == null) ? "" : prop.getProperty("serviceClass");
			String jarName = (prop.getProperty("jarName") == null) ? "" : prop.getProperty("jarName");
			String pageName = (prop.getProperty("pageName") == null) ? "" : prop.getProperty("pageName");
			
			if (!apiKey.equals(apiKey2)) {
				return null;
			}
			Plugin plugin = new Plugin(pluginName, apiKey, developer, Category.valueOf(category), description, serviceClass, jarName, pageName);
			return plugin;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

}
