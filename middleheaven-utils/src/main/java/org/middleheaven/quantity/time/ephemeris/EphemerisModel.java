package org.middleheaven.quantity.time.ephemeris;

import java.io.Serializable;
import java.util.Set;

import org.middleheaven.quantity.time.Chronology;
import org.middleheaven.quantity.time.DateHolder;
import org.middleheaven.quantity.time.DayOfWeek;
import org.middleheaven.quantity.time.Month;


public abstract class EphemerisModel implements Serializable{


	private Chronology chronology;
	

	public final void setChronology(Chronology chronology){
		this.chronology = chronology;
	}
	
	protected final Chronology getChronology(){
		return this.chronology;
	}
	
	public boolean isWeekend(DateHolder dateHolder){
	    final DayOfWeek dw =  dateHolder.dayOfWeek();
	    return dw.equals(DayOfWeek.SATURDAY) || dw.equals(DayOfWeek.SUNDAY);
	}
	

	public boolean isWorkingDay(DateHolder dateHolder){
		return !isWeekend(dateHolder) && !isHoliday(dateHolder);
	}

	public boolean isHoliday(DateHolder dateHolder) {
		
		final Set<Ephemeris> ephemerides = getEphemeris(dateHolder);
		for (Ephemeris e : ephemerides){
			if (e.isHoliday()){
				return true;
			}
		}
		return false;
		
	}
	
	public boolean hasEphemeris(DateHolder date){
		return !getEphemeris(date).isEmpty();
	}

	public abstract Set<Ephemeris> getEphemeris(DateHolder date);

	/**
	 * Calculates the n-th working day of the month (the ordinal) 
	 * @param month
	 * @param ordinal the ordinal of the day. 1 for the first day, 2 for the second , etc...
	 * @return DateHolder corresponding to the n-th working day of the month
	 * @throws IllegalArgumentException if ordinal is less than 1
	 */
	public final DateHolder getOrdinalWorkingDayOfMonth(Month month , int ordinal){
		if (ordinal<=0){
			throw new IllegalArgumentException("Ordinal must be greater than zero");
		}
		DateHolder current = month.start();
		int remaining = ordinal-1; // convert ordinal to numeral
		
		while (remaining>0){
			if (this.isWorkingDay(current)){
				remaining--;
			}
			current = current.nextDate();
		}

		return current;
	}
	
	/**
	 * Calculates the number of word days between two dates.
	 * @param start
	 * @param end
	 * @return
	 */
	public final int workingDaysBetween (DateHolder start,DateHolder end){
		
		DateHolder current = start;
		int count=0;
		while (current.isBefore(end)){
			if (this.isWorkingDay(current)){
				count++;
			}
			current = current.nextDate();
		}
		
		return count;
	}
	
	public final DateHolder addWorkingDays(int daysCount, DateHolder start){
		int remaining = daysCount;
		DateHolder current = start;
		while (remaining>0){
			current = current.nextDate();
			if (this.isWorkingDay(current)){
				remaining--;
			}
		}
		return current;
	}
	
	public final DateHolder subtractWorkingDays(int daysCount,DateHolder start){
		int remaining = daysCount;
		DateHolder current = start;
		while (remaining>0){
			current = current.previousDate();
			if (this.isWorkingDay(current)){
				remaining--;
			}
		}
		return current;
	}
	
}
