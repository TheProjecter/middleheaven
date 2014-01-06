package org.middleheaven.reflection.inspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.middleheaven.collections.Pair;
import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.reflection.MemberAccess;
import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.ReflectedConstructor;
import org.middleheaven.reflection.ReflectedParameter;
import org.middleheaven.util.function.Predicate;

public class ConstructorIntrospectionCriteriaBuilder<T> extends ParameterizableMemberIntrospectionCriteriaBuilder<T,ReflectedConstructor<T>>{

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
	
	public ConstructorIntrospectionCriteriaBuilder<T> match(Predicate<ReflectedConstructor<T>> filter) {
		add(filter);
		return this;
	}
	
	@Override
	protected Enumerable<ReflectedConstructor<T>> getAllMembersInType(ReflectedClass<T> type) {
		return type.getConstructors();
	}

	@Override
	protected int getModifiers(ReflectedConstructor<T> obj) {
		return obj.getModifiers();
	}

	@Override
	protected boolean isAnnotationPresent(ReflectedConstructor<T> obj,
			Class<? extends Annotation> annotationType) {
		return obj.isAnnotationPresent(annotationType);
	}

	@Override
	protected int getParameterCount(ReflectedConstructor<T> member) {
		return member.getParameters().size();
	}

	@Override
	protected String getName(ReflectedConstructor<T> obj) {
		return obj.getName();
	}

	@Override
	protected boolean hasParameterTypes(ReflectedConstructor<T> obj, Class<?>[] parameterTypes) {
		final Enumerable<ReflectedParameter> parameters = obj.getParameters();
		if (parameters.size() != parameterTypes.length){
			return false;
		}
		return parameters.join(Enumerables.asEnumerable(parameterTypes)).every(new Predicate<Pair<ReflectedParameter, Class<?>>>(){

			@Override
			public Boolean apply(Pair<ReflectedParameter, Class<?>> pair) {
				return pair.left().getType().getReflectedType().equals(pair.right());
			}
			
		});
	}

}
