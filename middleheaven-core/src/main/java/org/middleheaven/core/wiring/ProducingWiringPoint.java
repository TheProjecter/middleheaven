/**
 * 
 */
package org.middleheaven.core.wiring;

import org.middleheaven.collections.enumerable.Enumerable;

/**
 * A wiring point that produces or obtains an object.
 */
public interface ProducingWiringPoint extends WiringPoint {
	
	public ProducingWiringPoint merge(ProducingWiringPoint other);
	
	public WiringSpecification getSpecification();

	public Enumerable<WiringSpecification> getParamsSpecifications();

	/**
	 * Execute the production, creating a new object
	 * @param binder
	 * @return
	 */
	public <T> T produceWith(InstanceFactory factory);
}
