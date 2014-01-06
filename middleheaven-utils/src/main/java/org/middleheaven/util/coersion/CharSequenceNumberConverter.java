package org.middleheaven.util.coersion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Coersor for  {@link CharSequence} and {@link Number}.
 */
public class CharSequenceNumberConverter extends AbstractTypeCoersor<CharSequence, Number> {

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Number> T coerceForward(CharSequence cvalue, Class<T> targetClass) {
		
		if (targetClass==null){
			 throw new IllegalArgumentException("Target class is required");
		}
		if (cvalue==null){
			return null;
		}
		
		BigDecimal value = new BigDecimal(cvalue.toString());
		
        if (targetClass.equals(Byte.class)){
            return targetClass.cast(Byte.valueOf(value.byteValue()));
        } else if (targetClass.equals(Short.class)){
            return targetClass.cast(Short.valueOf(value.shortValue()));
        } else if (targetClass.equals(Integer.class)){
            return targetClass.cast(Integer.valueOf(value.intValue()));
        } else if (targetClass.equals(Long.class)){
            return targetClass.cast(Long.valueOf(value.longValue()));
        } else if (targetClass.equals(BigDecimal.class)){
            return targetClass.cast(new BigDecimal(value.toString()));
        } else if (targetClass.equals(BigInteger.class)){
            return targetClass.cast(new BigInteger(value.toString()));
        } else if (targetClass.equals(Double.class)){
            return targetClass.cast(Double.valueOf(value.doubleValue()));
        } else if (targetClass.equals(Float.class)){
            return targetClass.cast(Float.valueOf(value.floatValue()));
        } else if (targetClass.equals(AtomicLong.class)){
            return targetClass.cast(new AtomicLong(value.longValue()));
        } else if (targetClass.equals(AtomicInteger.class)){
            return targetClass.cast(new AtomicInteger(value.intValue()));
        } else {
            throw new IllegalArgumentException("Cannot convert " + value.getClass().getName() + " to " + targetClass.getName());
        }
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public <T extends CharSequence> T coerceReverse(Number value, Class<T> targetClass) {
		if (targetClass.equals(String.class)){
			return targetClass.cast(value.toString());
		} else if (targetClass.equals(StringBuilder.class)){
			return targetClass.cast(new StringBuilder(value.toString()));
		}else if (targetClass.equals(StringBuffer.class)){
			return targetClass.cast(new StringBuffer(value.toString()));
		} else {
			throw new IllegalArgumentException("Cannot convert " + value.getClass().getName() + " to " + targetClass.getName());
		}
	}





}
