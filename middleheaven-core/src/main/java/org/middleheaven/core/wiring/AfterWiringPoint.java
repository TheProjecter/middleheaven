package org.middleheaven.core.wiring;

import java.util.List;

/**
 * After creation wiring point. The framework will use this Wiring point to bind other objects.
 */
public interface AfterWiringPoint extends WiringPoint {

	public List<WiringSpecification> getSpecifications();
		
	/**
	 * Indicates if this wiring is required. An exception will be thrown by the Wiring service if the wiring cannot be satisfied.
	 * @return <code>true</code> if this wiring is required. 
	 */
	public boolean isRequired();
	
	/**
	 * Executes the wiring on the <code>object</code>.
	 * 
	 * @param <T> the class of the object that will be injected with this {@link AfterWiringPoint}.
	 * @param instanceFactory the binder being used.
	 * @param instance the object that will be injected with this {@link AfterWiringPoint}.
	 * @return the object after wiring.
	 */
	public <T> T writeAtPoint(InstanceFactory instanceFactory, T instance);
	
}
