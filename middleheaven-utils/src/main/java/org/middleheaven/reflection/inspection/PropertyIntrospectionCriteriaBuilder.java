package org.middleheaven.reflection.inspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.ReflectedProperty;
import org.middleheaven.reflection.Reflector;

public class PropertyIntrospectionCriteriaBuilder<T> extends MemberIntrospectionCriteriaBuilder<T,ReflectedProperty>{

	public PropertyIntrospectionCriteriaBuilder(IntrospectionCriteriaBuilder<T> builder) {
		super(builder);
	}

	public PropertyIntrospectionCriteriaBuilder<T> named(String propertyName) {
		super.addNameFilter(propertyName);
		return this;
	}


	@Override
	protected Enumerable<ReflectedProperty> getAllMembersInType(ReflectedClass<T> type) {
		return type.getProperties();
	}

	@Override
	protected int getModifiers(ReflectedProperty obj) {
		return Modifier.PUBLIC;
	}

	@Override
	protected String getName(ReflectedProperty obj) {
		return obj.getName();
	}

	@Override
	protected boolean isAnnotationPresent(ReflectedProperty obj, Class<? extends Annotation> annotationClass) {
		return obj.isAnnotationPresent(annotationClass);
	}
}
