package org.middleheaven.core.reflection.inspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;

import org.middleheaven.collections.Enumerable;
import org.middleheaven.core.reflection.PropertyHandler;

public class PropertyIntrospectionCriteriaBuilder<T> extends MemberIntrospectionCriteriaBuilder<T,PropertyHandler>{

	public PropertyIntrospectionCriteriaBuilder(IntrospectionCriteriaBuilder<T> builder) {
		super(builder);
	}

	public PropertyIntrospectionCriteriaBuilder<T> named(String propertyName) {
		super.addNameFilter(propertyName);
		return this;
	}


	@Override
	protected Enumerable<PropertyHandler> getAllMembersInType(Class<T> type) {
		return Reflector.getReflector().getPropertyAccessors(type, true);
	}

	@Override
	protected int getModifiers(PropertyHandler obj) {
		return Modifier.PUBLIC;
	}

	@Override
	protected String getName(PropertyHandler obj) {
		return obj.getName();
	}

	@Override
	protected boolean isAnnotationPresent(PropertyHandler obj, Class<? extends Annotation> annotationClass) {
		return obj.isAnnotadedWith(annotationClass);
	}
}
