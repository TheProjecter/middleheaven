/**
 * 
 */
package org.middleheaven.reflection.inspection;

import org.middleheaven.reflection.InstantiationReflectionException;
import org.middleheaven.reflection.ReflectedClass;

/**
 * 
 */
public interface InstrospectionStrategy {

	public <T> ReflectedClass<T> reflect(Class<T> type);

	/**
	 * @param className
	 * @return
	 */
	public ReflectedClass<?> loadClass(String className) throws InstantiationReflectionException;
	
	public <S> ReflectedClass<S> loadClass(String className, ReflectedClass<S> superType) throws InstantiationReflectionException;

}
