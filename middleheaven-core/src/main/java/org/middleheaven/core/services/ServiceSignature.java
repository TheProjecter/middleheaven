/**
 * 
 */
package org.middleheaven.core.services;

import org.middleheaven.collections.ParamsMap;

/**
 * 
 */
public class ServiceSignature {


	private Class<?> contractInterface;
	private ParamsMap params;



	public ServiceSignature(Class<?> contractInterface, ParamsMap params) {
		super();
		this.contractInterface = contractInterface;
		this.params = params;
	}

	/**
	 * Obtains {@link Class<?>}.
	 * @return the contractInterface
	 */
	public Class<?> getContractInterface() {
		return contractInterface;
	}
	/**
	 * Obtains {@link ParamsMap}.
	 * @return the params
	 */
	public ParamsMap getParams() {
		return params;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public int hashCode(){
		return this.contractInterface.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof ServiceSignature) && equalsServiceSignature((ServiceSignature)obj); 
	}

	
	private boolean equalsServiceSignature(ServiceSignature other) {
		return this.contractInterface.getName().equals(other.contractInterface.getName()) && this.params.containsSame(other.params);
	}

}
