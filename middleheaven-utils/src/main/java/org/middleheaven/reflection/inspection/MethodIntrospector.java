package org.middleheaven.reflection.inspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.reflection.IllegalAccessReflectionException;
import org.middleheaven.reflection.InvocationTargetReflectionException;
import org.middleheaven.util.Maybe;

public class MethodIntrospector extends Introspector{

	private Method method;

	MethodIntrospector(Method method) {
		this.method = method;
	}

	public String getName(){
		return method.getName();
	}
	
	public ClassIntrospector<?> getDeclaringClass(){
		return Introspector.of(method.getDeclaringClass());
	}

	@Override
	public <A extends Annotation> Maybe<A> getAnnotation(Class<A> annotationClass) {
		return Maybe.of(method.getAnnotation(annotationClass));
	}

	@Override
	public <A extends Annotation> boolean isAnnotationPresent(
			Class<A> annotationClass) {
		return method.isAnnotationPresent(annotationClass);
	}
	
	public boolean isBridge(){
		return method.isBridge();
	}
	
	public Enumerable<Annotation> getAnnotatedAnnotations(Class<? extends Annotation>  metaAnnotation) {
		Set<Annotation> result = new HashSet<Annotation>(); 

		Annotation[] all = method.getDeclaredAnnotations();
		for (Annotation a : all){
			if (a.annotationType().isAnnotationPresent(metaAnnotation)){
				result.add(a);
			}
		}
		
		return Enumerables.asEnumerable(result);
	}
	
	public <R> R invoke(Class<R> returnType,Object target, Object ... params) {
		try {
			this.method.setAccessible(true);
			Object obj = this.method.invoke(target, params);
			if (obj != null && !Void.class.isInstance(returnType)){
				return returnType.cast(obj);
			} else {
				return null;
			}
		} catch (SecurityException e) {
			throw new IllegalAccessReflectionException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalAccessReflectionException(e);
		} catch (InvocationTargetException e) {
			throw new InvocationTargetReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalAccessReflectionException(e);
		}
	}

} 
