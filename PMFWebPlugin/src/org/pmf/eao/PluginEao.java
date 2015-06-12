package org.pmf.eao;

import javax.ejb.Local;

import org.pmf.entity.Plugin;

@Local
public interface PluginEao extends Eao {
	
	public Plugin findAvailablePluginByApiKey(String apiKey);

}
