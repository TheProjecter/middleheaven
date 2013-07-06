package org.middleheaven.core.reflection.inspection;

import java.lang.annotation.Annotation;

import org.middleheaven.collections.Enumerable;

public class AnnotationIntrospectionCriteriaBuilder<T> {

	private IntrospectionCriteriaBuilder<T> introspectionCriteriaBuilder;

	public AnnotationIntrospectionCriteriaBuilder(IntrospectionCriteriaBuilder<T> introspectionCriteriaBuilder) {
		this.introspectionCriteriaBuilder = introspectionCriteriaBuilder;
	}
	
	public Enumerable<Annotation> retrive(){
		return Reflector.getReflector().getAnnotations(introspectionCriteriaBuilder.type);
	}

}
