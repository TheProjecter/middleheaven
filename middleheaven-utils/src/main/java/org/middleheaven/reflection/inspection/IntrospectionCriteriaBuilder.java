package org.middleheaven.reflection.inspection;

import java.lang.reflect.Modifier;

import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.ReflectedMethod;
import org.middleheaven.util.function.Predicate;


public class IntrospectionCriteriaBuilder<T> {

	ReflectedClass<T> type;
	
	public IntrospectionCriteriaBuilder(ReflectedClass<T> type){
		this.type = type;
	}
	
	public ConstructorIntrospectionCriteriaBuilder<T> constructors(){
		return new ConstructorIntrospectionCriteriaBuilder<T>(this);
	}
	
	public MethodIntrospectionCriteriaBuilder<T> methods(){
		return new MethodIntrospectionCriteriaBuilder<T>(this);
	}

	public MethodIntrospectionCriteriaBuilder<T> staticMethods() {
		return new MethodIntrospectionCriteriaBuilder<T>(this)
		.match(new StaticMethods());
	}

	public AnnotationIntrospectionCriteriaBuilder<T> annotations() {
		return new AnnotationIntrospectionCriteriaBuilder<T>(this);
	}

	public FieldIntrospectionCriteriaBuilder<T> fields() {
		return new FieldIntrospectionCriteriaBuilder<T>(this);
	}

	public PropertyIntrospectionCriteriaBuilder<T> properties() {
		return new PropertyIntrospectionCriteriaBuilder<T>(this);
	}
	
	private static class StaticMethods implements Predicate<ReflectedMethod> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean apply(ReflectedMethod m) {
			return Boolean.valueOf(Modifier.isStatic( m.getModifiers()));
		}
		
	}

}
