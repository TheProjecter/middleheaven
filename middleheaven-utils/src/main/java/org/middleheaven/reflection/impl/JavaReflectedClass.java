/**
 * 
 */
package org.middleheaven.reflection.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.reflection.IllegalAccessReflectionException;
import org.middleheaven.reflection.InvocationTargetReflectionException;
import org.middleheaven.reflection.NoSuchMethodReflectionException;
import org.middleheaven.reflection.ProxyHandler;
import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.ReflectedConstructor;
import org.middleheaven.reflection.ReflectedField;
import org.middleheaven.reflection.ReflectedMethod;
import org.middleheaven.reflection.ReflectedPackage;
import org.middleheaven.reflection.ReflectedParameter;
import org.middleheaven.reflection.ReflectedProperty;
import org.middleheaven.reflection.ReflectionException;
import org.middleheaven.reflection.Reflector;
import org.middleheaven.reflection.inspection.IntrospectionCriteriaBuilder;
import org.middleheaven.util.Maybe;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.function.Function;

/**
 * 
 */
public class JavaReflectedClass<T> implements ReflectedClass<T>{

	private static final Function<ReflectedMethod, Method> transformMethod = new Function<ReflectedMethod, Method>(){

		@Override
		public ReflectedMethod apply(Method method) {
			return new JavaReflectedMethod(method);
		}

	};

	private static final Function<ReflectedField, Field> transformField = new Function<ReflectedField, Field>(){

		@Override
		public ReflectedField apply(Field field) {
			return new JavaReflectedField(field);
		}

	};

	private static final ClassTransform transformClass = new ClassTransform();
	private static final ConstructorTransform transformConsctructor = new ConstructorTransform(); 

	private static class ClassTransform <T> implements Function<ReflectedClass<T>, Class<T>>{

		@Override
		public ReflectedClass<T> apply(Class<T> type) {
			return new JavaReflectedClass<T>(type);
		}

	};

	private static class ConstructorTransform <T> implements Function<ReflectedConstructor<T>, Constructor<T>>{

		@Override
		public ReflectedConstructor<T> apply(Constructor<T> constructor) {
			return new JavaReflectedConstructor<T>(constructor);
		}

	};


	private static Map<String , JavaReflectedClass> cache = new HashMap<String, JavaReflectedClass>();
	
	public static <U> JavaReflectedClass<U> valueOf(Class<U> type){
		if (type == null){
			return null; // the superclass of object can be null;
		}
		JavaReflectedClass<U> rc = cache.get(type.getName());
		if (rc == null){
			rc = new JavaReflectedClass<U>(type);
			cache.put(type.getName(), rc);
		}
		return rc; 
	}
	
	private Class<T> type;

	/**
	 * Constructor.
	 * @param superclass
	 */
	private JavaReflectedClass(Class<T> type) {
		this.type = type;
	}
	
	private Class<T> type(){
		 return type;
	}
	
	public T newProxyInstance(ProxyHandler handler, Object ... args){
		return Reflector.getReflector().proxyType(type(), handler, args);
	} 

	public <I> I newProxyInstance(ProxyHandler handler, Class<I> proxyInterface ,Class<?> ... adicionalInterfaces){
		if(!proxyInterface.isInterface()){
			throw new UnsupportedOperationException("Cannot proxy " + type.getName() + ".Type is not an interface");
		}
		return Reflector.getReflector().proxyType(type(), handler, proxyInterface, adicionalInterfaces);
	} 
	
	public boolean equals(Object other){
		return other instanceof JavaReflectedClass && equals((JavaReflectedClass)other);
	}
	
	private boolean equals(JavaReflectedClass<?> other){
		return this.type.equals(other.type);
	}
	
	public int hashCode(){
		return this.type.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReflectedClass<? super T> getSuperclass() {
		return valueOf(type().getSuperclass());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<T> getReflectedType() {
		return type();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<ReflectedMethod> getDeclaredMethods() {
		return Enumerables.asEnumerable(type().getDeclaredMethods()).map(transformMethod);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<ReflectedMethod> getMethods() {
		return Enumerables.asEnumerable(type().getMethods()).map(transformMethod);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<ReflectedMethod> getCallableMethods() {
		return getMethods().concat(getDeclaredMethods());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<ReflectedClass<?>> getInterfaces() {
		return Enumerables.asEnumerable(type().getInterfaces()).map(transformClass);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInterface() {
		return type().isInterface();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<ReflectedMethod> getInterfacesMethods() {
		Enumerable<ReflectedMethod> all = Enumerables.emptyEnumerable();
		for (ReflectedClass<?> i : getInterfaces()){
			all = all.concat( i.getCallableMethods());
		}
		return all;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
		return type().isAnnotationPresent(annotationClass);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<Annotation> getAnnotations() {
		// read all methods in the hierarchy
		Enumerable<Annotation> all = Enumerables.emptyEnumerable();

		Class<?> superType = type();
		while (superType!=null && !superType.equals(Object.class)){

			all = all.concat(Enumerables.asEnumerable(superType.getAnnotations())); // annotations in class
			for (Class<?> in : superType.getInterfaces()){
				// annotations in interfaces
				all = all.concat(Enumerables.asEnumerable(in.getAnnotations()));
			}
			// up to super class
			superType = superType.getSuperclass();
		}

		return Enumerables.asEnumerable(all);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T cast(Object object) {
		return type().cast(object);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCanonicalName() {
		return type().getCanonicalName();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return type.getName();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSimpleName() {
		return type().getSimpleName();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReflectedPackage getPackage() {
		return JavaReflectedPackage.valueOf(type().getPackage());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T newInstance(Object... args) {
		return newInstance(this.getClass().getClassLoader(), args);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T newInstance(ClassLoader loader, Object... args) {
		try {

			// look for a Constructor with the correct arguments
			Class<?> [] parameterTypes = new Class<?> [args.length];
			for (int i=0;i<args.length;i++){
				parameterTypes[i] = args[i].getClass();
			}

			try {
				Class<T> type = type();
				Constructor<?> c = type.getConstructor(parameterTypes);
				c.setAccessible(true);
				return type.cast(c.newInstance(args)); // Instantiate using the constructor
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
	public Enumerable<Constructor<T>> allAnnotatedConstructors( Class<T> type, Class<? extends Annotation> ... annotations) {
		Constructor<T>[] constructors = (Constructor<T>[]) type.getDeclaredConstructors();

		Enumerable<Constructor<T>> all = Enumerables.emptyEnumerable();

		for (Constructor<T> c : constructors){
			for (Class<? extends Annotation> a : annotations){
				if (c.isAnnotationPresent(a)){
					all = all.concat(c);
				}
			}
		}

		return all;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean isFundamental() {
		Class<T> type = type();
		return type.isPrimitive()
				|| String.class.isAssignableFrom(type)
				|| Date.class.isAssignableFrom(type)
				|| Integer.class.isAssignableFrom(type)
				|| Long.class.isAssignableFrom(type)
				|| BigInteger.class.isAssignableFrom(type)
				|| BigDecimal.class.isAssignableFrom(type)
				|| Byte.class.isAssignableFrom(type)
				|| Short.class.isAssignableFrom(type)
				|| Character.class.isAssignableFrom(type)
				|| Double.class.isAssignableFrom(type)
				|| Float.class.isAssignableFrom(type)
				;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInstance(Object obj) {
		return type().isInstance(obj);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPrimitive() {
		return type().isPrimitive();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAssignableFrom(ReflectedClass<?> type) {
		return this.type().isAssignableFrom(type.getReflectedType());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAssignableFrom(Class<?> type) {
		return this.type().isAssignableFrom(type);
	}


	/**
	 * The first super class that descends from {@link Object};
	 * @return an absent {@link Maybe} if this is directly descendent form Object, of a not absent {@link Maybe} if a super class can be found
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Maybe<ReflectedClass<?>> getRootParent() {

		if (type().getSuperclass().getName().equals(Object.class.getName())){
			return Maybe.absent();
		} 

		ReflectedClass<?> top = this.getSuperclass();

		while (!top.getSuperclass().getName().equals(Object.class.getName())){
			top = top.getSuperclass();
		}

		Maybe m = Maybe.of(top);
		return m;
	}
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Enumerable<ReflectedConstructor<T>> getConstructors() {
		// the constructors are directly taken from type T
		return Enumerables.asEnumerable(type().getConstructors()).map(transformConsctructor);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<ReflectedField> getFields() {
		return Enumerables.asEnumerable(type().getFields()).map(this.transformField);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<ReflectedField> getDeclaredFields() {
		return Enumerables.asEnumerable(type().getDeclaredFields()).map(this.transformField);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<ReflectedField> getCallableFields() {
		return getFields().concat(getDeclaredFields());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEnum() {
		return type().isEnum();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSubTypeOf(Class<?> other) {
		return other.isAssignableFrom(type());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<ReflectedProperty> getProperties() {

		Enumerable<ReflectedMethod> all = Enumerables.emptyEnumerable();

		if (this.isInterface()){
			all = all.concat(this.getCallableMethods()).concat(this.getInterfacesMethods()).distinct();
		} else {

			ReflectedClass<?> topType = this;
			while ( topType != null && !topType.equals(Object.class)){
				all = all.concat(topType.getCallableMethods());
				topType = topType.getSuperclass();
			}
		}

		Set<String> propertyNames = new HashSet<String>();
		Enumerable<ReflectedProperty> result = Enumerables.emptyEnumerable();

		for (ReflectedMethod m : all){
			final Enumerable<ReflectedParameter> parameters = m.getParameters();

			if (parameters.isEmpty()){
				if (!m.getName().startsWith("getClass")){
					if (m.getName().startsWith("get") ){
						final String propertyName = StringUtils.firstLetterToLower(m.getName().substring(3));

						if (propertyNames.add(propertyName)){
							result = result.concat(getPropertyAccessor(propertyName));
						}

					} else if (m.getName().startsWith("is")){
						final String propertyName = StringUtils.firstLetterToLower(m.getName().substring(2));

						if (propertyNames.add(propertyName)){
							result = result.concat(getPropertyAccessor(propertyName));
						}
					} 
				}
			} else if (parameters.size() == 1 && m.getName().startsWith("set")){
				final String propertyName = StringUtils.firstLetterToLower(m.getName().substring(3));

				if (propertyNames.add(propertyName)){
					result = result.concat(getPropertyAccessor(propertyName));
				}
			}
		}

		return result;
	}

	public ReflectedProperty getPropertyAccessor(String propertyName){
		return JavaPropertyAccessor.forTypeAndName(this,propertyName);
	}

	public Enumerable<ReflectedClass<?>> getDeclaredInterfaces() {
		Enumerable<Class<?>> all = Enumerables.emptyEnumerable();
		Class<?> superType = type();
		while (superType!=null && !superType.equals(Object.class)){
			all = all.concat(Enumerables.asEnumerable(superType.getInterfaces())); // interfaces in class

			// up to super class
			superType = superType.getSuperclass();
		}

		return all.map(this.transformClass);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReflectedMethod getMethod(String name, Class<?>... types) {
		try {
			return new JavaReflectedMethod(type().getMethod(name, types));
		} catch (SecurityException e) {
			throw new IllegalAccessReflectionException(e);
		} catch (NoSuchMethodException e) {
			throw new NoSuchMethodReflectionException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalAccessReflectionException(e);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <A extends Annotation> Maybe<A> getAnnotation(
			Class<A> annotationClass) {
		return Maybe.of(type().getAnnotation(annotationClass));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEnhanced() {
		return Reflector.getReflector().isEnhanced(type());
	}


	public IntrospectionCriteriaBuilder<T> inspect(){
		return new IntrospectionCriteriaBuilder<T>(this);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getModifiers() {
		return type().getModifiers();
	}

	public String toString(){
		return this.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsMethod(ReflectedMethod method) {
		try {
			this.type().getMethod(method.getName(), method.getParameters().map(new Function<Class<?>, ReflectedParameter>(){

				@Override
				public Class<?> apply(ReflectedParameter object) {
					return object.getType().getReflectedType();
				}
				
			}).asArray());
			return true;
		} catch (SecurityException e) {
			throw ReflectionException.manage(e);
		} catch (NoSuchMethodException e) {
			return false;
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Class<?> other) {
		return this.type.equals(other);
	}

}
