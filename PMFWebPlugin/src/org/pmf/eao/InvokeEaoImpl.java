package org.pmf.eao;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 * Session Bean implementation class InvokeEaoImpl
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class InvokeEaoImpl extends EaoImpl implements InvokeEao {

	@Override
	public List queryInvokeByPlugin() {
		// TODO Auto-generated method stub
		List result = null;
		result = em.createQuery("select o.plugin, count(o) from Invoke as o group by o.plugin")
				.getResultList();
		return result;
	}

	@Override
	public List queryInvokeByPluginByPeriod(Date from, Date to) {
		// TODO Auto-generated method stub
		System.out.println(from+"-->"+to);
		List result = null;
		result = em.createQuery("select o.plugin, o.itime, count(o) from Invoke as o where o.itime >= ?1 and o.itime < ?2 group by o.plugin, o.itime order by o.itime")
				.setParameter(1, from)
				.setParameter(2, to)
				.getResultList();
		return result;
	}


}
