package org.middleheaven.reflection.inspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.ReflectionException;
import org.middleheaven.reflection.Reflector;
import org.middleheaven.util.Maybe;

public class FieldIntrospector extends Introspector  {

	private Field field;

	FieldIntrospector(Field field) {
		this.field = field;
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

	public void setValue(Object target, Object value )  throws ReflectionException{
		try {
			field.set(target,value);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e);
		}
	}

	public String toString(){
		return this.getName();
	}
	
	public String getName() {
		return field.getName();
	}

	public ReflectedClass<?> getDeclaringClass(){
		return Reflector.getReflector().reflect(field.getDeclaringClass());
	}

	public ReflectedClass<?> getValueType(){
		return  Reflector.getReflector().reflect(field.getType());
	}

	public <A extends Annotation> boolean isAnnotationPresent(Class<A> annotationClass) {
		return field.isAnnotationPresent(annotationClass);
	}

	public <A extends Annotation> Maybe<A> getAnnotation(Class<A> annotationClass) {
		return Maybe.of(field.getAnnotation(annotationClass));
	}

	public Enumerable<Annotation> getAnnotatedAnnotations(Class<? extends Annotation>  metaAnnotation) {
		Set<Annotation> result = new HashSet<Annotation>(); 

		Annotation[] all = field.getDeclaredAnnotations();
		for (Annotation a : all){
			if (a.annotationType().isAnnotationPresent(metaAnnotation)){
				result.add(a);
			}
		}
		
		return Enumerables.asEnumerable(result);
	}


}
