/*
 * Created on 2006/08/19
 *
 */
package org.middleheaven.core.bootstrap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.middleheaven.core.Container;
import org.middleheaven.core.ContextIdentifier;
import org.middleheaven.core.services.RegistryServiceContext;
import org.middleheaven.core.services.ServiceContextConfigurator;
import org.middleheaven.core.services.ServiceContextEngineConfigurationService;
import org.middleheaven.core.services.discover.ServiceActivatorDiscoveryEngine;
import org.middleheaven.core.services.engine.ActivatorBagServiceDiscoveryEngine;
import org.middleheaven.core.services.engine.LocalFileRepositoryDiscoveryEngine;
import org.middleheaven.core.wiring.DefaultWiringService;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.global.atlas.modules.AtlasActivator;
import org.middleheaven.io.repository.FileRepositoryActivator;
import org.middleheaven.logging.LogBook;
import org.middleheaven.logging.LoggingActivator;

/**
 * This is the entry point for all applications
 * Subclasses of <code>ExecutionEnvironmentBootstrap</code> implement
 * bootstrap in different execution environments and allow
 * the applications execution to be environment independent.
 * 
 * @author Sergio M. M. Taborda 
 *
 */
public abstract class ExecutionEnvironmentBootstrap {

	
	private ListServiceContextConfigurator configurator = new ListServiceContextConfigurator();
	private SimpleBootstrapService bootstrapService = new SimpleBootstrapService();
	private RegistryServiceContext serviceRegistryContext;
	
	/**
	 * Start the environment 
	 */
	public final void start(LogBook log){
		long time = System.currentTimeMillis();
		
		serviceRegistryContext = new RegistryServiceContext(log);

		doBeforeStart();

		log.debug("Resolving container");
		
		Container container = getContainer();
		
		log.trace("Container resolved: " + container.getEnvironmentName());
		
		log.debug("Register bootstrap services");

		serviceRegistryContext.register(WiringService.class, new DefaultWiringService(),null);
		serviceRegistryContext.register(BootstrapService.class, bootstrapService,null);
		serviceRegistryContext.register(ServiceContextEngineConfigurationService.class, new UniqueServiceContextEngineConfigurationService(), null);
		
		bootstrapService.fireBootupStart();
		
		log.debug("Inicialize service discovery engines");
		
		ActivatorBagServiceDiscoveryEngine engine = new ActivatorBagServiceDiscoveryEngine()
		.addActivator(AtlasActivator.class)
		.addActivator(LoggingActivator.class)
		.addActivator(FileRepositoryActivator.class);
		
		
		configurator.addEngine(engine);
		configurator.addEngine(new LocalFileRepositoryDiscoveryEngine());

		container.init(this);

		doAfterStart();

		log.info("Environment inicialized in " + (System.currentTimeMillis()-time) + " ms.");
		bootstrapService.fireBootupEnd();
	}

	public ServiceContextConfigurator getServiceContextConfigurator(){
		return this.configurator;
	}
	
	
	private class SimpleBootstrapService implements BootstrapService{
		

		private List<BootstapListener> listeners = new CopyOnWriteArrayList<BootstapListener>();
		
		public SimpleBootstrapService() {
		}

		protected void fireBootupStart() {
			fire (new BootstrapEvent(true,true));
		}
		
		protected void fireBootupEnd() {
			fire (new BootstrapEvent(true,false));
		}

		protected void fireBootdownEnd() {
			fire (new BootstrapEvent(false,false));
		}

		protected void fireBootdownStart() {
			fire (new BootstrapEvent(false,true));
		}
		
		protected void fire(BootstrapEvent event) {
			for (BootstapListener listener : listeners){
				listener.onBoostapEvent(event);
			}
		}
		
		@Override
		public Container getContainer() {
			return ExecutionEnvironmentBootstrap.this.getContainer();
		}

		@Override
		public void addListener(BootstapListener listener) {
			listeners.add(listener);
		}

		@Override
		public void removeListener(BootstapListener listener) {
			listeners.remove(listener);
		}


		
	}
	
	public final void stop(){
		bootstrapService.fireBootdownStart();
		doBeforeStop();
		configurator.removeAllEngines();
		
		serviceRegistryContext.unRegister(ServiceContextEngineConfigurationService.class);
		serviceRegistryContext.unRegister(BootstrapService.class);
		serviceRegistryContext.unRegister(WiringService.class);
	
		doAfterStop();

		bootstrapService.fireBootdownEnd();
	}

	protected void doBeforeStart(){};
	protected void doAfterStart(){}; 
	protected void doBeforeStop(){};
	protected void doAfterStop(){}; 

	public abstract ContextIdentifier getContextIdentifier();
	public abstract Container getContainer();

	private class ListServiceContextConfigurator extends ServiceContextConfigurator {
		
		ArrayList<ServiceActivatorDiscoveryEngine> engines = new ArrayList<ServiceActivatorDiscoveryEngine>();
		public void addEngine(ServiceActivatorDiscoveryEngine engine){
			engines.add(engine);
			engines.trimToSize();
			super.addEngine(engine);
		}
		
	}
	
		
	private class UniqueServiceContextEngineConfigurationService implements ServiceContextEngineConfigurationService{


		@Override
		public ServiceContextConfigurator getServiceContextConfigurator() {
			return configurator;
		}
		
	}
}
