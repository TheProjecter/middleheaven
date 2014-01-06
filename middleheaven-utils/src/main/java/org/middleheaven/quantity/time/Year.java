/*
 * Created on 10/Mar/2007
 */
package org.middleheaven.quantity.time;

import java.io.Serializable;
import java.util.Iterator;

/**
 * @author Sergio M.M. Taborda
 */
public class Year implements Serializable{

	private static final long serialVersionUID = -7801128905367924609L;

	private int ordinal;
   
    private int monthInYear;
    Chronology chronology;
    
    
    protected Year (Chronology chronology , int ordinal,int monthInYear){
        this.ordinal = ordinal;
        this.monthInYear =  monthInYear;
        this.chronology = chronology;
    }
    

    public Chronology getChronology(){
    	return chronology;
    }
    
    /**
     * Convert this year to the equivalent year in the other Chronology
     * @param chronology for each to obtain the equivalent year. 
     * @return equivalent year in the other Chronology
     */
    public Year toChronology(Chronology chronology){
    	CalendarDate date = CalendarDate.date(ordinal, 1, 1);
    	
    	return this.chronology.convertTo(date, chronology).year();
    }
    
    public int ordinal(){
        return this.ordinal;
    }
    
    public int monthCount(){
    	return monthInYear;
    }
    
    public Iterator<Month> monthIterator(){
        return new MonthIterator(); 
    }
    
    
	public DateHolder start() {
		return startMonth().start();
	}
	
	public DateHolder end(){
		return endMonth().end();
	}
	
    public Month endMonth(){
    	return Month.ofYear(ordinal, monthInYear);
    }
    
    public Month startMonth(){
    	return Month.ofYear(ordinal, 1);
    }
    
	public TimeInterval asInterval(){
		return new TimeInterval (
				startMonth().asInterval().start(),
				endMonth().asInterval().end()
		);
	}
	
    
    public Month month(int month){
    	
    	return chronology.monthOf(ordinal, month);
    }
    
    private  class MonthIterator implements Iterator<Month>{

    	private int index = -1;
		@Override
		public boolean hasNext() {
			return index < monthInYear-1;
		}

		@Override
		public Month next() {
			return month(index);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
    	
    }
}
