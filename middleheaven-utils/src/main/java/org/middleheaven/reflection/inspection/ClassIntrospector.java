package org.middleheaven.reflection.inspection;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.reflection.ClassCastReflectionException;
import org.middleheaven.reflection.IllegalAccessReflectionException;
import org.middleheaven.reflection.InvocationTargetReflectionException;
import org.middleheaven.reflection.NoSuchClassReflectionException;
import org.middleheaven.reflection.NoSuchMethodReflectionException;
import org.middleheaven.reflection.ProxyHandler;
import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.ReflectedMethod;
import org.middleheaven.reflection.Reflector;
import org.middleheaven.util.Maybe;
import org.middleheaven.util.StringUtils;


/**
 * 
 *
 * @param <T> the introspected class
 */
public class ClassIntrospector<T> extends Introspector{

	private ReflectedClass<T> type;

	public static boolean isInClasspath(String className) {
		return Reflector.getReflector().isInClasspath(className);
	}

	ClassIntrospector(ReflectedClass<T> type) {
		this.type = type;
	}

	/**
	 * Creates a {@link ClassIntrospector} for the classe given name.
	 * @param className the name of the class that will be used to create the {@link ClassIntrospector}.
	 * @return the created {@link ClassIntrospector}.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ClassIntrospector<?> loadFrom( String className){
		return new ClassIntrospector(Reflector.getReflector().loadClass(className));
	}

	/**
	 * Loads a subclass of the introspected class from its name.
	 * 
	 * @param className
	 * @return a {@code ClassIntrospector} for the loaded class
	 * @throws NoSuchClassReflectionException if the class is not found on the classpath
	 * @throws ClassCastReflectionException if the loaded class is not a subclass of the introspected class 
	 */
	@SuppressWarnings("rawtypes")
	public ClassIntrospector<T> load( String className){
		return new ClassIntrospector(Reflector.getReflector().loadClass(className, this.type));
	}


	/**
	 * Determines if the instrospected type is annotated with a sepecific annotation.
	 * This method will process meta-annotations 
	 * 
	 * @param annotationClass the annotation type to search.
	 * @return <code>true</code> if the instrospected type has this aanotation, <code>false</code> otherwise
	 */
	public <A extends Annotation> boolean isAnnotationPresent(Class<A> annotationClass) {

		if (this.type.isAnnotationPresent(annotationClass)){
			return true;
		}

		if (isMetaAnnotation(annotationClass)){
			
			for (Annotation a : type.getAnnotations()){
				if (isMetaAnnotationPresent(a, annotationClass)){
					return true;
				}
			}
			
		}
		
		return false;
	}
	
	private boolean isMetaAnnotationPresent(Annotation annotation,Class<? extends Annotation> annotationClass) {
		return isMetaAnnotationPresent(annotation,annotationClass, new HashSet<Class<?>>());
	}

	private boolean isMetaAnnotationPresent(Annotation a, Class<? extends Annotation> annotationClass, Set<Class<?>> visited) {

		if (a.annotationType().equals(annotationClass)){
			
			return true;

		} else if (a.annotationType().isAnnotationPresent(annotationClass)){

			return true;

		} else if (visited.add(a.annotationType())){
	
			for (Annotation b : a.annotationType().getAnnotations()){
				if (isMetaAnnotationPresent(b, annotationClass, visited)){
					return true;
				}
			}
	
		}
		
		return false;

	}

	

	private boolean isMetaAnnotation(Class<? extends Annotation> annotationClass){
		// detect meta-annotation
		Target target = annotationClass.getAnnotation(Target.class);

		if ( target != null){
			for (ElementType element : target.value()){
				if (element.equals(ElementType.ANNOTATION_TYPE)){
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * Loads a subclass of the introspected class from its name using the given {@code ClassLoader}.
	 * 
	 * @param className
	 * @return a {@code ClassIntrospector} for the loaded class
	 * @throws NoSuchClassReflectionException if the class is not found on the classpath
	 * @throws ClassCastReflectionException if the loaded class is not a subclass of the introspected class 
	 */

	public ClassIntrospector<T> load( String className, ClassLoader loader){
		try{
			@SuppressWarnings("unchecked")
			Class<T> atype = (Class<T>) loader.loadClass(className).asSubclass(this.type.getReflectedType());
			return new ClassIntrospector<T>(Reflector.getReflector().reflect(atype));
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className, e);
		}
	}


	public ReflectedClass<T> getIntrospected(){
		return this.type;
	}

	public T cast (Object object){
		return type.cast(object);
	}

	public Type[] getActualTypeArguments(){
		Type genericSuperType  = getClass().getGenericSuperclass();
		if (genericSuperType instanceof ParameterizedType){
			ParameterizedType parameterizedType = (ParameterizedType) genericSuperType;
			return parameterizedType.getActualTypeArguments();
		} else {
			return new Type[0];
		}

	}
	public IntrospectionCriteriaBuilder<T> inspect(){
		return new IntrospectionCriteriaBuilder<T>(this.type);
	}

	public PackageIntrospector getPackage (){
		return Introspector.of(type.getReflectedType().getPackage());
	}

	public T newInstance (Object ... args){
		return type.newInstance(args);
	}

	public T newInstance (ClassLoader otherClassLoader, Object ... args){
		return type.newInstance(otherClassLoader, args);
	}

	public T newProxyInstance(ProxyHandler handler, Object ... args){
		return Reflector.getReflector().proxyType(type.getReflectedType(), handler, args);
	} 

	public <I> I newProxyInstance(ProxyHandler handler, Class<I> proxyInterface ,Class<?> ... adicionalInterfaces){
		if(!proxyInterface.isInterface()){
			throw new UnsupportedOperationException("Cannot proxy " + type.getName() + ".Type is not an interface");
		}
		return Reflector.getReflector().proxyType(type.getReflectedType(), handler, proxyInterface, adicionalInterfaces);
	} 

	@Override
	public <A extends Annotation> Maybe<A> getAnnotation(Class<A> annotationClass) {
		return type.getAnnotation(annotationClass);
	}

	/**
	 * 
	 */
	public Enumerable<Annotation> getAnnotations() {
		return type.getAnnotations();
	}

	

	public void invokeMain(String ... params) {
		try {
		   type.getMethod("main", String[].class).invokeStatic((Object[])params);
		} catch (SecurityException e) {
			throw new IllegalAccessReflectionException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalAccessReflectionException(e);
		}
	}

	/**
	 * Indicates if the class is enhanced by byte code re-writing.
	 * @return <code>true</code> if the class is enhanced by byte code re-writing, <code>false</code> otherwise.
	 */
	public boolean isEnhanced() {
		return Reflector.getReflector().isEnhanced(type.getReflectedType());
	}



	public Class getPrimitiveWrapper(){
		if (!type.isPrimitive()) {
			return null;
		}
		String name = type.getSimpleName();

		if ("int".equals(name)){
			name = "integer";
		} else if ("char".equals(name)){
			name = "character";
		}

		name = StringUtils.firstLetterToUpper(name);

		return loadFrom("java.lang.".concat(name)).getIntrospected().getReflectedType();
	}

	/**
	 * @param class1
	 * @return
	 */
	public boolean isSubtypeOf(Class<?> otherType) {
		return type.isSubTypeOf(otherType);
	}







}
