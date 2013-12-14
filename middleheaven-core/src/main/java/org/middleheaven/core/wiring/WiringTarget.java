/**
 * 
 */
package org.middleheaven.core.wiring;

import org.middleheaven.reflection.ReflectedClass;


/**
 * Represents the site where the injection will be made.
 * 
 * this can represent a field,  or a parameter of a method or constructor
 */
public interface WiringTarget {

	/**
	 * The type of the target. In the case of a field this retrieves the field type,
	 * in case of a parameter, returns the type of the parameter. 
	 * 
	 * @return the type of the target
	 */
	public ReflectedClass<?> getType();
	
	/**
	 * Returns the {@link Class} of the type where the wiring target is defined.  
	 * 
	 * @return the {@link Class} of the type where the wiring target is defined.  
	 */
	public ReflectedClass<?> getDeclaringType();
	
	/**
	 * The instance of the object where the wiring target is defined.
	 * If the target corresponds to a constructor parameter, this method will return <code>null</code>.
	 * 
	 * @return the instance of the object where the wiring target is defined, or <code>null</code> if none is defined.
	 */
	public Object getInstance(); 

	
}
