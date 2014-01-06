/**
 * 
 */
package org.middleheaven.reflection.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

/**
 * 
 */
class ConstructorJavaReflectedParametersEnumerable extends JavaReflectedParametersEnumerable{

	
	@SuppressWarnings("rawtypes")
	private Constructor constructor;

	@SuppressWarnings("rawtypes")
	public ConstructorJavaReflectedParametersEnumerable(Constructor constructor){
		this.constructor = constructor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<?>[] getParameterTypes() {
		return constructor.getParameterTypes();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean equals(JavaReflectedParametersEnumerable other) {
		 return other instanceof ConstructorJavaReflectedParametersEnumerable && ((ConstructorJavaReflectedParametersEnumerable)other).constructor.equals(this.constructor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Annotation[][] getParameterAnnotations() {
		return constructor.getParameterAnnotations();
	}

}
