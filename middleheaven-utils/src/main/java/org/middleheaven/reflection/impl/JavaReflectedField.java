package org.middleheaven.reflection.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.ReflectedField;
import org.middleheaven.reflection.ReflectionException;
import org.middleheaven.util.Maybe;


public class JavaReflectedField implements ReflectedField{

	protected String name;
	protected Field field;
	
	public static JavaReflectedField forTypeAndField(ReflectedClass<?> type, String fieldName){
		
		Field[] fields = type.getReflectedType().getDeclaredFields();
		for (Field f : fields){
			if(f.getName().equalsIgnoreCase(fieldName)){
				 return new JavaReflectedField(f);
			}
		}
		
		throw new ReflectionException("Field " + fieldName + " not found");
		
	}
	
    protected JavaReflectedField(Field field) {
		this.field = field;
		this.name = field.getName().toLowerCase();
		
	}

	public Object getValue(Object target) throws ReflectionException{
		try {
			field.setAccessible(true);
			return field.get(target);
		} catch (IllegalArgumentException e) {
			throw ReflectionException.manage(e);
		} catch (IllegalAccessException e) {
			throw ReflectionException.manage(e);
		}
	}

	public void  setValue(Object target, Object value )  throws ReflectionException{
		try {
			field.set(target,value);
		} catch (IllegalArgumentException e) {
			throw ReflectionException.manage(e);
		} catch (IllegalAccessException e) {
			throw ReflectionException.manage(e);
		}
	}

	public String toString(){
		return getDeclaringClass().getName() + "." + this.getName();
	}
	
	public String getName() {
		return name;
	}

	public ReflectedClass<?> getDeclaringClass(){
		return JavaReflectedClass.valueOf(field.getDeclaringClass());
	}

	public ReflectedClass<?> getValueType(){
		return JavaReflectedClass.valueOf(field.getType());
	}

	public <A extends Annotation> boolean isAnnotationPresent(Class<A> annotationClass) {
		return (field!=null && field.isAnnotationPresent(annotationClass));
	}

	public <A extends Annotation> Maybe<A> getAnnotation(Class<A> annotationClass) {
		return Maybe.of(field.getAnnotation(annotationClass));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReadable() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWritable() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(Object target, Object value) {
		field.setAccessible(true);
		try {
			field.set(target, value);
		} catch (IllegalArgumentException e) {
			throw ReflectionException.manage(e);
		} catch (IllegalAccessException e) {
			throw ReflectionException.manage(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object get(Object target) {
		field.setAccessible(true);
		try {
			return field.get(target);
		} catch (IllegalArgumentException e) {
			throw ReflectionException.manage(e);
		} catch (IllegalAccessException e) {
			throw ReflectionException.manage(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<Annotation> getAnnotations() {
		return Enumerables.asEnumerable(field.getAnnotations());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getModifiers() {
		return field.getModifiers();
	}
}
