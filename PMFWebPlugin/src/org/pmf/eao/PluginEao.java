package org.pmf.eao;

import java.util.List;

import javax.ejb.Local;

import org.pmf.entity.Plugin;

@Local
public interface PluginEao extends Eao {
	
	public Plugin findAvailablePluginByApiKey(String apiKey);
	
	public List<Plugin> findAllAvailablePlugins();

}
