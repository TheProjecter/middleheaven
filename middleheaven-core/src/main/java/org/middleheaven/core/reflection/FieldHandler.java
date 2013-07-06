package org.middleheaven.core.reflection;

import java.lang.annotation.Annotation;

import org.middleheaven.util.function.Maybe;

public interface FieldHandler {

	public Object getValue(Object target) throws ReflectionException;

	public void setValue(Object target, Object value) throws ReflectionException;

	public String getName();

	public Class<?> getDeclaringClass();

	public Class<?> getValueType();

	public <A extends Annotation> boolean isAnnotadedWith(Class<A> annotationClass);

	public <A extends Annotation> Maybe<A> getAnnotation(Class<A> annotationClass);

	public boolean isReadable();
	
	public boolean isWritable();
}
