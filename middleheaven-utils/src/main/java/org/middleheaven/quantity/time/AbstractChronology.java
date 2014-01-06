package org.middleheaven.quantity.time;

import org.middleheaven.reflection.inspection.Introspector;




public abstract class AbstractChronology extends Chronology {


	@Override
	public <T extends TimePoint> T add(T point, Period period) {
		return reduce(
				timePointFor(point.getMilliseconds() + period.milliseconds()),
				Introspector.of(point).getType()
		);
	}

	@SuppressWarnings("unchecked")
	protected <T extends TimePoint> T reduce(CalendarDateTime d , Class<T> resultClass){
		if (resultClass.equals(CalendarDate.class)){
			return (T)d.toDate();
		} 
		return (T)d;

	}

	protected TimePoint now(){
		return this.referenceClock.getTime();
	}

	protected CalendarDateTime timePointFor(long milliseconds){
		return new CalendarDateTime(TimeContext.getTimeContext(), milliseconds);
	}

	protected Month month(int year, int month , int maxDaysInMonth){
		return new Month(year,month,maxDaysInMonth);
	}

	protected Year year(int year,  int maxMonths){
		return new Year(this,year,maxMonths);
	}

	protected DayOfWeek weekDay(int ordinal){
		return DayOfWeek.valueOf(ordinal);
	}


}
