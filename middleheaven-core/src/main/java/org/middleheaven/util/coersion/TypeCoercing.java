package org.middleheaven.util.coersion;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.util.identity.Identity;

/**
 * Main utility class for type coercing.
 * 
 * When type coercing from a type A to a type B, the value represented by type A is preserved in type B.
 * <code>null</code> is coerced to <code>null</code>.
 * 
 */
public class TypeCoercing {

	private static final Map<Key, TypeCoersor> CONVERTERS = new HashMap<Key, TypeCoersor>();

	static {
		// text
		addCoersor(CharSequence.class, Number.class , new CharSequenceNumberConverter());
		addCoersor(CharSequence.class, Boolean.class , new CharSequenceBooleanCoersor());
		addCoersor(String.class, Identity.class , new StringIdentityCoersor());
		addCoersor(String.class, LocalizableText.class , new StringTextLocalizableCoersor());
		
		addCoersor(Number.class, Number.class ,  new NumberCoersor());
		
		// numbers
		addCoersor(Integer.class, Identity.class ,  NumberIdentityCoersor.newInstance(Integer.class));
		addCoersor(Long.class, Identity.class ,  NumberIdentityCoersor.newInstance(Long.class));
		// dates
		addCoersor(Timestamp.class, java.util.Date.class ,  DateTypeCoersor.newInstance(Timestamp.class));
		addCoersor(java.sql.Date.class, java.util.Date.class ,  DateTypeCoersor.newInstance(java.sql.Date.class));
		addCoersor(Time.class, java.util.Date.class ,  DateTypeCoersor.newInstance(Time.class));
		
		addCoersor(CalendarDate.class ,java.util.Date.class,  CalendarDateTypeCoersor.getInstance(CalendarDate.class,java.util.Date.class));
		addCoersor(CalendarDate.class ,java.sql.Date.class,  CalendarDateTypeCoersor.getInstance(CalendarDate.class,java.sql.Date.class));
		addCoersor(CalendarDate.class ,java.sql.Timestamp.class,  CalendarDateTypeCoersor.getInstance(CalendarDate.class,java.sql.Timestamp.class));
		addCoersor(CalendarDate.class ,java.sql.Time.class,  CalendarDateTypeCoersor.getInstance(CalendarDate.class,java.sql.Time.class));
		
		addCoersor(CalendarDateTime.class ,java.util.Date.class,  CalendarDateTypeCoersor.getInstance(CalendarDateTime.class,java.util.Date.class));
		addCoersor(CalendarDateTime.class ,java.sql.Date.class,  CalendarDateTypeCoersor.getInstance(CalendarDateTime.class,java.sql.Date.class));
		addCoersor(CalendarDateTime.class ,java.sql.Timestamp.class,  CalendarDateTypeCoersor.getInstance(
				CalendarDateTime.class,
				java.sql.Timestamp.class
				)
		);
		addCoersor(CalendarDateTime.class ,java.sql.Time.class,  CalendarDateTypeCoersor.getInstance(CalendarDateTime.class,java.sql.Time.class));
		addCoersor(CalendarDateTime.class ,CalendarDate.class,  new CalendarDateTimeTypeCoersor());
		
		addCoersor(String.class ,Enum.class,  new EnumNameTypeCoersor());
		
	}
	
	private TypeCoercing (){}
	
	/**
	 * Converts a value represented in one type to the same value represented by another type.
	 * 
	 * If {@code type} is a primitive type, the conversion 
	 * @param <O> value original type
	 * @param <T> target conversion type. 
	 * @param value the value to be converted
	 * @param type the target type. 
	 * @return The same value represented in the {@code type} type.
	 */
	
	@SuppressWarnings("unchecked")
	public static <O,T> T coerce (O value , Class<T> type ){
		if (value==null){
			return null;
		} else if (type.isAssignableFrom(java.util.Date.class)){
			// dates are handled differently
			if(value.getClass().equals(type)){
				return type.cast(value);
			}
			@SuppressWarnings("unchecked") Class<O> valueClass = (Class<O>) value.getClass();
			return getCoersor(valueClass, type).coerceForward(value, type);
		} else if (type.isInstance(value)){
			return type.cast(value);
		} else if (type.isPrimitive()){
			// cannot type.cast the result because type is a primitive
			
			if (type.isInstance(value)){
				return (T)value;
			} else if (type.equals(Boolean.TYPE)){
				return (T) Boolean.valueOf(((Boolean) coerce(value, Boolean.class)).booleanValue());
			} else {
				BigInteger big = new BigInteger(value.toString());
				if (type.equals(Byte.TYPE)){
					checkOverflow(big, new BigInteger(String.valueOf(Byte.MAX_VALUE)));
					return (T)Byte.valueOf(big.byteValue());
				} else if (type.equals(Short.TYPE)){
					checkOverflow(big, new BigInteger(String.valueOf(Short.MAX_VALUE)));
					return (T)Short.valueOf(big.shortValue());
				} else if (type.equals(Integer.TYPE)){
					checkOverflow(big, new BigInteger(String.valueOf(Integer.MAX_VALUE)));
					return (T)Integer.valueOf(big.intValue());
				} else if (type.equals(Character.TYPE)){
					checkOverflow(big, new BigInteger(String.valueOf(Character.MAX_VALUE)));
					return (T) Character.valueOf((char)big.intValue());
				} else if (type.equals(Long.TYPE)){
					checkOverflow(big, new BigInteger(String.valueOf(Long.MAX_VALUE)));
					return (T)Long.valueOf(big.longValue());
				}
			}
		} 
		
		
		@SuppressWarnings("unchecked") Class<O> valueClass = (Class<O>) value.getClass();
		return getCoersor(valueClass, type).coerceForward(value, type);
	}

	private static void checkOverflow(BigInteger value, BigInteger max){
		if (value.compareTo(max) > 0){
			throw new CoersionException("Overflow convertion." + value + " is greater than " + max);
		}
	}
	public static <O,R> void addCoersor(Class<O> from , Class<R> to, TypeCoersor<O,R> converter){
		CONVERTERS.put(new Key(from,to), converter);
		CONVERTERS.put(new Key(to,from), converter.inverse());
	}

	/**
	 * 
	 * @param <O>
	 * @param <R>
	 * @param from
	 * @param to
	 */
	public static <O,R> void removeCoersor(Class<O> from, Class<R> to) {
		CONVERTERS.remove(new Key(from,to));
		CONVERTERS.remove(new Key(to,from));
	}  
	
	
	
	@SuppressWarnings("unchecked")
	public static <O,R> TypeCoersor<O,R> getCoersor(Class<O> from , Class<R> to){
		
		if (to.isPrimitive()){
			String wrapper = to.getSimpleName().substring(0,1).toUpperCase() + to.getSimpleName().substring(1);
			if (to.getSimpleName().equals("int")){
				wrapper = "Integer";
			}
			to = (Class<R>)Introspector.of(Object.class).load("java.lang."+wrapper).getIntrospected();
			
		}
		Key key = new Key(from,to);
		TypeCoersor<O,R> converter = CONVERTERS.get(key);

		if (converter==null){
			throw new CoersionException("TypeCoersor from " + from + " to " + to + " has not found");
		}
		return converter;
	}

	private static class Key{
		
		public Class<?> from;
		public Class<?> to;


		public Key(Class<?> from, Class<?> to) {
			super();
			this.from = from;
			this.to = to;
		}


		@Override
		public int hashCode() {
			return 0 ;
		}

		@Override
		public boolean equals(Object other) {
			return other instanceof Key && isCompatible(this, (Key)other);
		}
		
		private static boolean isCompatible(Key a , Key b ){
			return a.from.isAssignableFrom(b.from) && a.to.isAssignableFrom(b.to) ||
			b.from.isAssignableFrom(a.from) && b.to.isAssignableFrom(a.to);
		}
	}

	/**
	 * @param array the array to coerce.
	 * @param type the type to coerce each element.
	 * 
	 * @param <T> original type
	 * @param <R> coerced type.
	 * 
	 * @return a new array of the new type with each element being the result of coercing the conterpart element in the <code>array</code>.
	 * 
	 * 
	 */
	public static <T, R> R[] coerceArray(T[] array, Class<R> type) {

		if (array == null){
			return null;
		}
		
		if (type == null){
			throw new IllegalArgumentException("Type is required");
		}
		
		@SuppressWarnings("unchecked")
		R[] result = (R[])Array.newInstance(type, array.length);
		
		for (int i =0 ; i < array.length; i++) {
			result[i] = coerce(array[i], type);
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param array
	 * @param type
	 * @param length
	 * @return
	 */
	public static <T, R> R[] coerceArray(T[] array, Class<R> type, int length) {
		if (array == null){
			return null;
		}
		
		if (type == null){
			throw new IllegalArgumentException("Type is required");
		}
		
		@SuppressWarnings("unchecked")
		R[] result = (R[])Array.newInstance(type, length);
		
		int top = Math.min(length, array.length);
		
		for (int i =0 ; i < top; i++) {
			result[i] = coerce(array[i], type);
		}
		
		return result;
	}

	
}
