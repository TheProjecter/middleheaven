/**
 * 
 */
package org.middleheaven.reflection.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 
 */
class MethodJavaReflectedParametersEnumerable extends JavaReflectedParametersEnumerable{

	
	private Method method;

	public MethodJavaReflectedParametersEnumerable(Method method){
		this.method = method;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<?>[] getParameterTypes() {
		return method.getParameterTypes();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean equals(JavaReflectedParametersEnumerable other) {
		 return other instanceof MethodJavaReflectedParametersEnumerable && 
				 Arrays.equals(((MethodJavaReflectedParametersEnumerable)other).method.getParameterTypes(), this.method.getParameterTypes());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Annotation[][] getParameterAnnotations() {
		return method.getParameterAnnotations();
	}

}
