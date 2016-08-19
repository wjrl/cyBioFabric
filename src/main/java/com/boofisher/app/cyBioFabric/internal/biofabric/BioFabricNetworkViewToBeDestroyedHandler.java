package com.boofisher.app.cyBioFabric.internal.biofabric;

import java.util.HashMap;
import java.util.Map;

import com.boofisher.app.cyBioFabric.internal.biofabric.app.BioFabricApplication;

public class BioFabricNetworkViewToBeDestroyedHandler {
	Map<Long, BioFabricApplication> apps;
	
	public BioFabricNetworkViewToBeDestroyedHandler(){
		 apps = new HashMap<Long, BioFabricApplication>();
		
	}
	
	public void registerApplication(Long id, BioFabricApplication bfa){
		apps.put(id, bfa);
	}
	
	/* Handle the shut down event, clear cache, free threads etc.
	** Sets the bioFabricApplication to null
	** @param id */
	public void handleEvent(long id){
		BioFabricApplication bfa = apps.get(id);
		if(bfa != null){
			bfa.shutdownFabric();
			bfa = null;//allow for garbage collection
		}
		
	}
	
}
