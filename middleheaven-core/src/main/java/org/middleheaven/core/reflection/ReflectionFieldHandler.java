package org.middleheaven.core.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.middleheaven.util.function.Maybe;


public  class ReflectionFieldHandler implements FieldHandler{

	protected Class<?> type;
	protected String name;
	protected Field field;

	public ReflectionFieldHandler(Class<?> type, String fieldName){
		this.type = type;
		this.name = fieldName;

		load();
	}

	ReflectionFieldHandler(Class<?> type, Field field) {
		this.type = type;
		this.name = field.getName().toLowerCase();
		this.field = field;
	}
	
	protected void load(){


		Field[] fields = type.getDeclaredFields();
		for (Field f : fields){
			if(f.getName().equalsIgnoreCase(name)){
				field = f;
				break;
			}
		}
		if (field==null){
			throw new ReflectionException("Field " + name + " not found");
		}
	}
	
	

	public Object getValue(Object target) throws ReflectionException{
		try {
			field.setAccessible(true);
			return field.get(target);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e);
		}
	}

	public void  setValue(Object target, Object value )  throws ReflectionException{
		try {
			field.set(target,value);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e);
		}
	}

	public String toString(){
		return getDeclaringClass().getName() + "." + this.getName();
	}
	
	public String getName() {
		return name;
	}

	public Class<?> getDeclaringClass(){
		return type;
	}

	public Class<?> getValueType(){
		return field.getType();
	}

	public <A extends Annotation> boolean isAnnotadedWith(Class<A> annotationClass) {
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
}
