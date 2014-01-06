/**
 * 
 */
package org.middleheaven.reflection.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.ReflectedConstructor;
import org.middleheaven.reflection.ReflectedParameter;
import org.middleheaven.reflection.ReflectionException;
import org.middleheaven.util.Maybe;

/**
 * 
 */
public class JavaReflectedConstructor<T> implements ReflectedConstructor<T> {

	private Constructor<T> constructor;

	/**
	 * Constructor.
	 * @param constructor
	 */
	public JavaReflectedConstructor(Constructor<T> constructor) {
		this.constructor = constructor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return constructor.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getModifiers() {
		return constructor.getModifiers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReflectedClass<T> getDeclaringClass() {
		return JavaReflectedClass.valueOf(constructor.getDeclaringClass());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T newInstance(Object... args) throws ReflectionException {
		try {
			return constructor.newInstance(args);
		} catch (IllegalArgumentException e) {
			throw ReflectionException.manage(e);
		} catch (InstantiationException e) {
			throw ReflectionException.manage(e);
		} catch (IllegalAccessException e) {
			throw ReflectionException.manage(e);
		} catch (InvocationTargetException e) {
			throw ReflectionException.manage(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<ReflectedParameter> getParameters() {
		return JavaReflectedParametersEnumerable.fromConstructor(constructor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> type) {
		return constructor.isAnnotationPresent(type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <A extends Annotation> Maybe<A> getAnnotation(Class<A> type) {
		return Maybe.of(constructor.getAnnotation(type));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<Annotation> getAnnotations() {
		return Enumerables.asEnumerable(constructor.getAnnotations());
	}

	public boolean equals(Object other){
		return other instanceof JavaReflectedConstructor && equalsOther((JavaReflectedConstructor) other);
	}

	/**
	 * @param other
	 * @return
	 */
	private boolean equalsOther(JavaReflectedConstructor other) {
		return this.constructor.equals(other.constructor);
	}
	
	public int hashCode(){
		return this.constructor.hashCode();
	}

	public String toString(){
		return this.constructor.toString();
	}

}
