package org.middleheaven.core.bootstrap.client;

import org.middleheaven.core.services.Service;
import org.middleheaven.core.services.ServiceNotAvailableException;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.logging.Logger;
import org.middleheaven.process.MapContext;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIEnvironmentType;
import org.middleheaven.ui.UIService;
import org.middleheaven.ui.components.UIDesktop;
import org.middleheaven.ui.desktop.swing.SwingRenderKit;
import org.middleheaven.ui.rendering.RenderingContext;


public class DesktopUIBootstrapEnvironment extends AbstractStandaloneBootstrapEnvironment {

	RenderingContext renderedContext;
	private Logger logger;
	private UIService uiService;

	public DesktopUIBootstrapEnvironment(Logger logger, UIService uiService) {
		super();
		this.logger = logger;
		this.uiService = uiService;
		this.renderedContext = new RenderingContext(new MapContext(), new SwingRenderKit());
	}

	public void start(){

		try{
			
			UIClient client = uiService.getUIClientRendering(UIEnvironmentType.DESKTOP).getComponent();

			UIComponent mainWindow;
			 if (client.getChildrenCount()>1){
				mainWindow = client.resolveMainWindow((UIDesktop)client,renderedContext);
			} else if (client.getChildrenCount()==0){
				logger.error("No main window found");
				return;
			} else {
				mainWindow = client.getChildrenComponents().get(0);
			}

			 client.getSceneNavigator().show(mainWindow);
			
		} catch (ServiceNotAvailableException e){
			logger.warn("Executing without UI client");
		}

	}

	@Override
	public void stop() {
		try{

			UIClient client =  uiService.getUIClientRendering(UIEnvironmentType.DESKTOP).getComponent();
			
			UIComponent mainWindow = client.resolveMainWindow((UIDesktop)client,renderedContext);

			client.getSceneNavigator().dispose(mainWindow);
			
		} catch (ServiceNotAvailableException e){
			logger.trace("Stopping without UI client");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return "desktop";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Service resolverRequestedService(ServiceSpecification spec) {
		return null;
	}


}
