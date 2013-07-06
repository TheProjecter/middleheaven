/**
 * 
 */
package org.middleheaven.application;

import org.middleheaven.events.EventListenersSet;

/**
 * 
 */
public abstract class AbstractListenableModuleActivator implements ListenableModuleActivator {


	
	private EventListenersSet<ModuleActivatorListener> eventListeners;
			
	public AbstractListenableModuleActivator (){}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void start(ApplicationContext context) {
		doStartListenableModule(context);
		
		if (eventListeners != null){
			eventListeners.broadcastEvent().onModuleStart(new ModuleActivationEvent(this));
		}
		
	}
	
	protected abstract void doStartListenableModule(ApplicationContext context);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop(ApplicationContext context) {
		doStopListenableModule(context);
		
		if (eventListeners != null){
			eventListeners.broadcastEvent().onModuleStop(new ModuleActivationEvent(this));
		}
	}

	protected abstract void doStopListenableModule(ApplicationContext context);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final synchronized void addModuleActivatorListener(ModuleActivatorListener listener) {
		if (eventListeners == null){
			this.eventListeners =  EventListenersSet.newSet(ModuleActivatorListener.class);
		}
		this.eventListeners.addListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void removeModuleActivatorListener(ModuleActivatorListener listener) {
		if (eventListeners != null){
			this.eventListeners.removeListener(listener);
		}
	}

}
