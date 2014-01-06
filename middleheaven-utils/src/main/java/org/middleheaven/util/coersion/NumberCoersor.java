/**
 * 
 */
package org.middleheaven.util.coersion;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


/**
 * 
 */
public class NumberCoersor extends AbstractTypeCoersor<Number, Number> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Number> T coerceForward(Number value, Class<T> type) {
		return (T) coerse(value, type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Number> T coerceReverse(Number value, Class<T> type) {
		return (T) coerse(value, type);
	}

	
	private Number coerse(Number n,  Class type){
		
		if (n == null){
			return null;
		} else if (type.isInstance(n)){
			return (Number) type.cast(n);
		}else if (Double.class.isAssignableFrom(type)){
			return Double.valueOf(n.doubleValue());
		} else if (Float.class.isAssignableFrom(type)){
			return Float.valueOf(n.floatValue());
		} else if (BigDecimal.class.isAssignableFrom(type)){
			return new BigDecimal(n.toString());
		} else if (AtomicInteger.class.isAssignableFrom(type)){
			return new AtomicInteger(n.intValue());
		} else if (AtomicLong.class.isAssignableFrom(type)){
			return new AtomicLong(n.longValue());
		} else if (Long.class.isAssignableFrom(type)){
			return Long.valueOf(n.longValue());
		} else if (Integer.class.isAssignableFrom(type)){
			return Integer.valueOf(n.intValue());
		}else if (Short.class.isAssignableFrom(type)){
			return Short.valueOf(n.shortValue());
		}else if (Byte.class.isAssignableFrom(type)){
			return Byte.valueOf(n.byteValue());
		} else {
			throw new CoersionException("Cannot coerce " + n + " to " + type);
		}
	}

}
