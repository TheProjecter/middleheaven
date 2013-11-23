/**
 * 
 */
package org.middleheaven.core.wiring;

import org.middleheaven.util.Maybe;


/**
 * 
 */
public interface InstanceFactory {

	
	public Object getInstance(WiringQuery query);

	/**
	 * @param specification
	 * @return
	 */
	public Maybe<Object> peekCyclickProxy(Class<?> contract);
}
