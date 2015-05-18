package org.pmf.tools.snminer.mining;

public class SNMiningOptions {
	
	// constants for the possible matrix (xor)
	public final static int HANDOVER_OF_WORK = 0;
	public final static int SUBCONTRACTING = 1;
	
	// constants for 'subcontracting/handover of work' setting (or)
	public final static int CONSIDER_CAUSALITY = 10;
	public final static int CONSIDER_DIRECT_SUCCESSION = 100;
	public final static int CONSIDER_MULTIPLE_TRANSFERS = 1000;
	
}
