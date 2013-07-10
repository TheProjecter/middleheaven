package org.middleheaven.application.web;

import org.middleheaven.application.AbstractListenableModuleActivator;
import org.middleheaven.application.ApplicationContext;
import org.middleheaven.process.web.server.HttpServerService;

/**
 * A Web Application main module.
 * 
 * A WebModule has access to a {@link HttpServerService}.
 */
public abstract class WebModule extends AbstractListenableModuleActivator {

	
	private HttpServerService serverService;

	
	public WebModule() {}

	/**
	 * 
	 * {@inheritDoc}
	 */
	protected final HttpServerService getHttpServerService(){
		return serverService;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public final void doStartListenableModule(ApplicationContext context) {

		serverService = context.getServiceContext().getService(HttpServerService.class);

		configurateModule(context);
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public void doStopListenableModule(ApplicationContext context){
		//no-op
	}
	
	/**
	 * @param context
	 */
	protected abstract void configurateModule(ApplicationContext context);



}
