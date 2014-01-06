package org.middleheaven.reflection;

import java.lang.annotation.Annotation;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.util.Maybe;

public interface ReflectedField {

	public Object getValue(Object target) throws ReflectionException;

	public void setValue(Object target, Object value) throws ReflectionException;

	public String getName();

	public ReflectedClass<?> getDeclaringClass();

	public ReflectedClass<?> getValueType();

	public <A extends Annotation> boolean isAnnotationPresent(Class<A> annotationClass);

	public <A extends Annotation> Maybe<A> getAnnotation(Class<A> annotationClass);

	public boolean isReadable();
	
	public boolean isWritable();

	/**
	 * @param target
	 * @param value
	 */
	public void set(Object target, Object value);

	/**
	 * @param target
	 * @return
	 */
	public Object get(Object target);

	/**
	 * @return
	 */
	public Enumerable<Annotation> getAnnotations();

	/**
	 * @return
	 */
	public int getModifiers();
}
