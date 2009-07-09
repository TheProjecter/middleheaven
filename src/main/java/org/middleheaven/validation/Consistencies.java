package org.middleheaven.validation;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Map;

import org.middleheaven.core.reflection.ClassIntrospector;
import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.util.collections.Interval;
import org.middleheaven.util.collections.Range;


public class Consistencies {

	public static <T> void consistIsValid(T obj, Validator<T> validator){
		consistIsValid(obj,validator, "Parameter is not valid");
	}

	public static <T> void consistIsValid(T obj, Validator<T> validator, String message){
		consistIsValid(obj,validator,message,IllegalArgumentException.class);
	}

	public static <T,E extends Exception > void consistIsValid(T obj, Validator<T> validator, Class<E> exceptionType) throws E{
		consistIsValid(obj,validator,null,exceptionType);
	}

	public static <T,E extends Exception > void consistIsValid(Object obj,  Validator<T> validator, String message,Class<E> exceptionType) throws E{
		new ValdiatorConsistency<T,E>(validator,message,exceptionType).consist(obj);
	}
	
	public static void consistNotNull(Object obj){
		consistNotNull(obj,"Parameter cannot be null");
	}

	public static void consistNotNull(Object obj, String message){
		consistNotNull(obj,message,IllegalArgumentException.class);
	}

	public static <E extends Exception > void consistNotNull(Object obj, Class<E> exceptionType) throws E{
		consistNotNull(obj,null,exceptionType);
	}

	public static <E extends Exception > void consistNotNull(Object obj,  String message,Class<E> exceptionType) throws E{
		new NotNullConsistency<E>(message,exceptionType).consist(obj);
	}
	
	public static void consistNotEmpty(Object object){
		consistNotEmpty(object,"Sequence cannot be empty",IllegalArgumentException.class);
	}

	public static void consistNotEmpty(Object object, String message){
		consistNotEmpty(object,message,IllegalArgumentException.class);
	}

	public static <E extends Exception > void consistNotEmpty(Object obj, Class<E> exceptionType) throws E{
		consistNotEmpty(obj,null,exceptionType);
	}

	public static <E extends Exception > void consistNotEmpty(Object obj,  String message,Class<E> exceptionType) throws E{
		new NotEmptyConsistency<E>(message,exceptionType).consist(obj);
	}
	
	public static void consistIsBoolean(boolean value, Object object, String message){
		consistIsBoolean(value,object,message,IllegalArgumentException.class);
	}

	public static <E extends Exception > void consistIsBoolean(boolean value, Object object, Class<E> exceptionType) throws E{
		consistIsBoolean(value,null,exceptionType);
	}

	public static <E extends Exception > void consistIsBoolean(boolean value,Object obj,  String message,Class<E> exceptionType) throws E{
		new HasBooleanValueConsistency<E>(value,message,exceptionType).consist(obj);
	}
	
	public static <T extends Comparable<T>> void consistIsBetween(T min, T max, T object) {
		consistIsBetween(min,max,object,"Value outside of bounds [" + min + "," + max + "]", IllegalArgumentException.class);
	}
	
	public static <T extends Comparable<T>> void consistIsBetween(T min, T max, T object, String message) {
		consistIsBetween(min,max,object,message, IllegalArgumentException.class);
	}
	
	public static <T extends Comparable<T>,E extends Exception> void consistIsBetween(T min, T max, T object,Class<E> exceptionType) throws E{
		consistIsBetween(min,max,object,null, exceptionType);
	}
	
	public static <T extends Comparable<T>,E extends Exception > void consistIsBetween(T min, T max, T object,  String message,Class<E> exceptionType) throws E{
		InBetweenValueConsistency.newInstance(Range.between(min, max),message,exceptionType).consist(object);
	}
	
	private static abstract class Consistency<E extends Exception>{

		String message;
		Class<E> exceptionType;
		
		public void consist(Object object) throws E {
			
			if (isNotConsistent(object)){
				ClassIntrospector<E> introspector = Introspector.of(exceptionType);
				if (message==null){
					Constructor<E> c=  introspector.inspect().constructors()
					.sortedByQuantityOfParameters().retrive();
					
					Object[] initargs = new Object[c.getParameterTypes().length];
					int i=0;
					for (Class<?> type : c.getParameterTypes()){
						initargs[i] = Introspector.of(type).newInstance();
						i++;
					}
					throw introspector.newInstance(initargs);
				}
				throw introspector.newInstance(new Object[]{message});
			}
		}

		protected abstract  boolean isNotConsistent(Object object);

		public Consistency(String message, Class<E> exceptionType) {
			super();
			this.message = message;
			this.exceptionType = exceptionType;
		}
	}
	
	private static class NotNullConsistency<E extends Exception> extends Consistency<E>{

		public NotNullConsistency(String message, Class<E> exceptionType) {
			super(message, exceptionType);
		}

		@Override
		protected boolean isNotConsistent(Object object) {
			return object==null;
		}
		
	}
	
	private static class NotEmptyConsistency<E extends Exception> extends Consistency<E>{

		public NotEmptyConsistency(String message, Class<E> exceptionType) {
			super(message, exceptionType);
		}

		@Override
		protected boolean isNotConsistent(Object object) {
			
			if (object instanceof CharSequence){
				return ((CharSequence)object).length()==0;
			}else if (object instanceof Collection){
				return ((Collection<?>)object).isEmpty();
			}else if (object instanceof Map){
				return ((Map<?,?>)object).isEmpty();
			} else if (object!=null && object.getClass().isArray()){
				return Array.getLength(object)==0;
			} else {
				return object==null;
			}
		}
		
	}
	
	private static class HasBooleanValueConsistency<E extends Exception> extends Consistency<E>{
		boolean value;
		public HasBooleanValueConsistency(boolean value, String message, Class<E> exceptionType) {
			super(message, exceptionType);
			this.value = value;
		}

		@Override
		protected boolean isNotConsistent(Object object) {
			
			if (object instanceof Boolean){
				return ((Boolean)object).booleanValue() != value;
			}else{
				return true;
			}
		}
		
	}

	private static class InBetweenValueConsistency<T extends Comparable<T>,E extends Exception> extends Consistency<E>{
		Interval<T> interval;
		
		public static  <C extends Comparable<C>,X extends Exception> InBetweenValueConsistency<C,X> newInstance(Interval<C> interval, String message, Class<X> exceptionType){
			return new InBetweenValueConsistency<C,X>(interval,message,exceptionType);
		}
		
		public InBetweenValueConsistency(Interval<T> interval, String message, Class<E> exceptionType) {
			super(message, exceptionType);
			this.interval = interval;
		}

		@Override
		protected boolean isNotConsistent(Object object) {
			return !interval.contains((T)object);
		}
		
	}

	private static class ValdiatorConsistency<T,E extends Exception> extends Consistency<E>{
		Validator<T> validator;
		
		
		public ValdiatorConsistency(Validator<T> validator, String message, Class<E> exceptionType) {
			super(message, exceptionType);
			this.validator = validator;
		}

		@Override
		protected boolean isNotConsistent(Object object) {
			ValidationContext context = new DefaultValidationContext();
			validator.validate(context,(T)object);
			return !context.isStrictlyValid();
		}
		
	}
}
