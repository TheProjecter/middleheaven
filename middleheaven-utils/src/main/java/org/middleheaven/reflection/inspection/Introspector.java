package org.middleheaven.reflection.inspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.middleheaven.reflection.Reflector;
import org.middleheaven.util.Maybe;



/**
 * High level abstraction for handling types metadata.
 */
public abstract class Introspector {

	public static <T> ObjectInstrospector<T> of(T object){
		return new ObjectInstrospector<T>(object);
	}
	

	public static <T> ClassIntrospector<T> of(Class<T> type){
		return new ClassIntrospector<T>(Reflector.getReflector().reflect(type));
	}

	public static PackageIntrospector of(Package typePackage) {
		return typePackage == null ? null : new PackageIntrospector(typePackage);
	}
	
	public static MethodIntrospector of(Method method) {
		return method == null ? null : new MethodIntrospector(method);
	}
	
	public static FieldIntrospector of(Field field) {
		return field == null ? null : new FieldIntrospector(field);
	}
	
	// package
	Introspector(){}
	
	public abstract <A extends Annotation> boolean isAnnotationPresent(Class<A> annotationClass);

	public abstract <A extends Annotation> Maybe<A> getAnnotation(Class<A> annotationClass);
}
