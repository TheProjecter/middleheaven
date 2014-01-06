/**
 * 
 */
package org.middleheaven.reflection.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.middleheaven.collections.enumerable.AbstractEnumerable;
import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.collections.enumerable.FastCountEnumerable;
import org.middleheaven.reflection.ReflectedParameter;
import org.middleheaven.util.function.BinaryFunction;

/**
 * 
 */
abstract class JavaReflectedParametersEnumerable extends AbstractEnumerable<ReflectedParameter> implements FastCountEnumerable{

	
	public static JavaReflectedParametersEnumerable fromMethod(Method method){
		return new MethodJavaReflectedParametersEnumerable(method);
	}
	
	public static <T> JavaReflectedParametersEnumerable fromConstructor(Constructor<T> constructor){
		return new ConstructorJavaReflectedParametersEnumerable(constructor);
	}
	
	public boolean equals(Object other){
		if (other instanceof JavaReflectedParametersEnumerable){
			return equals((JavaReflectedParametersEnumerable)other);
		} else if (other instanceof Enumerable){
			return other.equals(this);
		} else {
			return false;
		}
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size(){
		return getParameterTypes().length;
	}
	


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<ReflectedParameter> iterator() {
		return Enumerables.asEnumerable(getParameterTypes()).map(new BinaryFunction<ReflectedParameter, Class<?>, Integer>(){

			@Override
			public ReflectedParameter apply(Class<?> obj, Integer index) {
				return new JavaReflectedParameter(JavaReflectedParametersEnumerable.this, index);
			}
			
		}).iterator();
	}

	/**
	 * @return
	 */
	protected abstract Class<?>[] getParameterTypes();
	
	
	protected abstract  boolean equals(JavaReflectedParametersEnumerable other);
	
	/**
	 * @return
	 */
	protected abstract  Annotation[][] getParameterAnnotations();

}
