package org.middleheaven.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

/**
 * Collected methods which allow easy implementation of <code>hashCode</code>.
 *
 * Example use case:
 * <pre>
 *  public int hashCode(){
 *   
 *    //collect the contributions of various fields
 *    return Hash.hash(fPrimitive)
 *    .hash(fObject);
 *    .hash(fArray)
 *    .hashCode();
 *
 *  }
 * </pre>
 * 
 * @see http://www.javapractices.com/Topic28.cjp
 */
public final class Hash {

	private Hash(){}

	/**
	 * 
	 * Note that byte and short are handled by this method, through
	 * implicit conversion.
	 * @return hash code for an integer
	 */
	public static HashBuilder hash(int value){
		return new HashBuilder().hash(value);
	}
	
	public static HashBuilder hash(int[] value){
		return new HashBuilder().hash(value);
	}

	public static HashBuilder hash(boolean value){
		return new HashBuilder().hash(value);
	}

	public static HashBuilder hash(char value){
		return new HashBuilder().hash(value);
	}
	
	public static HashBuilder hash(float value){
		return new HashBuilder().hash(value);
	}
	
	public static HashBuilder hash(double value){
		return new HashBuilder().hash(value);
	}
	
	public static HashBuilder hash(Object value){
		return new HashBuilder().hash(value);
	}
	
	public static HashBuilder hash(Collection<?> value){
		return new HashBuilder().hash(value);
	}

	public final static class HashBuilder {

		private static final int ODD_PRIME_NUMBER = 37;
		/**
		 * An initial value for a <code>hashCode</code>, to which is added contributions
		 * from fields. Using a non-zero value decreases collisons of <code>hashCode</code>
		 * values.
		 */
		public static final int SEED = 23;

		private int hash;
		public HashBuilder(){
			this.hash = SEED; 
		}

		public HashBuilder hash(int value){
			/*
			 * Implementation Note
			 * Note that byte and short are handled by this method, through
			 * implicit conversion.
			 */
			this.hash = ODD_PRIME_NUMBER * hash + value;
			return this;
		}
		
		public HashBuilder hash(int[] value){
			/*
			 * Implementation Note
			 * Note that byte and short are handled by this method, through
			 * implicit conversion.
			 */
			for ( int i = 0; i < value.length; i++ ) {
				this.hash(value[i]);
			}
			return this;
		}
		
		public HashBuilder hash(boolean value){
			this.hash = ODD_PRIME_NUMBER * hash +  ( value ? 1 : 0 );
			return this;
		}
		
		public HashBuilder hash(char value){
			this.hash = ODD_PRIME_NUMBER * hash +  value;
			return this;
		}
		
		public HashBuilder hash(long value){
			this.hash = ODD_PRIME_NUMBER * hash +  (int)( value ^ (value >>> 32) );
			return this;
		}
		
		public HashBuilder hash(float value){
			this.hash(Float.floatToIntBits(value));
			return this;
		}
		
		public HashBuilder hash(double value){
			this.hash(Double.doubleToLongBits(value));
			return this;
		}
		
		/**
		 * <code>aObject</code> is a possibly-null object field, and possibly an array.
		 *
		 * If <code>aObject</code> is an array, then each element may be a primitive
		 * or a possibly-null object.
		 */
		public HashBuilder hash(Object value){
			
			if ( value == null) {
				hash(0);
			} else if ( !value.getClass().isArray()) {
				hash(value.hashCode());
			}else {
				int length = Array.getLength(value);
				for ( int idx = 0; idx < length; ++idx ) {
					Object item = Array.get(value, idx);
					//recursive call!
					hash(item);
				}
			}
			return this;
			
		}
		
		public HashBuilder hash(Collection<?> value){
	
			for (Iterator<?> it = value.iterator();it.hasNext(); ) {
				//recursive call!
				hash(it.next());
			}
			return this;
		}
		
		public int hashCode(){
			return hash;
		}
		
		public boolean equals (Object other){
			return other instanceof HashBuilder && this.hash == ((HashBuilder)other).hash; 
		}
		
		public String toString(){
			return Integer.toString(this.hash);
		}
	}










} 


