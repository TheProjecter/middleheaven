/**
 * 
 */
package org.middleheaven.quantity.time;

import org.middleheaven.util.coersion.AbstractTypeCoersor;

/**
 * 
 */
public class CalendarDateTimeTypeCoersor extends AbstractTypeCoersor<CalendarDateTime, CalendarDate> {

	public CalendarDateTimeTypeCoersor (){}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends CalendarDate> T coerceForward(CalendarDateTime value, Class<T> type) {
		if (value == null){
			return null;
		}
		
		return type.cast(value.toDate());
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends CalendarDateTime> T coerceReverse(CalendarDate value,
			Class<T> type) {
		
		if (value == null){
			return null;
		}
		
		return type.cast(CalendarDateTime.valueOf(value));
	}

}
