package org.systemsbiology.cyBioFabric.internal.graphics;

import org.apache.log4j.Logger;
import org.cytoscape.application.CyUserLog;
import org.systemsbiology.cyBioFabric.internal.graphics.AbstractGraphicsConfiguration;

public class BirdsEyeGraphicsConfiguration extends AbstractGraphicsConfiguration {
	final Logger logger = Logger.getLogger(CyUserLog.NAME);
	private final String NAME = "BirdsEyeGraphicsConfiguration";
	
	public BirdsEyeGraphicsConfiguration() {			
		
	}
	
	
	@Override
	public String toString() {
		return NAME;
	}
}
