package org.middleheaven.reflection;

import java.util.HashSet;
import java.util.Set;

import org.middleheaven.reflection.impl.JavaInstrospectionStrategy;
import org.middleheaven.reflection.inspection.InstrospectionStrategy;

public final class Reflector {

	private ProxyCreationStrategy proxyCreationStategy;
	private InstrospectionStrategy introspectionStategy;
	
	private Reflector(){
		proxyCreationStategy = new MixedProxyCreationStrategy();
		introspectionStategy = new JavaInstrospectionStrategy();
	}

	private final static Reflector ME = new Reflector();
	
	public static Reflector getReflector () {
		return ME;
	}
	
	public <T> ReflectedClass<T> reflect(Class<T> type){
		return introspectionStategy.reflect(type);
	}

	public boolean isEnhanced(Class<?> type) {
		return proxyCreationStategy.isEnhanced(type);
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
		return proxyCreationStategy.proxyType(facadeClass, delegator, args);
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
		
		
		return proxyCreationStategy.proxyType( facadeClass,delegator,proxyInterface,adicionalInterfaces);
	}

	public  <I> I proxyObject ( Object delegationTarget , Class<I> proxyInterface){
		return proxyCreationStategy.proxyObject(delegationTarget, proxyInterface);
	}

	public  <I> I proxyObject (Object delegationTarget , final ProxyHandler delegator , Class<I> proxyInterface ){
		return proxyCreationStategy.proxyObject(delegationTarget, delegator, proxyInterface);
	}

	public  <I> I proxyObject (Object delegationTarget , final ProxyHandler delegator , Class<I> proxyInterface , Class<?> ... adicionalInterfaces){
		return proxyCreationStategy.proxyObject(delegationTarget, delegator, proxyInterface,adicionalInterfaces);
	}

	public  ReflectedClass<?> getRealType(ReflectedClass<?> type){
		return proxyCreationStategy.getRealType(type);
	}
//
//	public  ReflectedProperty getPropertyAccessor(Class<?> type, String fieldName){
//		return stategy.getPropertyAccessor(type, fieldName);
//	}
//	public  Enumerable<ReflectedProperty> getPropertyAccessors(Class<?> type, boolean inherit) throws ReflectionException{
//		return stategy.getPropertyAccessors(type, inherit);
//	}

//	public  Enumerable<ReflectedProperty> getPropertyAccessors(Class<?> type) throws ReflectionException{
//		return getPropertyAccessors(type, false);
//	}
//	


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

//	public  Object compareField (Object obj , String fieldName) throws ReflectionException{
//
//		for (ReflectedMethod m : getMethods(obj.getClass())){
//			String name = m.getName().toLowerCase();
//			if ( (name.startsWith("get") || name.startsWith("is")) && name.endsWith(fieldName)){
//				return m.invoke(obj);
//			}
//		}
//
//		throw new NoSuchMethodReflectionException(fieldName);
//	}

	/**
	 * Loads a class from its name
	 * @param className
	 * @return
	 * @throws NoSuchClassReflectionException if the class is not found in the classpath
	 *  
	 */
	public  ReflectedClass<?> loadClass(String className) throws InstantiationReflectionException{
		return introspectionStategy.loadClass(className);
	}

	public  <T> ReflectedClass<? extends T> loadClass(String className, ReflectedClass<T> superType) throws InstantiationReflectionException{
		return introspectionStategy.loadClass(className, superType);

	}

	public  <T> Class<? extends T> loadClass(String className, Class<T> superType, ClassLoader cloader) {
		try {
			return cloader.loadClass(className).asSubclass(superType);
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	}
//
//	public  <T> T newInstance(String className , Class<T> type, ClassLoader cloader) {
//		try {
//			return  type.cast(newInstance(Class.forName(className, true, cloader)));
//		} catch (ClassNotFoundException e) {
//			throw new NoSuchClassReflectionException(className);
//		}
//	}
//
//	public  <T> T newInstance(String className, Class<T> type) throws NoSuchClassReflectionException {
//		return  type.cast(newInstance(loadClass(className)));
//	}
//
//	public  <T> T newInstance(Class<T> type, Object ... args) throws ReflectionException{
//		return newInstance(type,type,args);
//	}

//	
//
//	public  Class<?>[] typesOf (Object[] objs ){
//		Class<?>[] classes = new Class<?>[objs.length];
//		for (int i =0;i < objs.length;i++){
//			classes[i] = objs[i].getClass();
//		}
//		return classes;
//	}

	
//
//	public  <T> T invokeStatic(Class<T> returnType,Method methodToInvoke, Object ... params) {
//		try {
//			methodToInvoke.setAccessible(true);
//			return returnType.cast(methodToInvoke.invoke(null, params));
//		} catch (SecurityException e) {
//			throw new IllegalAccessReflectionException(e);
//		} catch (IllegalArgumentException e) {
//			throw new IllegalAccessReflectionException(e);
//		} catch (InvocationTargetException e) {
//			throw new InvocationTargetReflectionException(e);
//		} catch (IllegalAccessException e) {
//			throw new IllegalAccessReflectionException(e);
//		}
//	}
//
//	public  <T> T invoke(Class<T> returnType,Method methodToInvoke, Class<?> translatingObjectClass, Object ... params) {
//		return invoke(returnType, methodToInvoke, newInstance(translatingObjectClass), params);
//	}

//	public  <T> T invoke(Class<T> returnType,Method methodToInvoke, Object translatingObject, Object ... params) {
//		try {
//			methodToInvoke.setAccessible(true);
//			Object obj = methodToInvoke.invoke(translatingObject, params);
//			if (returnType!=null){
//				return returnType.cast(obj);
//			} else {
//				return null;
//			}
//		} catch (SecurityException e) {
//			throw new IllegalAccessReflectionException(e);
//		} catch (IllegalArgumentException e) {
//			throw new IllegalAccessReflectionException(e);
//		} catch (InvocationTargetException e) {
//			throw new InvocationTargetReflectionException(e);
//		} catch (IllegalAccessException e) {
//			throw new IllegalAccessReflectionException(e);
//		}
//	}



//	public Method getMethod (Class<?> type , String name, Class<?>[] paramTypes){
//		try{
//			return type.getMethod(name, paramTypes);
//		} catch (NoSuchMethodException e){
//			try {
//				return type.getDeclaredMethod(name, paramTypes);
//			} catch (NoSuchMethodException e2) {
//				return null;
//			}
//		}
//	}

//	public  Enumerable<ReflectedMethod> getMethods(Class<?> type) {
//		return getMethods(type,null);
//	}
//
//	public  Enumerable<ReflectedMethod> getMethods(Class<?> type, Predicate<ReflectedMethod> predicate) {
//		if (predicate != null){
//			return getClassMethods(type).filter(predicate);
//		} else {
//			return getClassMethods(type);
//		}
//	}
//
//	private Enumerable<ReflectedMethod> getClassMethods (final Class<?> type){
//		
//		
//		
//		Set<ReflectedMethod> all = new HashSet<ReflectedMethod>();
//		for(Method m : type.getDeclaredMethods()){
//			all.add(m);
//		}
//		for(Method m : type.getMethods()){
//			all.add(m);
//		}
//		return new ArrayEnumerable<Method>(all.toArray(new Method[all.size()]));
//	}
	
	
//	/**
//	 * @param type
//	 * @return
//	 */
//	public Enumerable<Field> getFields(final Class<?> type) {
//		return Enumerables.asEnumerable(type.getFields()).concat(Enumerables.asEnumerable(type.getDeclaredFields()));
//	}

}
