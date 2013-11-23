package org.middleheaven.global.text;

import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.quantity.time.TimeUtils;
import org.middleheaven.util.coersion.AbstractTypeCoersor;
import org.middleheaven.util.coersion.CoersionException;


public class StringCalendarDateTimeCoersor extends AbstractTypeCoersor<String, CalendarDateTime>{

	TimepointFormatter format;
	public StringCalendarDateTimeCoersor(TimepointFormatter format){
		this.format = format;
	}
	
	@Override
	public <T extends CalendarDateTime> T coerceForward(String value, Class<T> type) {

		try {
			if (value==null || value.trim().isEmpty()){
				return null;
			}
			
			try {
				return type.cast(TimeUtils.from(Long.valueOf(value)));
				
			} catch (NumberFormatException e){
				
				return type.cast(TimeUtils.from(format.parse(value)));
				
			}
			
		} catch (Exception e) {
			throw new CoersionException(e);
		} 
	}

	@Override
	public <T extends String> T coerceReverse(CalendarDateTime value, Class<T> type) {
		if (value==null){
			return null;
		}
		return type.cast(format.format(value));
	}

	

}
