/**
 * 
 */
package org.middleheaven.reflection;

import java.lang.annotation.Annotation;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.util.Maybe;

/**
 * 
 */
public interface ReflectedConstructor<T> {

	/**
	 * @return
	 */
	public String getName();

	/**
	 * @return
	 */
	public int getModifiers();

	/**
	 * @return
	 */
	public ReflectedClass<T> getDeclaringClass();

	/**
	 * 
	 * @param obj
	 * @param args
	 * @return
	 * @throws ReflectionException
	 */
	public T newInstance(Object ... args) throws ReflectionException;

	/**
	 * 
	 * @return
	 */
	public Enumerable<ReflectedParameter> getParameters();

	
	/**
	 * @param class1
	 * @return
	 */
	public boolean isAnnotationPresent(Class<? extends Annotation> type);

	/**
	 * @param class1
	 * @return
	 */
	public <A extends Annotation> Maybe<A> getAnnotation(Class<A> type);

	/**
	 * @return
	 */
	public Enumerable<Annotation> getAnnotations();
}
