/**
 * 
 */
package org.middleheaven.reflection.impl;

import org.middleheaven.reflection.ClassCastReflectionException;
import org.middleheaven.reflection.InstantiationReflectionException;
import org.middleheaven.reflection.NoSuchClassReflectionException;
import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.inspection.InstrospectionStrategy;

/**
 * 
 */
public class JavaInstrospectionStrategy implements InstrospectionStrategy {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> ReflectedClass<T> reflect(Class<T> type) {
		return JavaReflectedClass.valueOf(type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReflectedClass<?> loadClass(String className)throws InstantiationReflectionException {
		try {
			return this.reflect(Class.forName(className));
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <S> ReflectedClass<S> loadClass(String className, ReflectedClass<S> superType)throws InstantiationReflectionException {

		try{
			return (ReflectedClass<S>) this.reflect(Class.forName(className).asSubclass(superType.getReflectedType()));
		} catch (ClassCastException e){
			throw new ClassCastReflectionException(className, superType.getName());
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	}
	
	
}
