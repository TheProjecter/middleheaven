/**
 * 
 */
package org.middleheaven.core.wiring;

import org.middleheaven.reflection.ReflectedClass;

/**
 * 
 */
class ParameterWiringTarget implements WiringTarget {

	
	private Object instance;
	private ReflectedClass<?> type;
	private ReflectedClass<?> declaringType;

	public ParameterWiringTarget (){
		
	}
	/**
	 * Constructor.
	 * @param class1
	 * @param declaringClass
	 * @param object
	 */
	public ParameterWiringTarget(ReflectedClass<?> type, ReflectedClass<?> declaringType,Object instance ) {
		this.instance = instance;
		this.type = type;
		this.declaringType = declaringType;
		
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReflectedClass<?> getType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReflectedClass<?> getDeclaringType() {
		return declaringType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getInstance() {
		return instance;
	}

}
