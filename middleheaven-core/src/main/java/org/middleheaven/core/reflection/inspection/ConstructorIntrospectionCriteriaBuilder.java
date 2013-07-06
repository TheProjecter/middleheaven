package org.middleheaven.core.reflection.inspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.Enumerable;
import org.middleheaven.core.reflection.MemberAccess;
import org.middleheaven.util.function.Predicate;

public class ConstructorIntrospectionCriteriaBuilder<T> extends ParameterizableMemberIntrospectionCriteriaBuilder<T,Constructor<T>>{

	ConstructorIntrospectionCriteriaBuilder(IntrospectionCriteriaBuilder<T> builder){
		super(builder);
	}

	public ConstructorIntrospectionCriteriaBuilder<T> annotathedWith(final Class<? extends Annotation> ... annotations) {

		super.addAnnotatedWithFilter(annotations);
		return this;
	}

	public ConstructorIntrospectionCriteriaBuilder<T> withAccess(final MemberAccess ... acesses){

		super.addWithAccessFilter(acesses);
		return this;
		
	}

	public ConstructorIntrospectionCriteriaBuilder<T> sortedByQuantityOfParameters(){
		
		super.addSortingByQuantityOfParameters();
		return this;

	}

	public ConstructorIntrospectionCriteriaBuilder<T> named(final String name) {
		super.addNameFilter(name);
		return this;
	}
	
	public ConstructorIntrospectionCriteriaBuilder<T> match(Predicate<Constructor<T>> filter) {
		add(filter);
		return this;
	}
	
	@Override
	protected Enumerable<Constructor<T>> getAllMembersInType(Class<T> type) {
		// the constructors are directly taken from type T
		@SuppressWarnings("unchecked") Constructor<T>[] constructors = (Constructor<T>[]) type.getConstructors();
		return CollectionUtils.asEnumerable(constructors);
	}

	@Override
	protected int getModifiers(Constructor<T> obj) {
		return obj.getModifiers();
	}

	@Override
	protected boolean isAnnotationPresent(Constructor<T> obj,
			Class<? extends Annotation> annotationType) {
		return obj.isAnnotationPresent(annotationType);
	}

	@Override
	protected int getParameterCount(Constructor<T> member) {
		return member.getTypeParameters().length;
	}

	@Override
	protected String getName(Constructor<T> obj) {
		return obj.getName();
	}

	@Override
	protected boolean hasParameterTypes(Constructor<T> obj, Class<?>[] parameterTypes) {
		return Arrays.equals(obj.getParameterTypes(), parameterTypes);
	}

}
