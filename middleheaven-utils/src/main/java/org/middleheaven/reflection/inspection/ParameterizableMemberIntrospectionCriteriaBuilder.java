package org.middleheaven.reflection.inspection;

import java.util.Comparator;

import org.middleheaven.util.function.Predicate;


public abstract class ParameterizableMemberIntrospectionCriteriaBuilder<T, M> extends
		MemberIntrospectionCriteriaBuilder<T, M> {

	protected ParameterizableMemberIntrospectionCriteriaBuilder(
			IntrospectionCriteriaBuilder<T> builder) {
		super(builder);
	}

	protected void addSortingByQuantityOfParameters() {
		this.comparator = new Comparator<M>(){

			@Override
			public int compare(M a, M b) {
				return getParameterCount(a) - getParameterCount(b);
			}

		};

	}
	
	protected void addParamterTypeFilter(final Class<?>[] parameterTypes){
		logicClassifier.add(new Predicate<M>(){

			@Override
			public Boolean apply(M obj) {
				return hasParameterTypes(obj, parameterTypes);
			}
			
		});
	}
	
	protected abstract boolean hasParameterTypes(M obj, Class<?>[] parameterTypes);

	protected abstract int getParameterCount(M member);

}
