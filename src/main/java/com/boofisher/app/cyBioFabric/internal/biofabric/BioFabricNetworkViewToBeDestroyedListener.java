package com.boofisher.app.cyBioFabric.internal.biofabric;

import javax.swing.SwingUtilities;

import org.cytoscape.view.model.events.NetworkViewAboutToBeDestroyedEvent;
import org.cytoscape.view.model.events.NetworkViewAboutToBeDestroyedListener;

import com.boofisher.app.cyBioFabric.internal.CyBFNetworkViewRenderer;

/**
 * 
 * Use this class to listen for network destroy events.  Can then shut down BioFabric application correctly.
 * 
 * */
public class BioFabricNetworkViewToBeDestroyedListener implements NetworkViewAboutToBeDestroyedListener{
	
	BioFabricNetworkViewToBeDestroyedHandler networkDestroyedHandler;
	
	public BioFabricNetworkViewToBeDestroyedListener(BioFabricNetworkViewToBeDestroyedHandler networkDestroyedHandler){
		this.networkDestroyedHandler = networkDestroyedHandler;
	}
	
	@Override
	public void handleEvent(NetworkViewAboutToBeDestroyedEvent e) {
		
		if(e.getNetworkView().getRendererId().equals(CyBFNetworkViewRenderer.ID)){
			if(SwingUtilities.isEventDispatchThread()){
				networkDestroyedHandler.handleEvent(e.getNetworkView().getSUID());
			}else{
				SwingUtilities.invokeLater(new Runnable(){

					@Override
					public void run() {						
						networkDestroyedHandler.handleEvent(e.getNetworkView().getSUID());						
					}					
				});
			}
		}
	}



}
