package org.pmf.tools.snminer.mining.operation;

import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XLog;

import cern.colt.matrix.tdouble.DoubleMatrix2D;

public abstract class BasicOperation {
	
	protected XLogInfo logInfo;
	protected XLog log;
	protected List<String> performersList;
	protected XEventClasses lcEvents;
	protected String[] tasksArray;
	
	public BasicOperation(XLog log) {
		this.log = log;
		this.logInfo = XLogInfoFactory.createLogInfo(log);
		
		this.performersList = new ArrayList<String>();
		
		XEventClasses performers = this.logInfo.getResourceClasses();
		for (int i=0; i<performers.getClasses().size(); i++) {
			this.performersList.add(performers.getByIndex(i).toString());
		}
		
		XEventClasses tasks = this.logInfo.getNameClasses();
		this.tasksArray = new String[tasks.getClasses().size()];
		for (int i=0; i<tasks.getClasses().size(); i++) {
			tasksArray[i] = tasks.getByIndex(i).toString();
		}
		
		this.lcEvents = this.logInfo.getTransitionClasses();
	}
	
	public abstract DoubleMatrix2D calculation(double beta, int depth);
	
	public DoubleMatrix2D calculation() {
		return this.calculation(1, 1);
	}
	
	public List<String> getPerformersList() {
		return this.performersList;
	}
	
}
