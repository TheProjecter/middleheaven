/**
 * 
 */
package org.middleheaven.core.wiring;

import org.middleheaven.collections.enumerable.Enumerable;


/**
 * 
 */
public abstract class AbstractProducingWiringPoint implements ProducingWiringPoint {

	
	private WiringSpecification methodSpecification;
	private Enumerable<WiringSpecification> paramsSpecifications;
	
	
	protected AbstractProducingWiringPoint (WiringSpecification methodSpecification,Enumerable<WiringSpecification> paramsSpecifications){
		this.methodSpecification = methodSpecification;
		this.paramsSpecifications = paramsSpecifications;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public WiringSpecification getSpecification() {
		return methodSpecification;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<WiringSpecification> getParamsSpecifications() {
		return paramsSpecifications;
	}
	



}
