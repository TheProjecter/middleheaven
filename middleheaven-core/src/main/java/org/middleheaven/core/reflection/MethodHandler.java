/**
 * 
 */
package org.middleheaven.core.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;

import org.middleheaven.collections.Enumerable;
import org.middleheaven.core.annotations.Publish;
import org.middleheaven.util.function.Maybe;

/**
 * 
 */
public interface MethodHandler {

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
	public Class<?> getDeclaringClass();

	/**
	 * @param obj
	 * @param objects
	 * @return
	 */
	public Object invoke(Object obj, Object ... args);

	/**
	 * @return
	 */
	public Class<?>[] getParameterTypes();

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
	public Class<?> getReturnType();

	/**
	 * @return
	 */
	public Annotation[][] getParameterAnnotations();

	/**
	 * @return
	 */
	public Enumerable<Annotation> getAnnotations();

}
