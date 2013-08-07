package org.middleheaven.quantity.time;

import org.middleheaven.core.exception.UnimplementedMethodException;
import org.middleheaven.quantity.time.clocks.Clock;
import org.middleheaven.quantity.time.clocks.MachineClock;


/**
 * A definition of how time is computed.
 * All int value correspond to ordinal values (i.e. 1- fisrt, 2- secound, 3-thirs , and so on ...)
 */
public abstract class Chronology {

	protected Clock referenceClock = MachineClock.getInstance();
	
	/**
	 * Change the Reference {@link Clock}. By default the {@link Chronology} uses the {@link MachineClock}.
	 * @param referenceClock
	 */
	public void setClock(Clock referenceClock){
		this.referenceClock = referenceClock;
	}
	
	/**
	 * Transforms a yera, month, day information into milisecounds from 1990, January 1st 
	 * 
	 * @param lenient <code>true</code> to use the next possible date if the values conform to a non-existing date, if <code>false</code> throw an exception if the date does not exist.
	 * @param year the year
	 * @param month the month of the year
	 * @param day the day of the month
	 * @return the correspondent milisecounds in the {@link Chronology}.
	 */
	public long milisecondsFor(boolean lenient,int year, int month, int day ){
		return milisecondsFor(lenient, year, month, day, 0, 0, 0);
	}
	
	public abstract long milisecondsFor(boolean lenient,int ... fields);
	
	/**
	 * Add an {@link ElapsedTime} to a {@link TimePoint}.
	 * @param point the reference point
	 * @param elapsed the elapsed time to add
	 * @return the resulting point.
	 */
	public <T extends TimePoint> T add(T point , ElapsedTime elapsed){
		if (elapsed instanceof Duration){
			return add(point , (Duration)elapsed);
		} else if (elapsed instanceof Period){
			return add(point , (Period)elapsed);
		} else {
			throw new IllegalArgumentException("Cannot add " + elapsed.getClass()  + " to " + point.getClass());
		}	
    }

	/**
	 * Add an {@link Duration} to a {@link TimePoint}.
	 * @param point the reference point
	 * @param elapsed the elapsed time to add
	 * @return the resulting point.
	 */
	public abstract <T extends TimePoint> T add(T point , Duration eplased);
	
	/**
	 * Add an {@link Period} to a {@link TimePoint}.
	 * @param point the reference point
	 * @param elapsed the elapsed time to add
	 * @return the resulting point.
	 */
	public abstract <T extends TimePoint> T add(T point , Period eplased);
	
	/**
	 * Obtains the {@link DayOfMonth} in this {@link Chronology} for a given {@link TimePoint}.
	 * 
	 * @param point the point from where to extract the {@link DayOfMonth}.
	 * @return the resulting {@link DayOfMonth}.
	 */
    public abstract DayOfMonth monthDay(TimePoint point);
    
	/**
	 * Obtains the {@link DayOfWeek} in this {@link Chronology} for a given {@link TimePoint}.
	 * 
	 * @param point the point from where to extract the {@link DayOfWeek}.
	 * @return the resulting {@link DayOfWeek}.
	 */
    public abstract DayOfWeek weekDay(TimePoint point);
    
	/**
	 * Obtains the {@link Month} in this {@link Chronology} for a given {@link TimePoint}.
	 * 
	 * @param point the point from where to extract the {@link Month}.
	 * @return the resulting {@link Month}.
	 */
    public abstract Month monthOf(TimePoint point);
    
	/**
	 * Obtains the {@link Year} in this {@link Chronology} for a given {@link TimePoint}.
	 * 
	 * @param point the point from where to extract the {@link Year}.
	 * @return the resulting {@link Year}.
	 */
    public abstract Year yearOf(TimePoint point);

	/**
	 * Obtains the ordinal of the day in the current year for a given {@link TimePoint}   considering this {@link Chronology} .
	 * 
	 * @param point the point from where to extract the {@link Year}.
	 * @return the resulting day of year ordinal.
	 */
    public abstract int dayOfYear(TimePoint point);
    
    /**
     * Obtains the {@link Month} in this {@link Chronology} for a given  year and month ordinal
     * 
     * @param year the year ordinal
     * @param month the month ordinal
     * @return the corresponding {@link Month} in this {@link Chronology}.
     */
	public abstract Month monthOf(int year, int month);

	/**
	 * Obtains a {@link TimeHolder} in this {@link Chronology} for a given {@link TimePoint}.
	 * 
	 * @param point the point from where to extract the {@link TimeHolder}.
	 * @return the resulting {@link TimeHolder}.
	 */
	public abstract TimeHolder timeOf(TimePoint point);

	/**
	 * Convert a {@link CalendarDate} in one given {@link Chronology} to a {@link CalendarDate} in this {@link Chronology}.
	 * @param date the date to convert
	 * @param chronology the {@link Chronology} for the date.
	 * @return the corresponding date in this {@link Chronology}.
	 */
	public CalendarDate convertTo(CalendarDate date, Chronology chronology){
		
		// TODO 
		throw new UnimplementedMethodException("Conversion between chonologies is not yet supported");
	}

	/**
	 * The duration between to {@link TimePoint}s.
	 * 
	 * @param calendarDateTime
	 * @param other
	 * @param unit 
	 * @return
	 */
	public abstract Duration differenceBetween(TimePoint calendarDateTime,TimePoint other, DurationUnit unit);

	

    
}
