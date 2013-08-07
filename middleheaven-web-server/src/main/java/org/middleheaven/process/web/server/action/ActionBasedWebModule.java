package org.middleheaven.process.web.server.action;

import org.middleheaven.application.ApplicationContext;
import org.middleheaven.application.ModuleActivationEvent;
import org.middleheaven.application.ModuleActivatorListener;
import org.middleheaven.application.web.WebModule;

/**
 * Represent an action based application running the the web container.
 */
public abstract class ActionBasedWebModule  extends WebModule{

	/**
	 * 
	 * Constructor.
	 */
	public ActionBasedWebModule() {

	}
	
	protected final String processorID() {
		return this.getModuleVersion().toString() + "_processor";
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected void configurateModule(ApplicationContext context){
	
		this.addModuleActivatorListener(new ModuleActivatorListener() {
			
			@Override
			public void onModuleStop(ModuleActivationEvent event) {
				getHttpServerService().unRegisterHttpProcessor(processorID());
			}
			
			@Override
			public void onModuleStart(ModuleActivationEvent event) {}
		});
	
		configurate(context);
	}

	
//	protected void setDefaults(URLMappingBuilder builder ,String url ){
//		// set defaults
//		if (url.indexOf(".")>=0){
//			String[] parts = StringUtils.split(url, '.');
//			String presenter = parts[0]; 
//			builder.withNoAction()
//			.onSuccess().forwardTo(presenter.concat("/sucess.jsp"))
//			.onInvalid().forwardTo(presenter.concat("/invalid.jsp"))
//			.onFailure().forwardTo("error.jsp")
//			.onError().forwardTo("error.jsp");
//		}
//	}


	protected abstract void configurate(ApplicationContext context);


}
