package org.middleheaven.reflection.inspection;

import java.lang.annotation.Annotation;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.reflection.MemberAccess;
import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.ReflectedField;

public class FieldIntrospectionCriteriaBuilder<T> extends MemberIntrospectionCriteriaBuilder<T,ReflectedField>{

	Resolver resolver = new StandardResolver();
	
	FieldIntrospectionCriteriaBuilder(IntrospectionCriteriaBuilder<T> builder) {
		super(builder);
	}
	
	public FieldIntrospectionCriteriaBuilder<T> named(final String name) {
		super.addNameFilter(name);
		return this;
	}
	
	/**
	 * annotated 
	 * @param annotations
	 * @return
	 */
	public FieldIntrospectionCriteriaBuilder<T> annotatedWith(final Class<? extends Annotation> ... annotations) {
		addAnnotatedWithFilter(annotations);
		return this;
	}
	
	/**
	 * @return
	 */
	public FieldIntrospectionCriteriaBuilder<T> beingStatic(final boolean allowStatic) {
		addWithStaticFilter(allowStatic);
		return this;
	}
	
	public FieldIntrospectionCriteriaBuilder<T> withAccess(final MemberAccess ... acesses){
		addWithAccessFilter(acesses);
		return this;

	}
	
	@Override
	protected int getModifiers(ReflectedField obj) {
		return obj.getModifiers();
	}

	@Override
	protected Enumerable<ReflectedField> getAllMembersInType(ReflectedClass<T> type) {
		return resolver.resolve(type);
	}

	@Override
	protected boolean isAnnotationPresent(ReflectedField obj,
			Class<? extends Annotation> annotationType) {
		return obj.isAnnotationPresent(annotationType);
	}

	@Override
	protected String getName(ReflectedField obj) {
		return obj.getName();
	}

	/**
	 * @return
	 */
	public FieldIntrospectionCriteriaBuilder<T> searchHierarchy() {
		resolver = new HierarchyResolver();
		return this;
	}



	private static interface Resolver {
		
		public Enumerable<ReflectedField> resolve(ReflectedClass<?> type);
	}
	
	private static class StandardResolver implements Resolver{

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Enumerable<ReflectedField> resolve(ReflectedClass<?> type) {
			return type.getCallableFields();
		}
		
	}
	
	private static class HierarchyResolver implements Resolver{

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Enumerable<ReflectedField> resolve(ReflectedClass<?> type) {

			Enumerable<ReflectedField> stack =  Enumerables.emptyEnumerable();
			
			while ( type != null && !type.equals(Object.class) ){
				
				stack = stack.concat(type.getCallableFields());
				
				type = type.getSuperclass();
			}
			
			return stack.reverse();
		}
		
	}


}
