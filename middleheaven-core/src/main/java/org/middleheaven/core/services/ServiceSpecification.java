/**
 * 
 */
package org.middleheaven.core.services;

import java.util.Collections;
import java.util.Map;

import org.middleheaven.collections.CollectionUtils;

/**
 * 
 */
public final class ServiceSpecification {

	private Class<?> serviceContractType;
	private Map<String, Object> parameters;
	private boolean optional;

	
	
	
	
	public static ServiceSpecification forService(Class<?> serviceContractType){
		return new ServiceSpecification(serviceContractType, Collections.<String,Object>emptyMap(), false);
	}

	public static ServiceSpecification forService(Class<?> serviceContractType, boolean isOptional){
		return new ServiceSpecification(serviceContractType, Collections.<String,Object>emptyMap(), isOptional);
	}

	public static ServiceSpecification forService(Class<?> serviceContractType, Map<String, Object> parameters){
		return new ServiceSpecification(serviceContractType, parameters, false);
	}

	private ServiceSpecification(Class<?> serviceContractType, Map<String, Object> parameters, boolean isOptional){
		this.serviceContractType = serviceContractType;
		this.parameters = parameters;
		this.optional = isOptional;
	}

	/**
	 * Obtains {@link Class<?>}.
	 * @return the serviceContractType
	 */
	public Class<?> getServiceContractType() {
		return serviceContractType;
	}

	/**
	 * Obtains {@link Map<String,Object>}.
	 * @return the parameters
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	/**
	 * Obtains {@link boolean}.
	 * @return the optional
	 */
	public boolean isOptional() {
		return optional;
	}

	public int hashCode(){
		return this.serviceContractType.getName().hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof ServiceSpecification) && equalsServiceSpecification((ServiceSpecification)obj); 
	}


	private boolean equalsServiceSpecification(ServiceSpecification other) {
		return this.serviceContractType.getName().equals(other.serviceContractType.getName()) 
				&& CollectionUtils.equalContents(this.parameters , other.parameters);
	}

	public boolean matches (ServiceSpecification s){
		if (this.parameters.size() < s.parameters.size()){
			return false;
		}

		int count=0;
		for (Map.Entry<String,? extends Object> entry : s.parameters.entrySet() ){
			if (entry.getValue()!=null && entry.getValue().equals(this.parameters.get(entry.getKey()))){
				count++;
			}
		}

		return count > 0;
	}

}
