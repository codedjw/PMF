package org.pmf.eao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.pmf.entity.Plugin;

/**
 * Session Bean implementation class PluginEaoImpl
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PluginEaoImpl extends EaoImpl implements PluginEao {

	@Override
	public Plugin findAvailablePluginByApiKey(String apiKey) {
		// TODO Auto-generated method stub
		List<Plugin> pluginList = getResultList(Plugin.class
				, "where o.apiKey = ?1 and o.isAvailable > 0"
				, null
				, apiKey);
		if (pluginList != null && pluginList.size() > 0) {
			return pluginList.get(0);
		}
		return null;
	}

	@Override
	public List<Plugin> findAllAvailablePlugins() {
		// TODO Auto-generated method stub
		List<Plugin> pluginList = getResultList(Plugin.class
				, "where o.isAvailable > 0"
				, null);
		if (pluginList != null && pluginList.size() > 0) {
			return pluginList;
		}
		return null;
	}

}
