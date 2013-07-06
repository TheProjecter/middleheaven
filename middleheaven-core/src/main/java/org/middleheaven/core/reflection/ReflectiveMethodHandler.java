/**
 * 
 */
package org.middleheaven.core.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.Enumerable;
import org.middleheaven.util.function.Maybe;


/**
 * 
 */
public class ReflectiveMethodHandler implements MethodHandler {


	private Method method;

	public ReflectiveMethodHandler (Method method){
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
	public Class<?> getDeclaringClass() {
		return method.getDeclaringClass();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object invoke(Object obj, Object... args) {
		try{
			return method.invoke(obj, args);
		} catch (IllegalArgumentException e) {
			throw ReflectionException.manage(e, method.getDeclaringClass());
		} catch (IllegalAccessException e) {
			throw ReflectionException.manage(e, method.getDeclaringClass());
		} catch (InvocationTargetException e) {
			throw ReflectionException.manage(e, method.getDeclaringClass());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?>[] getParameterTypes() {
		return method.getParameterTypes();
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
	public Class<?> getReturnType() {
		return method.getReturnType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Annotation[][] getParameterAnnotations() {
		return method.getParameterAnnotations();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<Annotation> getAnnotations() {
		return CollectionUtils.asEnumerable(method.getAnnotations());
	}
	
	public boolean equals(Object other){
		return other instanceof ReflectiveMethodHandler && equalsOther((ReflectiveMethodHandler) other);
	}

	/**
	 * @param other
	 * @return
	 */
	private boolean equalsOther(ReflectiveMethodHandler other) {
		return this.method.equals(other.method);
	}
	
	public int hashCode(){
		return this.method.hashCode();
	}

	public String toString(){
		return this.method.toString();
	}
}
