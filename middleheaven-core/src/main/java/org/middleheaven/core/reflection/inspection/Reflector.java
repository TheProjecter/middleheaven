package org.middleheaven.core.reflection.inspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.middleheaven.collections.ArrayEnumerable;
import org.middleheaven.collections.ArrayIterable;
import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.CompositeIterableEnumerable;
import org.middleheaven.collections.Enumerable;
import org.middleheaven.core.reflection.ClassCastReflectionException;
import org.middleheaven.core.reflection.IllegalAccessReflectionException;
import org.middleheaven.core.reflection.InstantiationReflectionException;
import org.middleheaven.core.reflection.InvocationTargetReflectionException;
import org.middleheaven.core.reflection.NoSuchClassReflectionException;
import org.middleheaven.core.reflection.NoSuchMethodReflectionException;
import org.middleheaven.core.reflection.PropertyHandler;
import org.middleheaven.core.reflection.ProxyHandler;
import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.core.reflection.ReflectionStrategy;
import org.middleheaven.core.reflection.WrapperProxy;
import org.middleheaven.util.function.Predicate;

class Reflector {

	private ReflectionStrategy stategy;

	private Reflector(){
		stategy = new MixedReflectionStrategy();
	}

	private final static Reflector ME = new Reflector();
	
	protected static Reflector getReflector () {
		return ME;
	}
	
	public  boolean isEnhanced(Class<?> type) {
		return stategy.isEnhanced(type);
	}

	public  boolean isInClasspath(String className) {
		try {
			Class.forName(className, false, Reflector.class.getClassLoader());
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public  Object unproxy(Object proxy){
		if (proxy instanceof WrapperProxy){
			return ((WrapperProxy)proxy).getWrappedObject();
		}
		return null;
	}

	public  <T> T proxyType (Class<T> facadeClass , final ProxyHandler delegator, Object[] args){
		return stategy.proxyType(facadeClass, delegator, args);
	}

	public  <T> T proxyType (Class<?> facadeClass , final ProxyHandler delegator,Class<T> proxyInterface , Class<?> ... adicionalInterfaces){
		
		if (adicionalInterfaces.length > 0){
			Set<Class<?>> set = new HashSet<Class<?>>();
			set.add(proxyInterface);
			
			for (Class<?> c : adicionalInterfaces){
				if (!set.add(c)){
					// duplicated
					throw new IllegalArgumentException("Adicional interfaces must be diferent from each other and from the proxy interface");
				}
			}
		}
		
		
		return stategy.proxyType( facadeClass,delegator,proxyInterface,adicionalInterfaces);
	}

	public  <I> I proxyObject ( Object delegationTarget , Class<I> proxyInterface){
		return stategy.proxyObject(delegationTarget, proxyInterface);
	}

	public  <I> I proxyObject (Object delegationTarget , final ProxyHandler delegator , Class<I> proxyInterface ){
		return stategy.proxyObject(delegationTarget, delegator, proxyInterface);
	}

	public  <I> I proxyObject (Object delegationTarget , final ProxyHandler delegator , Class<I> proxyInterface , Class<?> ... adicionalInterfaces){
		return stategy.proxyObject(delegationTarget, delegator, proxyInterface,adicionalInterfaces);
	}

	

	public  Class<?> getRealType(Class<?> type){
		return stategy.getRealType(type);
	}

	public  PropertyHandler getPropertyAccessor(Class<?> type, String fieldName){
		return stategy.getPropertyAccessor(type, fieldName);
	}

	public  Enumerable<PropertyHandler> getPropertyAccessors(Class<?> type) throws ReflectionException{
		return getPropertyAccessors(type, false);
	}
	
	public  Enumerable<PropertyHandler> getPropertyAccessors(Class<?> type, boolean inherit) throws ReflectionException{
		return stategy.getPropertyAccessors(type, inherit);
	}

	/**
	 * Determines if class <code>match</code> is a superclass or supper-interface
	 * of class <code>test</code> 
	 * @param test
	 * @param match
	 * @return
	 */
	public  boolean isAssignableFrom (Class<?> test , Class<?> match){
		return match.isAssignableFrom(test);
	}

	public  Object compareField (Object obj , String fieldName)throws ReflectionException{

		for (Method m : getMethods(obj.getClass())){
			String name = m.getName().toLowerCase();
			if ( (name.startsWith("get") || name.startsWith("is")) && name.endsWith(fieldName)){
				try {
					return m.invoke(obj, new Object[0]);
				} catch (IllegalArgumentException e) {
					throw new IllegalAccessReflectionException(e);
				} catch (IllegalAccessException e) {
					throw new IllegalAccessReflectionException(e);
				} catch (InvocationTargetException e) {
					throw new InvocationTargetReflectionException(e);
				}
			}
		}

		throw new NoSuchMethodReflectionException(fieldName);
	}

	/**
	 * Loads a class from its name
	 * @param className
	 * @return
	 * @throws NoSuchClassReflectionException if the class is not found in the classpath
	 *  
	 */
	public  Class<?> loadClass(String className) throws InstantiationReflectionException{
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	}

	public  <T> Class<? extends T> loadClass(String className, Class<T> superType) throws InstantiationReflectionException{
		try{
		return loadClass(className).asSubclass(superType);
		} catch (ClassCastException e){
			throw new ClassCastReflectionException(className, superType.getName());
		}
	}

	public  <T> Class<? extends T> loadClass(String className, Class<T> superType, ClassLoader cloader) {
		try {
			return cloader.loadClass(className).asSubclass(superType);
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	}

	public  <T> T newInstance(String className , Class<T> type, ClassLoader cloader) {
		try {
			return  type.cast(newInstance(Class.forName(className, true, cloader)));
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	}

	public  <T> T newInstance(String className, Class<T> type) throws NoSuchClassReflectionException {
		return  type.cast(newInstance(loadClass(className)));
	}

	public  <T> T newInstance(Class<T> type, Object ... args) throws ReflectionException{
		return newInstance(type,type,args);
	}

	public  <T> T newInstance(Class<T> castAs,Class<?> type, Object ... args) throws ReflectionException{
		try {

			// look for a Constructor with the correct arguments
			Class<?> [] parameterTypes = new Class<?> [args.length];
			for (int i=0;i<args.length;i++){
				parameterTypes[i] = args[i].getClass();
			}

			try {

				Constructor<?> c = type.getConstructor(parameterTypes);
				c.setAccessible(true);
				return castAs.cast(c.newInstance(args)); // Instantiate using the constructor
			} catch (SecurityException e) {
				throw new IllegalAccessReflectionException(e);
			} catch (NoSuchMethodException e) {
				throw new NoSuchMethodReflectionException(e);
			} catch (IllegalArgumentException e) {
				throw new IllegalAccessReflectionException(e);
			} catch (InvocationTargetException e) {
				throw new InvocationTargetReflectionException(e);
			}

		} catch (InstantiationException e){
			throw new ReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalAccessReflectionException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public  <T> List<Constructor<T>> allAnnotatedConstructors( Class<T> type, Class<? extends Annotation> ... annotations) {
		Constructor<T>[] constructors = (Constructor<T>[]) type.getDeclaredConstructors();

		List<Constructor<T>> annotated = new ArrayList<Constructor<T>>(constructors.length);

		for (Constructor<T> c : constructors){
			for (Class<? extends Annotation> a : annotations){
				if (c.isAnnotationPresent(a)){
					annotated.add(c);
				}
			}
		}

		return annotated;
	}

	public  Class<?>[] typesOf (Object[] objs ){
		Class<?>[] classes = new Class<?>[objs.length];
		for (int i =0;i < objs.length;i++){
			classes[i] = objs[i].getClass();
		}
		return classes;
	}

	public Enumerable<Annotation> getAnnotations(Class<?> type) {
		// read all methods in the hierarchy
		Set<Annotation> all = new HashSet<Annotation>();
		Class<?> superType = type;
		while (superType!=null && !superType.equals(Object.class)){
			all.addAll(Arrays.asList(superType.getAnnotations())); // pannotations in class
			for (Class<?> in : superType.getInterfaces()){
				// annotations in interfaces
				all.addAll(Arrays.asList(in.getAnnotations()));
			}
			// up to super class
			superType = superType.getSuperclass();
		}

		return CollectionUtils.asEnumerable(all);
	}

	public  <T> T invokeStatic(Class<T> returnType,Method methodToInvoke, Object ... params) {
		try {
			methodToInvoke.setAccessible(true);
			return returnType.cast(methodToInvoke.invoke(null, params));
		} catch (SecurityException e) {
			throw new IllegalAccessReflectionException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalAccessReflectionException(e);
		} catch (InvocationTargetException e) {
			throw new InvocationTargetReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalAccessReflectionException(e);
		}
	}

	public  <T> T invoke(Class<T> returnType,Method methodToInvoke, Class<?> translatingObjectClass, Object ... params) {
		return invoke(returnType, methodToInvoke, newInstance(translatingObjectClass), params);
	}

	public  <T> T invoke(Class<T> returnType,Method methodToInvoke, Object translatingObject, Object ... params) {
		try {
			methodToInvoke.setAccessible(true);
			Object obj = methodToInvoke.invoke(translatingObject, params);
			if (returnType!=null){
				return returnType.cast(obj);
			} else {
				return null;
			}
		} catch (SecurityException e) {
			throw new IllegalAccessReflectionException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalAccessReflectionException(e);
		} catch (InvocationTargetException e) {
			throw new InvocationTargetReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalAccessReflectionException(e);
		}
	}

	private Enumerable<Method> getClassMethods (final Class<?> type){
		
		Set<Method> all = new HashSet<Method>();
		for(Method m : type.getDeclaredMethods()){
			all.add(m);
		}
		for(Method m : type.getMethods()){
			all.add(m);
		}
		return new ArrayEnumerable<Method>(all.toArray(new Method[all.size()]));
	}
	

	public Method getMethod (Class<?> type , String name, Class<?>[] paramTypes){
		try{
			return type.getMethod(name, paramTypes);
		} catch (NoSuchMethodException e){
			try {
				return type.getDeclaredMethod(name, paramTypes);
			} catch (NoSuchMethodException e2) {
				return null;
			}
		}
	}

	public  Enumerable<Method> getMethods(Class<?> type) {
		return getMethods(type,null);
	}

	public  Enumerable<Method> getMethods(Class<?> type, Predicate<Method> predicate) {
		if (predicate != null){
			return getClassMethods(type).filter(predicate);
		} else {
			return getClassMethods(type);
		}
	}

	/**
	 * @param type
	 * @return
	 */
	public Enumerable<Field> getFields(final Class<?> type) {
		return new CompositeIterableEnumerable<Field>()
				.Add(new ArrayIterable<Field>(type.getFields()))
				.Add(new ArrayIterable<Field>(type.getDeclaredFields()));
	}

}
