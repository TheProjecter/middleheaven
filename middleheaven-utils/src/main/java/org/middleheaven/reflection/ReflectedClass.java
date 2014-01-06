/**
 * 
 */
package org.middleheaven.reflection;

import java.lang.annotation.Annotation;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.reflection.inspection.IntrospectionCriteriaBuilder;
import org.middleheaven.util.Maybe;

/**
 * 
 */
public interface ReflectedClass<T> {

	
	public boolean equals(Class<?> other); 
	
	public T newProxyInstance(ProxyHandler handler, Object ... args);

	public <I> I newProxyInstance(ProxyHandler handler, Class<I> proxyInterface ,Class<?> ... adicionalInterfaces);
	
	Class<T> getReflectedType();

	/**
	 * @return
	 */
	ReflectedClass<? super T> getSuperclass();

	/**
	 * @see Class#getDeclaredMethods()
	 * @return
	 */
	Enumerable<ReflectedMethod> getDeclaredMethods();

	/**
	 * @param string
	 * @param class1
	 * @return
	 */
	ReflectedMethod getMethod(String string, Class<?> ... types);
	
	/**
	 * @see Class#getMethods()
	 * @return
	 */
	Enumerable<ReflectedMethod> getMethods();

	/**
	 * Returns the union of all Methods and DeclaredMethod
	 * @return
	 */
	Enumerable<ReflectedMethod> getCallableMethods();

	/**
	 * @return
	 */
	Enumerable<ReflectedClass<?>> getInterfaces();

	/**
	 * @return
	 */
	boolean isInterface();

	/**
	 * @return all methods in interfaces
	 */
	Enumerable<ReflectedMethod> getInterfacesMethods();

	/**
	 * @param annotationClass
	 * @return
	 */
	boolean isAnnotationPresent(Class<? extends Annotation> annotationClass);

	public <A extends Annotation> Maybe<A> getAnnotation(Class<A> annotationClass);
	
	/**
	 * Returns all annotations in the class hierarchy including annotations in implementes interfaces
	 * @return
	 */
	Enumerable<Annotation> getAnnotations();

	
	
	/**
	 * @param object
	 * @return
	 */
	T cast(Object object);

	/**
	 * @return
	 */
	String getCanonicalName();

	/**
	 * @return
	 */
	String getName();

	/**
	 * @return
	 */
	String getSimpleName();

	/**
	 * @return
	 */
	ReflectedPackage getPackage();

	/**
	 * @param args
	 * @return
	 */
	T newInstance(Object ... args);
	
	T newInstance(ClassLoader loader, Object ... args);

	/**
	 * @param obj
	 * @return
	 */
	boolean isInstance(Object obj);

	/**
	 * @return
	 */
	boolean isPrimitive();
	
	/**
	 * Informs if the  type is fundamental. Fundamental types are thoses primitive, respective wrappers,
	 * {@code String} and {@code Date}
	 * 
	 * @return {@code true} if the type is fundamental, {@code false] otherwise.
	 */
	public boolean isFundamental();

	/**
	 * @return
	 */
	Enumerable<ReflectedField> getDeclaredFields();

	/**
	 * @param type
	 * @return
	 */
	boolean isAssignableFrom(ReflectedClass<?> type);

	/**
	 * @param class1
	 * @return
	 */
	boolean isAssignableFrom(Class<?> type);
	
	/**
	 * The first super class that descends from {@link Object};
	 * @return an absent {@link Maybe} if this is directly descendent form Object, of a not absent {@link Maybe} if a super class can be found
	 */
	public Maybe<ReflectedClass<?>> getRootParent();

	/**
	 * @return
	 */
	Enumerable<ReflectedConstructor<T>> getConstructors();

	/**
	 * @return
	 */
	Enumerable<ReflectedField> getFields();

	/**
	 * @return
	 */
	Enumerable<ReflectedField> getCallableFields();

	/**
	 * @return
	 */
	boolean isEnum();

	/**
	 * @param class1
	 * @return
	 */
	boolean isSubTypeOf(Class<?> other);

	/**
	 * @return
	 */
	Enumerable<ReflectedProperty> getProperties();
	
	
	public Enumerable<ReflectedClass<?>> getDeclaredInterfaces();

	/**
	 * @return
	 */
	boolean isEnhanced();

	/**
	 * @return
	 */
	int getModifiers();

	/**
	 * @return
	 */
	IntrospectionCriteriaBuilder<T> inspect();

	/**
	 * @param obj
	 * @return
	 */
	public boolean containsMethod(ReflectedMethod obj);

}
