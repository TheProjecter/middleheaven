/**
 * 
 */
package org.middleheaven.core.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.middleheaven.collections.CollectionUtils;

/**
 * Representation of a service metadata. This class does not represent the service it self or an instance of it, but 
 * the metada to obtain the service instance, and the caracteristics of that instance.
 */
public final class Service {


	private final Map<String, Object> params ;
	private final Class<?> contractInterface;
	private final ServiceActivator activator;

	private boolean activated = false;
	private Collection<ServiceSpecification> dependencies = new ArrayList<ServiceSpecification>(3);

	protected Service(Class<?> contractInterface, ServiceActivator activator, Map<String, Object> params){
		this.contractInterface = contractInterface;
		this.params = params;
		this.activator = activator;

		activator.collectRequiredServicesSpecifications(dependencies);
	}

	public String getName(){
		return getServiceContract().getName();
	}

	public Class<?> getServiceContract(){
		return contractInterface;
	}

	public Map<String, Object> getParams(){
		return params;
	}

	public Collection<ServiceSpecification> getDependencies(){
		return dependencies;
	}

	public void addDependency(ServiceSpecification dependency){
		this.dependencies.add(dependency);
	}

	public String toString(){
		return getName();
	}

	public boolean isActivated(){
		return activated;
	}

	/**
	 * Activate de service. If the service is already activated an  exceptino will be thrown
	 * @param serviceContext
	 */
	public void activate(ServiceContext serviceContext){
		if (this.activated){
			throw new IllegalStateException("Service already activated");
		}

		this.activator.activate(serviceContext);
		this.activated = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode(){
		return contractInterface.getName().hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Service) && equalsService((Service)obj); 
	}


	private boolean equalsService(Service other) {
		return this.contractInterface.getName().equals(other.contractInterface.getName()) && CollectionUtils.equalContents(this.params, other.params);
	}

	/**
	 * @return
	 */
	public ServiceSpecification getServiceSpecification() {
		return ServiceSpecification.forService(this.contractInterface, this.params);
	}
}
