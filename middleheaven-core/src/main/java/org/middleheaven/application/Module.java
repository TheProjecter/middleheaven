/**
 * 
 */
package org.middleheaven.application;

import java.util.ArrayList;
import java.util.Collection;

import org.middleheaven.util.Version;

/**
 * 
 */
public class Module {


	private String name;
	private Version version;
	private ModuleActivator activator;

	private Collection<ModuleDependency> dependencies = new ArrayList<ModuleDependency>(1);
	private boolean activated = false;
	private String applicationId;

	public Module(String applicationId, String name, Version version, ModuleActivator activator) {
		super();
		this.applicationId = applicationId;
		this.name = name;
		this.version = version;
		this.activator = activator;
	}

	
	
	/**
	 * Obtains {@link String}.
	 * @return the applicationId
	 */
	public String getApplicationId() {
		return applicationId;
	}



	public void addDependency(ModuleDependency dependency){
		this.dependencies.add(dependency);
	}
	
	
	public boolean isActivated(){
		return activated;
	}

	/**
	 * Activate de service. If the service is already activated an  exceptino will be thrown
	 * @param applicationContext the current application context
	 */
	public void activate(ApplicationContext applicationContext){
		if (this.activated){
			throw new IllegalStateException("Module already activated");
		}

		try {
			this.activator.start(applicationContext);
			this.activated = true;
		} catch (RuntimeException e){
			throw e;
		}
	}
	
	/**
	 * Inactivate de service. If the service is already inactivated do nothing 
	 * @param applicationContext the current application context
	 */
	public void inactivate(ApplicationContext applicationContext){
		if (!this.activated){
			return;
		}

		try {
			this.activator.stop(applicationContext);
			this.activated = false;
		} catch (RuntimeException e){
			throw e;
		}
	}

	/**
	 * Obtains {@link String}.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Obtains {@link Version}.
	 * @return the version
	 */
	public Version getVersion() {
		return version;
	}

	/**
	 * Obtains {@link ModuleActivator}.
	 * @return the activator
	 */
	public ModuleActivator getActivator() {
		return activator;
	}

	/**
	 * Obtains a collection of {@link ModuleDependency}.
	 * @return the dependencies
	 */
	public Collection<ModuleDependency> getDependencies() {
		return dependencies;
	}

	public int hashCode(){
		return this.name.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Module) && equalsModule((Module)obj); 
	}


	private boolean equalsModule(Module other) {
		return this.name.equals(other.name);
	}

	/**
	 * @return
	 */
	public ModuleVersion getModuleVersion() {
		return new ModuleVersion(name, version);
	}

}
