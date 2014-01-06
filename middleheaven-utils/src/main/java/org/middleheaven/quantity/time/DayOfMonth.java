/*
 * Created on 10/Mar/2007
 */
package org.middleheaven.quantity.time;

/**
 * @author Sergio M.M. Taborda
 */
public class DayOfMonth {


    private int day;

    private int month;
    
   
    
    /**
     * @param day
     * @param month
     */
    public DayOfMonth(int day, int month) {
        this.day = day;
        this.month = month;
    }
    
    

    /**
	 * @return  Returns the day.
	 *
	 */
    public int getDay() {
        return day;
    }
    /**
	 * @param day  The day to set.
	 */
    public void setDay(int day) {
        this.day = day;
    }
    /**
	 * @return  Returns the month. 
	 */
    public int getMonth() {
        return month;
    }
    /**
	 * @param month  The month to set.
	 * 
	 */
    public void setMonth(int month) {
        this.month = month;
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other){
        return other instanceof DayOfMonth && equalsOther((DayOfMonth)other);
    }
    
    private boolean equalsOther(DayOfMonth other){
        return this.month == other.month && this.day == other.day;
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public int hashCode(){
        // concatenates the month and day into a single number so hash is unic
        // for each pair. this intended to boots performance of Set and Map 
        return day | (month << 8 );
    }
}
