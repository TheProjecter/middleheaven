package org.middleheaven.quantity.time.chono;

import java.util.Calendar;
import java.util.Locale;

import org.middleheaven.quantity.time.AbstractChronology;
import org.middleheaven.quantity.time.DayOfMonth;
import org.middleheaven.quantity.time.DayOfWeek;
import org.middleheaven.quantity.time.Duration;
import org.middleheaven.quantity.time.DurationUnit;
import org.middleheaven.quantity.time.Month;
import org.middleheaven.quantity.time.TimeHolder;
import org.middleheaven.quantity.time.TimePoint;
import org.middleheaven.quantity.time.Year;
import org.middleheaven.reflection.inspection.Introspector;


public class JavaCalendarCronology extends AbstractChronology{


	private Calendar prototype;

	public JavaCalendarCronology(){
		this(Calendar.getInstance(Locale.getDefault()));
	}

	public JavaCalendarCronology(Calendar prototype){
		this.prototype = prototype;
	}

	public JavaCalendarCronology(Locale locale){
		this(Calendar.getInstance(locale));
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public <T extends TimePoint> T add(T point, Duration duration) {
		Calendar calendar = (Calendar)prototype.clone();
		calendar.setLenient(true);
		calendar.setTimeInMillis(point.getMilliseconds());
		calendar.add(Calendar.YEAR, duration.years());
		calendar.add(Calendar.MONTH, duration.months());
		calendar.add(Calendar.DATE, duration.days());
		calendar.add(Calendar.HOUR, duration.hours());
		calendar.add(Calendar.MINUTE, duration.minutes());
		calendar.add(Calendar.MILLISECOND, (int)(duration.secounds()*1000));
	
		return reduce(
				timePointFor(calendar.getTimeInMillis()), 
				Introspector.of(point).getType() 
		);
	}


	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public long milisecondsFor(boolean lenient, int ... fields) {
		
		if (fields.length > 0 && fields[0] <=0){
			throw new IllegalArgumentException("Year must be a positive integer");
		}
		if (fields.length > 1 && fields[1] <=0){
			throw new IllegalArgumentException("Month must be a positive integer");
		}
		if (fields.length > 2 && fields[2] <=0){
			throw new IllegalArgumentException("Day must be a positive integer");
		}
		if (fields.length > 3 && fields[3]  <0){
			throw new IllegalArgumentException("Hour must be a positive integer");
		}
		if (fields.length > 4 && fields[4]  <0){
			throw new IllegalArgumentException("Minute must be a positive integer");
		}
		if (fields.length > 5 && fields[5]  <0){
			throw new IllegalArgumentException("Second must be a positive integer");
		}

		Calendar calendar = (Calendar)prototype.clone();
		clearCalendar(calendar);
		calendar.setLenient(lenient);
		if (fields.length > 0){
			calendar.set(Calendar.YEAR, fields[0]);
		}
		if (fields.length > 1){
			calendar.set(Calendar.MONTH, fields[1] - 1);
		}
		if (fields.length > 2){
			calendar.set(Calendar.DATE, fields[2]);
		}
		if (fields.length > 3){
			calendar.set(Calendar.HOUR_OF_DAY, fields[3]);
		}
		if (fields.length > 4){
			calendar.set(Calendar.MINUTE, fields[4]);
		}
		if (fields.length > 5){
			calendar.set(Calendar.SECOND, fields[5]);
		}

		calendar.clear(Calendar.MILLISECOND);
		
		return calendar.getTimeInMillis();
	}

	protected void clearCalendar(Calendar calendar){
		for (int i=0;i<Calendar.FIELD_COUNT;i++){
			calendar.clear(i);
		}
	}
	
	@Override
	public DayOfMonth monthDay(TimePoint point) {
		Calendar c = (Calendar)prototype.clone();
		c.setTimeInMillis(point.getMilliseconds());
		return new DayOfMonth(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1);
	}

	@Override
	public Month monthOf(TimePoint point) {
		Calendar c = (Calendar)prototype.clone();
		c.setTimeInMillis(point.getMilliseconds());
		return month( c.get(Calendar.YEAR),
				c.get(Calendar.MONTH)+1,
				c.getActualMaximum(Calendar.DAY_OF_MONTH));
	}

	@Override
	public DayOfWeek weekDay(TimePoint point) {
		Calendar c = (Calendar)prototype.clone();
		c.setTimeInMillis(point.getMilliseconds());
		return weekDay(c.get(Calendar.DAY_OF_WEEK));
	}

	@Override
	public Year yearOf(TimePoint point) {
		Calendar c =  (Calendar)prototype.clone();
		c.setTimeInMillis(point.getMilliseconds());
		return year(
				c.get(Calendar.YEAR),
				c.getActualMaximum(Calendar.MONTH)
		);
	}

	@Override
	public int dayOfYear(TimePoint point) {
		Calendar c =  (Calendar)prototype.clone();
		c.setTimeInMillis(point.getMilliseconds());
		return c.get(Calendar.DAY_OF_YEAR);
	}
	
	@Override
	public Month monthOf(int year, int month) {
		Calendar calendar = (Calendar)prototype.clone();
		clearCalendar(calendar);
		calendar.setLenient(false);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DATE, 1);
		calendar.clear(Calendar.HOUR_OF_DAY);
		calendar.clear(Calendar.MINUTE);
		calendar.clear(Calendar.SECOND);
		calendar.clear(Calendar.MILLISECOND);
		
		return new Month(year, month, calendar.getGreatestMinimum(Calendar.DAY_OF_MONTH));
	}

	@Override
	public TimeHolder timeOf(TimePoint point) {
		Calendar c =  (Calendar)prototype.clone();
		c.setTimeInMillis(point.getMilliseconds());
		return new CalendarTimeHolder(c);
	}

	
	private static final class CalendarTimeHolder implements TimeHolder{
		Calendar calendar;
		public CalendarTimeHolder(Calendar calendar) {
			this.calendar = calendar;
		}

		@Override
		public int hour() {
			return calendar.get(Calendar.HOUR_OF_DAY);
		}

		@Override
		public long getMilliseconds() {
			return calendar.get(Calendar.MILLISECOND);
		}

		@Override
		public int minute() {
			return calendar.get(Calendar.MINUTE);
		}

		@Override
		public int second() {
			return calendar.get(Calendar.SECOND);
		}
		
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Duration differenceBetween(TimePoint a, TimePoint b , DurationUnit unit) {

		long max;
		long min;
		
		if (a.getMilliseconds() > b.getMilliseconds()){
			max = a.getMilliseconds();
			min = b.getMilliseconds();
		} else {
			max = b.getMilliseconds();
			min = a.getMilliseconds();
		}
		
		Calendar ca = (Calendar)prototype.clone();
		ca.setTimeInMillis(max);
		
		Calendar cb = (Calendar)prototype.clone();
		cb.setTimeInMillis(min);
		
		if (ca.get(Calendar.YEAR) == cb.get(Calendar.YEAR) && ca.get(Calendar.MONDAY) == cb.get(Calendar.MONDAY) && ca.get(Calendar.DATE) == cb.get(Calendar.DATE)){
			switch (unit){
			case DAYS:
			case MONTHS:
			case YEARS:
				return Duration.of().add(unit, 0);
			default:
				// default algorithm
			}
		}
		
		long count = 0;
		int calendarUnit = calendarUnitFromDurationUnit(unit);
		while (cb.compareTo(ca) < 0){
			cb.add(calendarUnit, 1);
			count++;
		}

		return Duration.of().add(unit, count);
		
	}

	private int calendarUnitFromDurationUnit(DurationUnit unit){
		switch (unit){
		case MILISECONDS:
			return Calendar.MILLISECOND;
		case SECONDS:
			return Calendar.SECOND;
		case MINUTES:
			return Calendar.MINUTE;
		case HOURS:
			return Calendar.HOUR_OF_DAY;
		case MONTHS:
			return Calendar.MONTH;
		case YEARS:
			return Calendar.YEAR;
		case DAYS:
		default:
			return Calendar.DATE;
		}
	}

}
