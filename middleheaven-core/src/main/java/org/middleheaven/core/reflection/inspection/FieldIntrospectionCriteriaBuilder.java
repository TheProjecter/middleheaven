package org.middleheaven.core.reflection.inspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.Enumerable;
import org.middleheaven.core.reflection.MemberAccess;

public class FieldIntrospectionCriteriaBuilder<T> extends MemberIntrospectionCriteriaBuilder<T,Field>{

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
	protected int getModifiers(Field obj) {
		return obj.getModifiers();
	}

	@Override
	protected Enumerable<Field> getAllMembersInType(Class<T> type) {
		return resolver.resolve(type);
	}

	@Override
	protected boolean isAnnotationPresent(Field obj,
			Class<? extends Annotation> annotationType) {
		return obj.isAnnotationPresent(annotationType);
	}

	@Override
	protected String getName(Field obj) {
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
		
		public Enumerable<Field> resolve(Class<?> type);
	}
	
	private static class StandardResolver implements Resolver{

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Enumerable<Field> resolve(Class<?> type) {
			return CollectionUtils.asEnumerable(Reflector.getReflector().getFields(type));
		}
		
	}
	
	private static class HierarchyResolver implements Resolver{

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Enumerable<Field> resolve(Class<?> type) {

			LinkedList<Field> stack = new LinkedList<Field>();
			
			while ( type != null && !type.equals(Object.class) ){
				for (Field m : Reflector.getReflector().getFields(type)){
					stack.addFirst(m);
				}
				
				type = type.getSuperclass();
			}
			
			return CollectionUtils.asEnumerable(stack);
		}
		
	}


}
