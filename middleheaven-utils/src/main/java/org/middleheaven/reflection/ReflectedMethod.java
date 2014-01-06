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
public interface ReflectedMethod {

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
	public ReflectedClass<?> getDeclaringClass();

	/**
	 * @param obj
	 * @param objects
	 * @return
	 */
	public Object invoke(Object obj, Object ... args) throws ReflectionException;
	
	/**
	 * 
	 * @param obj
	 * @param args
	 * @return
	 * @throws ReflectionException
	 */
	public Object invokeStatic(Object ... args) throws ReflectionException;

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
	public ReflectedClass<?> getReturnType();

	/**
	 * @return
	 */
	public Enumerable<Annotation> getAnnotations();

	/**
	 * @return
	 */
	public boolean isStatic();


}
