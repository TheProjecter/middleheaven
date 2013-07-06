package org.middleheaven.core.reflection.inspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.Enumerable;
import org.middleheaven.core.reflection.MemberAccess;
import org.middleheaven.core.reflection.MethodHandler;
import org.middleheaven.core.reflection.ReflectiveMethodHandler;
import org.middleheaven.util.function.Mapper;
import org.middleheaven.util.function.Predicate;

public class MethodIntrospectionCriteriaBuilder<T> extends ParameterizableMemberIntrospectionCriteriaBuilder<T,MethodHandler>{

	Resolver resolver = new StandardResolver();
	
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
		add(new Predicate<MethodHandler>(){

			@Override
			public Boolean apply(MethodHandler obj) {
				return !obj.getDeclaringClass().equals(Object.class);
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

	public MethodIntrospectionCriteriaBuilder<T> match(Predicate<MethodHandler> filter) {
		add(filter);
		return this;
	}

	@Override
	protected int getParameterCount(MethodHandler member) {
		return member.getParameterTypes().length;
	}



	@Override
	protected int getModifiers(MethodHandler method) {
		return method.getModifiers();
	}

	@Override
	protected boolean isAnnotationPresent(MethodHandler method,Class<? extends Annotation> annotationType) {

		Set<Method> all = new HashSet<Method>();

		// read all methods in the hierarchy
		Class<?> superType = method.getDeclaringClass();

		while (superType!=null && !Object.class.equals(superType)){
			all.addAll(Arrays.asList(superType.getDeclaredMethods())); // private , protected , public
			all.addAll(Arrays.asList(superType.getMethods())); // super class inherit public

			for (Class<?> interfaceType : superType.getInterfaces()){
				all.addAll(Arrays.asList(interfaceType.getDeclaredMethods())); 
				all.addAll(Arrays.asList(interfaceType.getMethods())); 
			}

			superType = superType.getSuperclass();

		}

		for (Method m : all){
			if (m.getName().equals(method.getName()) 
					&& m.getParameterTypes().length == method.getParameterTypes().length 
					&& m.isAnnotationPresent(annotationType)
					&& Arrays.equals(m.getParameterTypes(), method.getParameterTypes())

			){
				return true;
			}
		}

		return false;

	}

	@Override
	protected String getName(MethodHandler obj) {
		return obj.getName();
	}

	@Override
	protected boolean hasParameterTypes(MethodHandler obj, Class<?>[] parameterTypes) {
		// equals already compares array length.
		return Arrays.equals(obj.getParameterTypes(), parameterTypes);
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
	protected Enumerable<MethodHandler> getAllMembersInType(Class<T> type) {
		return CollectionUtils.asEnumerable(resolver.resolve(type)).map(new Mapper<MethodHandler, Method>(){

			@Override
			public MethodHandler apply(Method obj) {
				return new ReflectiveMethodHandler(obj);
			}
			
		});
	}
	
	private static interface Resolver {
		
		public Enumerable<Method> resolve(Class<?> type);
	}
	
	private static class StandardResolver implements Resolver{

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Enumerable<Method> resolve(Class<?> type) {
			return CollectionUtils.asEnumerable(Reflector.getReflector().getMethods(type));
		}
		
	}
	
	private static class HierarchyResolver implements Resolver{

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Enumerable<Method> resolve(Class<?> type) {
			
			
			LinkedList<Method> stack = new LinkedList<Method>();
			
			while ( type != null && !type.equals(Object.class) ){
				for (Method m : Reflector.getReflector().getMethods(type)){
					stack.addFirst(m);
				}
				
				type = type.getSuperclass();
			}
			
			
			
			return CollectionUtils.asEnumerable(stack);
		}
		
	}


}
