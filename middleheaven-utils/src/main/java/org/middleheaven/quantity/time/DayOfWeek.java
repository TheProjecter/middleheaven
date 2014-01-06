/*
 * Created on 10/Mar/2007
 */
package org.middleheaven.quantity.time;

import java.util.Calendar;

/**
 * @author Sergio M.M. Taborda
 */
public enum DayOfWeek  {

    SUNDAY(Calendar.SUNDAY),
    MONDAY (Calendar.MONDAY),
    TUESDAY (Calendar.TUESDAY),
    WEDNESDAY (Calendar.WEDNESDAY),
    THURSDAY (Calendar.THURSDAY),
    FRIDAY (Calendar.FRIDAY),
    SATURDAY (Calendar.SATURDAY);
    
    
    private final int calendarOrdinal;
    
	public static DayOfWeek valueOf(int calendarOrdinal) {
		for (DayOfWeek day : DayOfWeek.values()){
			if (day.calendarOrdinal == calendarOrdinal){
				return day;
			}
		}
		return null;
	}
	
    /**
     * @param sunday2
     */
    private DayOfWeek(int ordinal) {
         this.calendarOrdinal = ordinal;
    }

    public boolean isMonday(){
    	return this.equals(MONDAY);
    }
    
    public boolean isTuesday(){
    	return this.equals(TUESDAY);
    }
    
    public boolean isWednesday(){
    	return this.equals(WEDNESDAY);
    }
    public boolean isThursday(){
    	return this.equals(THURSDAY);
    }
    public boolean isFriday(){
    	return this.equals(FRIDAY);
    }
    public boolean isSaturday(){
    	return this.equals(SATURDAY);
    }
    
    public boolean isSunday(){
    	return this.equals(SUNDAY);
    }
    
    public int calendarOrdinal(){
        return calendarOrdinal;
    }


    
}
