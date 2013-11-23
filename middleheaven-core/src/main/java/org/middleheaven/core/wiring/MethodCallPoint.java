/**
 * 
 */
package org.middleheaven.core.wiring;

import org.middleheaven.core.reflection.MethodHandler;


/**
 * 
 */
public class MethodCallPoint implements PostCreatePoint, PreDestroiPoint {

	
	private MethodHandler method;
	
	public MethodCallPoint (MethodHandler method){
		this.method = method;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void call(Object target) {
		this.method.invoke(target);
	}

}
