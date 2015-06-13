package org.pmf.eao;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

@Local
public interface InvokeEao extends Eao {
	
	public List queryInvokeByPlugin();
	
	public List queryInvokeByPluginByPeriod(Date from, Date to);

}
