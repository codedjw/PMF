package org.pmf.entity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

@Entity
@Table(name="plugin_table")
public class Plugin {
	
	public enum Category {
		CFD, SND
	}
	
	public static String[] cateNames = {"Control-flow Discovery", "Social-network Discovery"};
	
	public static String[] cateNamesInShort = {"Control Flows", "Social Networks"};
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int pid;
	
	@Column(nullable=false)
	private String pluginName;
	
	@Column(nullable=false)
	private String apiKey;
	
	@Column(nullable=false)
	private String developer;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable=false)
	private Category category;
	
	@Column(nullable=false, length=10000)
	private String description;
	
	@Column(nullable=false)
	private String serviceClass;
	
	@Column(nullable=false)
	private String jarName;
	
	@Column(nullable=false)
	private String pageName;
	
	@Column(nullable=false)
	private int isAvailable;
	
	@OneToMany(mappedBy="plugin",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Invoke> invokes = new HashSet<Invoke>();
	
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "plugin_user", 
            joinColumns = { @JoinColumn(name = "plugin_id", referencedColumnName = "pid") }, 
            inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "uid") })
	private Set<User> users = new HashSet<User>();
	
	public Plugin() {
		
	}

	public Plugin(String pluginName, String apiKey, String developer,
			Category category, String description, String serviceClass, String jarName, String pageName, int isAvailable) {
		super();
		this.pluginName = pluginName;
		this.apiKey = apiKey;
		this.developer = developer;
		this.category = category;
		this.description = description;
		this.serviceClass = serviceClass;
		this.jarName = jarName;
		this.pageName = pageName;
		this.isAvailable = isAvailable;
	}
	
	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
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

	public int getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(int isAvailable) {
		this.isAvailable = isAvailable;
	}

	public Set<Invoke> getInvokes() {
		return invokes;
	}

	public void setInvokes(Set<Invoke> invokes) {
		this.invokes = invokes;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	 public void addInvoke(Invoke invoke) {
	       if (!this.invokes.contains(invoke)) {
	            this.invokes.add(invoke);
	            invoke.setPlugin(this);
	       }
	    }

	    public void removeInoke(Invoke invoke) {
	        if (this.invokes.contains(invoke)) {
	            invoke.setPlugin(null);
	            this.invokes.remove(invoke);
	        }
	    }

	@Override
	public String toString() {
		return "Plugin [pid=" + pid + ", pluginName=" + pluginName
				+ ", apiKey=" + apiKey + ", developer=" + developer
				+ ", category=" + category + ", description=" + description
				+ ", serviceClass=" + serviceClass + ", jarName=" + jarName
				+ ", pageName=" + pageName + ", isAvailable=" + isAvailable
				+ ", invokes=" + invokes + ", users=" + users + "]";
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
			Plugin plugin = new Plugin(pluginName, apiKey, developer, Category.valueOf(category), description, serviceClass, jarName, pageName, 1);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + pid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Plugin other = (Plugin) obj;
		if (pid != other.pid)
			return false;
		return true;
	}

}
