package org.middleheaven.core.reflection.inspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Comparator;

import org.middleheaven.collections.Enumerable;
import org.middleheaven.core.reflection.MemberAccess;
import org.middleheaven.util.classification.LogicComposedPredicate;
import org.middleheaven.util.classification.LogicOperator;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.function.Predicate;

public abstract class MemberIntrospectionCriteriaBuilder<T, M> {

	protected Comparator<M> comparator = null;
	private IntrospectionCriteriaBuilder<T> builder;
	protected final LogicComposedPredicate<M> logicClassifier =
		new LogicComposedPredicate<M>(LogicOperator.and());
	
	protected MemberIntrospectionCriteriaBuilder(IntrospectionCriteriaBuilder<T> builder) {
		this.builder = builder;
	}
	
	protected void add(Predicate<M> filter) {
		logicClassifier.add(filter);
	}
	
	public M retrive(){
		return retriveAll().isEmpty() ? null : retriveAll().getFirst();
	}


	public void each(Block<M> block){
		retriveAll().forEach(block);
	}
	
	protected void addNameFilter(final String name){
		logicClassifier.add(new Predicate<M>(){

			@Override
			public Boolean apply(M obj) {
				return getName(obj).equalsIgnoreCase(name);
			}
			
		});
	}

	protected abstract String getName(M obj);
	
	protected void addWithAccessFilter(final MemberAccess ... acesses){
		logicClassifier.add(new Predicate<M>(){

			@Override
			public Boolean apply(M obj) {
				boolean result = false;
				for (MemberAccess a : acesses){
					result = result | a.is(getModifiers(obj));
				}
				return result;
			}
			
		});
	}
	
	protected void addWithStaticFilter(final boolean allowStatic){
		logicClassifier.add(new Predicate<M>(){

			@Override
			public Boolean apply(M obj) {
				if (Modifier.isStatic(getModifiers(obj))){
					return allowStatic;
				}
				return true;
			}
			
		});
	}
	
	protected abstract int getModifiers(M obj);
	protected abstract boolean isAnnotationPresent(M obj , Class<? extends Annotation> annotationType);
	
	protected void addAnnotatedWithFilter(final Class<? extends Annotation> ... annotations){
		logicClassifier.add(new Predicate<M>(){
			
			@Override
			public Boolean apply(M c) {
				for (Class<? extends Annotation> a : annotations){
					if (isAnnotationPresent(c,a)){
						return true;
					}
				}
				return false;
			}
		
		});
	}
	
	public Enumerable<M> retriveAll(){
		Enumerable<M> result = getAllMembersInType(builder.type);
		
		if(logicClassifier!=null){
			result = result.filter(logicClassifier);
		} 
		
		if (comparator!=null){
			result = result.sort(comparator);
		}

		return result;
	}

	protected abstract Enumerable<M> getAllMembersInType(Class<T> type);
}
