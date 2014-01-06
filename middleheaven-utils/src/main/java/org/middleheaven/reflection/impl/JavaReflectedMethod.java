/**
 * 
 */
package org.middleheaven.reflection.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.ReflectedMethod;
import org.middleheaven.reflection.ReflectedParameter;
import org.middleheaven.reflection.ReflectionException;
import org.middleheaven.util.Maybe;


/**
 * 
 */
public class JavaReflectedMethod implements ReflectedMethod {

	private Method method;

	public JavaReflectedMethod (Method method){
		this.method = method;
		method.setAccessible(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return method.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getModifiers() {
		return method.getModifiers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReflectedClass<?> getDeclaringClass() {
		return JavaReflectedClass.valueOf(method.getDeclaringClass());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReflectedClass<?> getReturnType() {
		return JavaReflectedClass.valueOf(method.getReturnType());
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object invoke(Object obj, Object... args) {
		if (obj == null){
			throw new IllegalArgumentException("Target obj is null. If you are trying to make a static invocation use the 'invokeStatic' method");
		}
		try{
			return method.invoke(obj, args);
		} catch (IllegalArgumentException e) {
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
	public Object invokeStatic(Object... args)
			throws ReflectionException {
		try{
			return method.invoke(null, args);
		} catch (IllegalArgumentException e) {
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
	public boolean isAnnotationPresent(Class<? extends Annotation> type) {
		return method.isAnnotationPresent(type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <A extends Annotation> Maybe<A> getAnnotation(Class<A> type) {
		return Maybe.of(method.getAnnotation(type));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<ReflectedParameter> getParameters() {
		return JavaReflectedParametersEnumerable.fromMethod(method);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<Annotation> getAnnotations() {
		return Enumerables.asEnumerable(method.getAnnotations());
	}
	
	public boolean equals(Object other){
		return other instanceof JavaReflectedMethod && equalsOther((JavaReflectedMethod) other);
	}

	/**
	 * @param other
	 * @return
	 */
	private boolean equalsOther(JavaReflectedMethod other) {
		return this.method.equals(other.method);
	}
	
	public int hashCode(){
		return this.method.hashCode();
	}

	public String toString(){
		return this.method.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isStatic() {
		return Modifier.isStatic(method.getModifiers());
	}




}
