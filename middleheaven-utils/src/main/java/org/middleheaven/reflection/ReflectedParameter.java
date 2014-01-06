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
public interface ReflectedParameter {

	
	/**
	 * @return
	 */
	public ReflectedClass<?> getType();

	public int getIndex();
	
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
	
	
	public Enumerable<Annotation> getAnnotations();
}
