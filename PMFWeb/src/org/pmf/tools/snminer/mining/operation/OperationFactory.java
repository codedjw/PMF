package org.pmf.tools.snminer.mining.operation;

import org.deckfour.xes.model.XLog;
import org.pmf.tools.snminer.mining.SNMiningOptions;
import org.pmf.tools.snminer.mining.handover.HandOverCCIDCM;
import org.pmf.tools.snminer.mining.handover.HandOverCCIDIM;
import org.pmf.tools.snminer.mining.handover.HandOverICIDCM;
import org.pmf.tools.snminer.mining.handover.HandOverICIDIM;

public class OperationFactory {
	
	public OperationFactory() {}
	
	public static BasicOperation getOperation(XLog log, int type) {
		BasicOperation operation = null;
		
		switch(type) {
		case SNMiningOptions.HANDOVER_OF_WORK + SNMiningOptions.CONSIDER_CAUSALITY + SNMiningOptions.CONSIDER_MULTIPLE_TRANSFERS:
			System.out.println("HandOverCCIDCM");
			operation = new HandOverCCIDCM(log);
			break;
		case SNMiningOptions.HANDOVER_OF_WORK + SNMiningOptions.CONSIDER_CAUSALITY:
			System.out.println("HandOverCCIDIM");
			operation = new HandOverCCIDIM(log);
			break;
		case SNMiningOptions.HANDOVER_OF_WORK + SNMiningOptions.CONSIDER_MULTIPLE_TRANSFERS:
			System.out.println("HandOverICIDCM");
			operation = new HandOverICIDCM(log);
			break;
		case SNMiningOptions.HANDOVER_OF_WORK:
			System.out.println("HandOverICIDIM");
			operation = new HandOverICIDIM(log);
			break;
		default:
			break;
		}
		
		return operation;
	}

}
