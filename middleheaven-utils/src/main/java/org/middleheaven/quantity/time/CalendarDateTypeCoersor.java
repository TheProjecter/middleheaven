package org.middleheaven.quantity.time;

import java.util.Date;

import org.middleheaven.reflection.inspection.Introspector;
import org.middleheaven.util.coersion.AbstractTypeCoersor;
import org.middleheaven.util.coersion.TypeCoercing;

public class CalendarDateTypeCoersor<CD extends CalendarDateTime, D extends Date> extends AbstractTypeCoersor<CD,D> {

	static {
		TypeCoercing.addCoersor(CalendarDate.class ,java.util.Date.class,  CalendarDateTypeCoersor.getInstance(CalendarDate.class,java.util.Date.class));
		TypeCoercing.addCoersor(CalendarDate.class ,java.sql.Date.class,  CalendarDateTypeCoersor.getInstance(CalendarDate.class,java.sql.Date.class));
		TypeCoercing.addCoersor(CalendarDate.class ,java.sql.Timestamp.class,  CalendarDateTypeCoersor.getInstance(CalendarDate.class,java.sql.Timestamp.class));
		TypeCoercing.addCoersor(CalendarDate.class ,java.sql.Time.class,  CalendarDateTypeCoersor.getInstance(CalendarDate.class,java.sql.Time.class));
		
		TypeCoercing.addCoersor(CalendarDateTime.class ,java.util.Date.class,  CalendarDateTypeCoersor.getInstance(CalendarDateTime.class,java.util.Date.class));
		TypeCoercing.addCoersor(CalendarDateTime.class ,java.sql.Date.class,  CalendarDateTypeCoersor.getInstance(CalendarDateTime.class,java.sql.Date.class));
		TypeCoercing.addCoersor(CalendarDateTime.class ,java.sql.Timestamp.class,  CalendarDateTypeCoersor.getInstance(
				CalendarDateTime.class,
				java.sql.Timestamp.class
				)
		);
		TypeCoercing.addCoersor(CalendarDateTime.class ,java.sql.Time.class,  CalendarDateTypeCoersor.getInstance(CalendarDateTime.class,java.sql.Time.class));
		TypeCoercing.addCoersor(CalendarDateTime.class ,CalendarDate.class,  new CalendarDateTimeTypeCoersor());
		
	}
	
	public static <F extends CalendarDateTime,E extends Date>  CalendarDateTypeCoersor<F,E> getInstance(Class<F> calendarDateTime, Class<E> date){
		return new CalendarDateTypeCoersor<F,E>(calendarDateTime,date);
	}

	private Class<CD> calendarDateTimeType;
	private Class<D> dateType;
	
	private CalendarDateTypeCoersor(Class<CD> calendarDateTimeType, Class<D> dateType){
		this.calendarDateTimeType = calendarDateTimeType;
		this.dateType = dateType;
	}

	@Override
	public <T extends D> T coerceForward(CD value, Class<T> type) {
		return type.cast(Introspector.of(dateType).newInstance(Long.valueOf(value.getMilliseconds())));
	}

	@Override
	public <T extends CD> T coerceReverse(D value, Class<T> type) {
		CalendarDateTime cdt = TimeUtils.from(value);
		if (type.isAssignableFrom(CalendarDate.class)){
			return type.cast(cdt.toDate());
		}
		return type.cast(cdt);
	}

		
}
