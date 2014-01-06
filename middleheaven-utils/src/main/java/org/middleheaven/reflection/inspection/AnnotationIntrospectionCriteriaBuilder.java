package org.middleheaven.reflection.inspection;

import java.lang.annotation.Annotation;

import org.middleheaven.collections.enumerable.Enumerable;

public class AnnotationIntrospectionCriteriaBuilder<T> {

	private IntrospectionCriteriaBuilder<T> introspectionCriteriaBuilder;

	public AnnotationIntrospectionCriteriaBuilder(IntrospectionCriteriaBuilder<T> introspectionCriteriaBuilder) {
		this.introspectionCriteriaBuilder = introspectionCriteriaBuilder;
	}
	
	public Enumerable<Annotation> retrive(){
		return introspectionCriteriaBuilder.type.getAnnotations();
	}

}
