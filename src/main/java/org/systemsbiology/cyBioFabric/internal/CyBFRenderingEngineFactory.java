package org.systemsbiology.cyBioFabric.internal;

import java.awt.Container;
import javax.swing.JComponent;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.View;
import org.cytoscape.view.model.VisualLexicon;
import org.cytoscape.view.presentation.RenderingEngine;
import org.cytoscape.view.presentation.RenderingEngineFactory;
import org.cytoscape.view.presentation.RenderingEngineManager;
import org.cytoscape.work.swing.DialogTaskManager;
import org.systemsbiology.cyBioFabric.internal.CyBFRenderingEngine;
import org.systemsbiology.cyBioFabric.internal.cytoscape.view.CyBFNetworkView;
import org.systemsbiology.cyBioFabric.internal.events.BioFabricNetworkViewAddedHandler;
import org.systemsbiology.cyBioFabric.internal.events.BioFabricNetworkViewToBeDestroyedHandler;
import org.systemsbiology.cyBioFabric.internal.graphics.BioFabricCytoPanel;
import org.systemsbiology.cyBioFabric.internal.graphics.GraphicsConfiguration;
import org.systemsbiology.cyBioFabric.internal.graphics.GraphicsConfigurationFactory;
import org.systemsbiology.cyBioFabric.internal.task.TaskFactoryListener;

/** The RenderingEngineFactory for the CyBFRenderingEngine
 * You should provide two rendering engine factories, 
 * one for the main view, and another for the smaller birds-eye view.

The RenderingEngineFactory creates an instance of RenderingEngine for the given network view.
The RenderingEngineFactory also provides access to your VisualLexicon.
A container object is passed to the factory whenever a RenderingEngine is created. 
This is typically an instance of JComponent, and will be the parent for the RenderingEngine's drawing canvas.
Your RenderingEngineFactory must also register the newly created RenderingEngine with the Cytoscape RenderingEngineManager.
In CyBF there is one class CyBFRenderingEngineFactory that implements RenderingEngineFactory. Two instances are created,
each is parameterized with a GraphicsConfigurationFactory which provides functionality that is specific to the 
main view or the birds-eye view.

 * @author paperwing (Yue Dong)
 */
public class CyBFRenderingEngineFactory implements RenderingEngineFactory<CyNetwork> {
	
	private final RenderingEngineManager renderingEngineManager;
	private final VisualLexicon visualLexicon;
	private final TaskFactoryListener taskFactoryListener;
	private final DialogTaskManager taskManager;
	private final GraphicsConfigurationFactory graphicsConfigFactory;
	private final CyLayoutAlgorithmManager layoutAlgorithmManager;
	private final String defaultLayout;
	private BioFabricNetworkViewAddedHandler addNetworkHandler; 
	private BioFabricNetworkViewToBeDestroyedHandler destroyNetworkHandler;	
	
	
	public CyBFRenderingEngineFactory(			
			RenderingEngineManager renderingEngineManager, 
			VisualLexicon lexicon,
			TaskFactoryListener taskFactoryListener,
			DialogTaskManager taskManager,			
			GraphicsConfigurationFactory graphicsConfigFactory,
			BioFabricNetworkViewAddedHandler addNetworkHandler, 
			BioFabricNetworkViewToBeDestroyedHandler destroyNetworkHandler,
			CyLayoutAlgorithmManager layoutAlgorithmManager,
			String defaultLayout) {	
		
		//this.layoutAlgorithmManager = layoutAlgorithmManager;
		this.renderingEngineManager = renderingEngineManager;
		this.visualLexicon = lexicon;
		this.taskFactoryListener = taskFactoryListener;
		this.taskManager = taskManager;
		this.graphicsConfigFactory = graphicsConfigFactory;
		this.addNetworkHandler = addNetworkHandler;
		this.destroyNetworkHandler = destroyNetworkHandler;
		this.layoutAlgorithmManager = layoutAlgorithmManager;
		this.defaultLayout = defaultLayout;
	}
	
	/**
	 * Catch these errors up front.
	 * 
	 * @throws ClassCastException if the viewModel is not an instance of CyBFNetworkView
	 * @throws ClassCastException if the container is not an instance of JComponent
	 */
	@Override
	public RenderingEngine<CyNetwork> createRenderingEngine(Object container, View<CyNetwork> viewModel) {

		// Verify the type of the view up front.
		CyBFNetworkView cyBFViewModel = (CyBFNetworkView) viewModel;
		JComponent component = (JComponent) container;
		
		GraphicsConfiguration configuration = graphicsConfigFactory.createGraphicsConfiguration();				
		
		// TODO the birds eye view should not be attaching input listeners to the outer component
		// Is the Birds eye view above the top glass pane?
		JComponent inputComponent = getKeyboardComponent(component, cyBFViewModel.getSUID());
		if(inputComponent == null)
			inputComponent = component; // happens for birds-eye-view
						
		CyBFRenderingEngine engine = new CyBFRenderingEngine(component, inputComponent, cyBFViewModel, visualLexicon,
				                                             configuration, taskFactoryListener, taskManager, addNetworkHandler,  
				                                             destroyNetworkHandler, layoutAlgorithmManager, defaultLayout);
		
		renderingEngineManager.addRenderingEngine(engine);

		return engine;
	}
	
	/**
	 * This is a HACK for now to get the component to attach hotkeys and cursors to.
	 */
	private JComponent getKeyboardComponent(JComponent start, long suid) {
		String componentName = "__CyNetworkView_" + suid; // see ViewUtil.createUniqueKey(CyNetworkView)
		Container parent = start;
		while(parent != null) {
			if(componentName.equals(parent.getName())) {
				return (JComponent) parent;
			}
			parent = parent.getParent();
		}
		return null;
	}
	
	
	@Override
	public VisualLexicon getVisualLexicon() {
		return visualLexicon;
	}
	/*
	//Programatically set the layout algorithm
	private void setLayoutAlgorithm(CyBFNetworkView cyBFViewModel){
		// Get the layout	
		CyLayoutAlgorithm layout = layoutAlgorithmManager.getLayout(bfLayoutAlg.getName());
		if(layout == null){
			System.out.println("layout is null");
			layout = layoutAlgorithmManager.getDefaultLayout();
		}

		// apply the layout
		TaskIterator taskIterator = layout.createTaskIterator(cyBFViewModel, layout.getDefaultLayoutContext(), CyLayoutAlgorithm.ALL_NODE_VIEWS, null);
		taskManager.execute(taskIterator);
	}*/
	
	public void setBioFabricApplication(){}
}

