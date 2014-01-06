package org.middleheaven.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * {@link Incrementor} implementation for any {@link java.lang.Number}.
 */
public class NumberIncrementor<N extends Number & Comparable> implements Incrementor<N,N> {

	java.lang.Number step;
	private boolean reversed;
	
	/**
	 * 
	 * Constructor.
	 * @param step the setp to increment by
	 */
	public NumberIncrementor(java.lang.Number step){
		this(step, false);
	}
	
	NumberIncrementor(java.lang.Number step, boolean reversed){
		this.step = step;
		this.reversed = reversed;
	}
	
	@Override
	public N increment(java.lang.Number object) {
		try {
			Method m = this.getClass().getDeclaredMethod("increment", object.getClass());
			return (N)m.invoke(this, object);
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot increment " + object.getClass(),e);
		} 
	}

	/**
	 * Inrement a byte value
	 * @param b the byte value to increment
	 * @return the next byte in ascending order
	 */
	protected java.lang.Number increment(Byte b){
		return Byte.valueOf((byte)(b.byteValue()+step.byteValue()));
	}
	
	/**
	 * Inrement an integer value
	 * @param b the integer value to increment
	 * @return the next integer in ascending order
	 */
	protected java.lang.Number increment(java.lang.Integer b){
		return  java.lang.Integer.valueOf(b.intValue()+step.intValue());
	}
	
	/**
	 * Inrement a short value
	 * @param b the short value to increment
	 * @return the next short in ascending order
	 */
	protected java.lang.Number increment(Short b){
		return Short.valueOf((short)(b.shortValue()+step.shortValue()));
	}
	
	/**
	 * Inrement a long value
	 * @param b the long value to increment
	 * @return the next long in ascending order
	 */
	protected java.lang.Number increment(Long b){
		return Long.valueOf(b.longValue()+step.longValue());
	}
	
	/**
	 * Inrement a double value
	 * @param b the double value to increment
	 * @return the next double in ascending order
	 */
	protected java.lang.Number increment(Double b){
		return  Double.valueOf(b.doubleValue()+step.doubleValue());
	}
	
	/**
	 * Inrement a float value
	 * @param b the float value to increment
	 * @return the next float in ascending order
	 */
	protected java.lang.Number increment(Float b){
		return  Float.valueOf(b.floatValue()+step.floatValue());
	}
	
	/**
	 * Inrement a {@link BigDecimal} value
	 * @param b the {@link BigDecimal} value to increment
	 * @return the next {@link BigDecimal} in ascending order
	 */
	protected java.lang.Number increment(BigDecimal b){
		return  ((BigDecimal)b).add(new BigDecimal(step.toString()));
	}
	
	/**
	 * Inrement a {@link BigInteger} value
	 * @param b the {@link BigInteger} value to increment
	 * @return the next {@link BigInteger} in ascending order
	 */
	protected java.lang.Number increment(BigInteger b){
		return  ((BigInteger)b).add(new BigInteger(step.toString()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Incrementor<N, N> reverse() {
		return new NumberIncrementor((new BigDecimal(step.toString())).negate(), !this.reversed);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Incrementor<N, N> withStep(Number step) {
		NumberIncrementor inc = new NumberIncrementor(step);
		if (this.reversed){
			return inc.reverse();
		}
		return inc;
	}
}
