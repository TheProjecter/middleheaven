/**
 * 
 */
package org.middleheaven.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.middleheaven.core.dependency.DependencyGraph;
import org.middleheaven.core.dependency.DependencyGraphNode;
import org.middleheaven.core.dependency.DependencyInicilizer;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.events.EventListenersSet;
import org.middleheaven.util.Version;

/**
 * 
 */
class ModularApplicationRegistry implements ApplicationRegistry {

	private String applicationId;
	private Version version;
	private ApplicationActivator activator;

	/**
	 * 
	 * Constructor.
	 */
	public ModularApplicationRegistry (String applicationId, Version version, ApplicationActivator activator){
		this.applicationId = applicationId;
		this.version = version;
		this.activator = activator;
	}
	
	
	private static class ModuleInit implements DependencyInicilizer {
		
		private ApplicationContext applicationContext;

		
		public ModuleInit(ApplicationContext applicationContext) {
			super();
			this.applicationContext = applicationContext;
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public void inicialize(DependencyGraphNode node) {
			
			Module m  = (Module)node.getObject();
			
			m.activate(applicationContext);
		}
		
	}

	private ApplicationCycleState state = ApplicationCycleState.STOPED;

	private Deque<ModuleVersion> modulesVersions = new LinkedList<ModuleVersion>();

	private Map<String, ModuleVersion> modulesNamesMapping = new HashMap<String, ModuleVersion>();

	private EventListenersSet<ModuleListener> moduleListeners =  EventListenersSet.newSet(ModuleListener.class);
	private EventListenersSet<ApplicationListener> listeners =  EventListenersSet.newSet(ApplicationListener.class);
	private EventListenersSet<ModuleDiscoveryListener> moduleDiscoveryListeners =  EventListenersSet.newSet(ModuleDiscoveryListener.class);


	private Collection<Module> modules = new ArrayList<Module>(3);


	public void addModule(ModuleVersion module) {

		ModuleVersion currentModule = modulesNamesMapping.get(module.getName());

		if (currentModule==null){
			// new module
			modulesVersions.add(module);
			modulesNamesMapping.put(module.getName(), module);

		} else {

			Version candidate = module.getVersion();
			Version current = currentModule.getVersion();

			// if it is a newer version
			if (candidate.compareTo(current)>0){
				// unload older
				// TODO currentModule.unload(this);

				// install newer
				modulesNamesMapping.put(module.getName(),module);
				modulesVersions.add(module);
			}
		}
	}

	protected boolean setState(ApplicationCycleState phase){
		if ( state.canChangeTo(phase)){
			state = phase;
			listeners.broadcastEvent().onApplicationCycleStateChanged(new ApplicationEvent(phase));
			return true;
		}
		return false;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addModuleListener(ModuleListener listener) {
		this.moduleListeners.addListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeModuleListener(ModuleListener listener) {
		this.moduleListeners.removeListener(listener);
	}
	

	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addApplicationListener(ApplicationListener listener) {
		this.listeners.addListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeApplicationListener(ApplicationListener listener) {
		this.listeners.removeListener(listener);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Version getVersion() {
		return this.version;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getApplicationId() {
		return this.applicationId;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Module> getModules() {
		return Collections.unmodifiableCollection(modules);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isModulePresent(String name) {
		return modulesNamesMapping.containsKey(name);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCompatibleModulePresent(ModuleVersion version) {
		ModuleVersion currentModule = modulesNamesMapping.get(version.getName());

		return currentModule!=null && currentModule.getVersion().compareTo(version.getVersion())>=0;
	}


	/**
	 * 
	 */
	protected void start(final ServiceContext serviceContext) {
		if (setState(ApplicationCycleState.STOPED)){

			setState(ApplicationCycleState.LOADED);
			
			// start modules.
			ApplicationContext appContext = new ApplicationContext(){

				
				@Override
				public ServiceContext getServiceContext() {
					return serviceContext;
				}

				@Override
				public ApplicationRegistry getRegistry() {
					return ModularApplicationRegistry.this;
				}

			};
			
			
			this.activator.init(appContext);
			
			this.activator.beforeModuleStart(appContext);
			
			if (modules.size() == 1){
				// TODO why the graph dont do 1 vertex ?
				modules.iterator().next().activate(appContext);
			} else {
				Map<ModuleVersion , Module> modulesMap = new HashMap<ModuleVersion, Module>();
				
				
				for (Module m : modules){
					modulesMap.put(m.getModuleVersion(), m);
				}

				// activate modules in the correct order.
				DependencyGraph graph = new DependencyGraph();

				ModuleInit init = new ModuleInit(appContext);
				
				for (Module m : modules){
					for (ModuleDependency d : m.getDependencies()){
						graph.addDependency(
								new DependencyGraphNode(modulesMap.get(d.getDependencyModuleVersion()), init),
								new DependencyGraphNode(m, init),
								d.isRequired()
						);
					}
				}
				

				graph.resolve();
			}

			this.activator.afterModuleStart(appContext);
			
			setState(ApplicationCycleState.READY);

		}
	}
	
	protected void stop(final ServiceContext serviceContext) {
		setState(ApplicationCycleState.PAUSED);

		// start modules.
		ApplicationContext appContext = new ApplicationContext(){

			
			@Override
			public ServiceContext getServiceContext() {
				return serviceContext;
			}

			@Override
			public ApplicationRegistry getRegistry() {
				return ModularApplicationRegistry.this;
			}

		};
		
		
		this.activator.beforeModuleStop(appContext);
		
		// stop modules.
		for (Module m : modules){
			
			m.inactivate(appContext);
		}
		
		this.activator.afterModuleStop(appContext);
		
		this.activator.stop(appContext);
		
		
		setState(ApplicationCycleState.STOPED);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addModule(Module module) {
		if (!module.getApplicationId().equals(this.applicationId)){
			throw new IllegalArgumentException("This module does not belong on this application ");
		}
		this.modules.add(module);
		this.moduleDiscoveryListeners.broadcastEvent().onModuleDiscovered(new ModuleDiscoveryEvent(module.getModuleVersion()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addModuleDiscoveryListener(ModuleDiscoveryListener listener) {
		this.moduleDiscoveryListeners.addListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeModuleDiscoveryListener(ModuleDiscoveryListener listener) {
		this.moduleDiscoveryListeners.removeListener(listener);
	}



	
}
