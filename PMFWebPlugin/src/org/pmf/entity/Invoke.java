package org.pmf.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="invoke_table")
public class Invoke {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int iid;
	
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	private Date itime;
	
	@ManyToOne(cascade=CascadeType.REFRESH, optional=false)
    @JoinColumn(name = "plugin_id")  
	private Plugin plugin;
	
	public Invoke() {
		
	}

	public Invoke(Date itime) {
		super();
		this.itime = itime;
	}

	public int getIid() {
		return iid;
	}

	public void setIid(int iid) {
		this.iid = iid;
	}

	public Date getItime() {
		return itime;
	}

	public void setItime(Date itime) {
		this.itime = itime;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public String toString() {
		return "Invoke [iid=" + iid + ", itime=" + itime + ", plugin=" + plugin
				+ "]";
	}

}
