package org.middleheaven.reflection.inspection;

import java.lang.annotation.Annotation;

import org.middleheaven.collections.Pair;
import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.reflection.MemberAccess;
import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.ReflectedMethod;
import org.middleheaven.reflection.ReflectedParameter;
import org.middleheaven.reflection.Reflector;
import org.middleheaven.util.function.Predicate;

public class MethodIntrospectionCriteriaBuilder<T> extends ParameterizableMemberIntrospectionCriteriaBuilder<T,ReflectedMethod>{

	Resolver resolver = new StandardResolver();
	ReflectedClass<Object> objectClass = Reflector.getReflector().reflect(Object.class);
	
	MethodIntrospectionCriteriaBuilder(IntrospectionCriteriaBuilder<T> builder){
		super(builder);
	}
	
	public MethodIntrospectionCriteriaBuilder<T> annotatedWith(final Class<? extends Annotation> ... annotations) {

		super.addAnnotatedWithFilter(annotations);
		return this;
	}

	public MethodIntrospectionCriteriaBuilder<T> sortedByQuantityOfParameters(){

		super.addSortingByQuantityOfParameters();
		return this;
	}

	public MethodIntrospectionCriteriaBuilder<T> withAccess(final MemberAccess ... acesses){

		super.addWithAccessFilter(acesses);

		return this;
	}

	public MethodIntrospectionCriteriaBuilder<T> notInheritFromObject() {
		add(new Predicate<ReflectedMethod>(){

			@Override
			public Boolean apply(ReflectedMethod obj) {
				return !obj.getDeclaringClass().getReflectedType().equals(Object.class) && !objectClass.containsMethod(obj);
			}

		});
		return this;
	}

	public MethodIntrospectionCriteriaBuilder<T> named(final String name) {
		super.addNameFilter(name);
		return this;
	}

	public MethodIntrospectionCriteriaBuilder<T> withParametersType(Class<?>[] parameterTypes) {
		super.addParamterTypeFilter(parameterTypes);
		return this;
	}

	public MethodIntrospectionCriteriaBuilder<T> match(Predicate<ReflectedMethod> filter) {
		add(filter);
		return this;
	}

	@Override
	protected int getParameterCount(ReflectedMethod member) {
		return member.getParameters().size();
	}



	@Override
	protected int getModifiers(ReflectedMethod method) {
		return method.getModifiers();
	}

	@Override
	protected boolean isAnnotationPresent(final ReflectedMethod method, final Class<? extends Annotation> annotationType) {

		Enumerable<ReflectedMethod> all = Enumerables.emptyEnumerable();

		// read all methods in the hierarchy
		ReflectedClass<?> superType = method.getDeclaringClass();

		while (superType!=null && !Object.class.equals(superType)){
			all = all.concat(superType.getDeclaredMethods());
			all = all.concat(superType.getMethods());
			
			for (ReflectedClass<?> interfaceType : superType.getInterfaces()){
				all = all.concat(interfaceType.getDeclaredMethods()); 
				all = all.concat(interfaceType.getMethods()); 
			}

			superType = superType.getSuperclass();

		}
		
		return all.any(new Predicate<ReflectedMethod>(){

			@Override
			public Boolean apply(ReflectedMethod m) {
				return m.getName().equals(method.getName()) 
						&& m.isAnnotationPresent(annotationType)
						&& m.getParameters().equals(method.getParameters());
			}
			
		});


	}

	@Override
	protected String getName(ReflectedMethod obj) {
		return obj.getName();
	}

	@Override
	protected boolean hasParameterTypes(ReflectedMethod obj, Class<?>[] parameterTypes) {
		
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

	/**
	 * 
	 * @param allowStatic
	 * @return
	 */
	public MethodIntrospectionCriteriaBuilder<T> beingStatic(boolean allowStatic) {
		this.addWithStaticFilter(allowStatic);
		return this;
	}

	/**
	 * @return
	 */
	public MethodIntrospectionCriteriaBuilder<T>  searchHierarchy() {
		resolver = new HierarchyResolver();
		return this;
	}


	@Override
	protected Enumerable<ReflectedMethod> getAllMembersInType(ReflectedClass<T> type) {
		return resolver.resolve(type);
	}
	
	private static interface Resolver {
		
		public Enumerable<ReflectedMethod> resolve(ReflectedClass<?> type);
	}
	
	private static class StandardResolver implements Resolver{

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Enumerable<ReflectedMethod> resolve(ReflectedClass<?> type) {
		   Enumerable<ReflectedMethod> stack =  Enumerables.emptyEnumerable();
			
			while ( type != null && !type.equals(Object.class) ){
				
				stack = stack.concat(type.getDeclaredMethods());
				
				type = type.getSuperclass();
			}
			
			return stack;
		}
		
	}
	
	private static class HierarchyResolver implements Resolver{

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Enumerable<ReflectedMethod> resolve(ReflectedClass<?> type) {

			Enumerable<ReflectedMethod> stack =  Enumerables.emptyEnumerable();
			
			while ( type != null && !type.equals(Object.class) ){
				
				stack = stack.concat(type.getMethods());
				
				type = type.getSuperclass();
			}
			
			return stack.reverse();
		}
		
	}


}
