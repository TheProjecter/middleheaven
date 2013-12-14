/**
 * 
 */
package org.middleheaven.core.wiring;

import org.middleheaven.reflection.ReflectedMethod;


/**
 * 
 */
public class MethodCallPoint implements PostCreatePoint, PreDestroiPoint {

	
	private ReflectedMethod method;
	
	public MethodCallPoint (ReflectedMethod method){
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
