package org.middleheaven.core.reflection.inspection;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.middleheaven.core.reflection.ClassCastReflectionException;
import org.middleheaven.core.reflection.IllegalAccessReflectionException;
import org.middleheaven.core.reflection.InvocationTargetReflectionException;
import org.middleheaven.core.reflection.NoSuchClassReflectionException;
import org.middleheaven.core.reflection.NoSuchMethodReflectionException;
import org.middleheaven.core.reflection.ProxyHandler;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.Enumerable;
import org.middleheaven.util.function.Maybe;


/**
 * 
 *
 * @param <T> the introspected class
 */
public class ClassIntrospector<T> extends Introspector{

	private Class<T> type;

	public static boolean isInClasspath(String className) {
		try {
			Class.forName(className, false, ClassIntrospector.class.getClassLoader());
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	ClassIntrospector(Class<T> type) {
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	public static ClassIntrospector<?> loadFrom( String className){
		// this does not delegate to load(string,classloaded) because inicialization control
		try{
			// TODO do without creating the class object
			Class type =  Class.forName(className, false, ClassIntrospector.class.getClassLoader());
			return new ClassIntrospector(type);
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	}

	/**
	 * Loads a subclass of the introspected class from its name.
	 * 
	 * @param className
	 * @return a {@code ClassIntrospector} for the loaded class
	 * @throws NoSuchClassReflectionException if the class is not found on the classpath
	 * @throws ClassCastReflectionException if the loaded class is not a subclass of the introspected class 
	 */
	public ClassIntrospector<T> load( String className){
		// this does not delegate to load(string,classloaded) because inicialization control
		try{
			@SuppressWarnings("unchecked")
			Class<T> type = (Class<T>) Class.forName(className, false, this.getClass().getClassLoader()).asSubclass(this.type);
			return new ClassIntrospector<T>(type);
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		} catch (ClassCastException e){
			throw new ClassCastReflectionException(className , this.type.getName());
		}
	}


	/**
	 * Determines if the instrospected type is annotated with a sepecific annotation.
	 * This method will process meta-annotations 
	 * 
	 * @param annotationClass the annotation type to search.
	 * @return <code>true</code> if the instrospected type has this aanotation, <code>false</code> otherwise
	 */
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {

		if (this.type.isAnnotationPresent(annotationClass)){
			return true;
		}

		if (isMetaAnnotation(annotationClass)){
			
			for (Annotation a : type.getAnnotations()){
				if (isMetaAnnotationPresent(a, annotationClass)){;
					return true;
				}
			}
			
		}
		
		return false;
	}
	
	@Override
	public <A extends Annotation> boolean isAnnotadedWith(Class<A> annotationClass) {
		return type.isAnnotationPresent(annotationClass);
	}

	private boolean isMetaAnnotationPresent(Annotation annotation,Class<? extends Annotation> annotationClass) {
		return isMetaAnnotationPresent(annotation,annotationClass, new HashSet());
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
			Class<T> type = (Class<T>) loader.loadClass(className).asSubclass(this.type);
			return new ClassIntrospector<T>(type);
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	}


	public Class<T> getIntrospected(){
		return this.type;
	}

	public T cast (Object object){
		return type.cast(object);
	}

	public String getCanonicalName (){
		return type.getCanonicalName();
	}

	public String getName (){
		return type.getName();
	}

	public String getSimpleName (){
		return type.getSimpleName();
	}

	public Type[] getActualTypeArguments(){
		Type type  = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType){
			ParameterizedType parameterizedType = (ParameterizedType) type;
			return parameterizedType.getActualTypeArguments();
		} else {
			return new Type[0];
		}

	}
	public IntrospectionCriteriaBuilder<T> inspect(){
		return new IntrospectionCriteriaBuilder<T>(this.type);
	}

	public PackageIntrospector getPackage (){
		return Introspector.of(type.getPackage());
	}

	public T newInstance (Object ... args){
		return Reflector.getReflector().newInstance(this.type, args);
	}

	public T newInstance (ClassLoader otherClassLoader){
		return Reflector.getReflector().newInstance(this.type, otherClassLoader);
	}

	public T newProxyInstance(ProxyHandler handler){
		if(!type.isInterface()){
			throw new UnsupportedOperationException("Cannot proxy " + type.getName() + " as interface");
		}
		return Reflector.getReflector().proxyType(type, handler);
	} 

	public <I> I newProxyInstance(ProxyHandler handler, Class<I> proxyInterface ,Class<?> ... adicionalInterfaces){
		if(!proxyInterface.isInterface()){
			throw new UnsupportedOperationException("Cannot proxy " + type.getName() + ".Type is not an interface");
		}
		return Reflector.getReflector().proxyType(type, handler, proxyInterface, adicionalInterfaces);
	} 

	public boolean isInstance (Object obj){
		return type.isInstance(obj);
	}

	@Override
	public <A extends Annotation> Maybe<A> getAnnotation(Class<A> annotationClass) {
		return Maybe.of(type.getAnnotation(annotationClass));
	}

	/**
	 * 
	 */
	public Annotation[] getAnnotations() {
		return type.getAnnotations();
	}

	

	public void invokeMain(String ... params) {
		try {
			Method methodToInvoke = type.getMethod("main", String[].class);
			methodToInvoke.setAccessible(true);
			final Object args = params; 
			methodToInvoke.invoke(null, args);
		} catch (SecurityException e) {
			throw new IllegalAccessReflectionException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalAccessReflectionException(e);
		} catch (InvocationTargetException e) {
			throw new InvocationTargetReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalAccessReflectionException(e);
		} catch (NoSuchMethodException e) {
			throw new NoSuchMethodReflectionException(e);
		}
	}

	/**
	 * Indicates if the class is enhanced by byte code re-writing.
	 * @return <code>true</code> if the class is enhanced by byte code re-writing, <code>false</code> otherwise.
	 */
	public boolean isEnhanced() {
		return Reflector.getReflector().isEnhanced(type);
	}

	public Enumerable<Class<?>> getImplementedInterfaces() {
		return CollectionUtils.asEnumerable(type.getInterfaces()); 
	}

	public Enumerable<Class<?>> getDeclaredInterfaces() {
		List<Class<?>> all = new LinkedList<Class<?>>();
		Class<?> superType = type;
		while (superType!=null && !superType.equals(Object.class)){
			all.addAll(Arrays.asList(superType.getInterfaces())); // interfaces in class

			// up to super class
			superType = superType.getSuperclass();
		}

		Class<?>[] result = new Class<?>[all.size()]; 
		return CollectionUtils.asEnumerable(all.toArray(result));
	}

	/**
	 * Informs if the instrospected type is fundamental. Fundamental types are thoses primitive, respective wrappers,
	 * {@code String} and {@code Date}
	 * 
	 * @return {@code true} if the instrospected type is fundamental, {@code false] otherwise.
	 */
	public boolean isFundamental() {
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
	 * 
	 */
	public boolean isPrimitive() {
		return type.isPrimitive();
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

		return loadFrom("java.lang.".concat(name)).getIntrospected();
	}

	/**
	 * @param class1
	 * @return
	 */
	public boolean isSubtypeOf(Class<?> otherType) {
		return otherType.isAssignableFrom(type);
	}

	/**
	 * The first super class that descends from {@link Object};
	 * @return an absent {@link Maybe} if this is directly descendent form Object, of a not absent {@link Maybe} if a super class can be found
	 */
	public Maybe<Class> getRootParent() {
		
		if (type.getSuperclass().getName().equals(Object.class.getName())){
			return Maybe.absent();
		} 
		
		Class top = type.getSuperclass();
		
		while (!top.getSuperclass().getName().equals(Object.class.getName())){
			 top = type.getSuperclass();
		}
		
		return Maybe.of(top);
	}






}
