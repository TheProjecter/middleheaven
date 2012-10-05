/*
 * Created on 10/Mar/2007
 */
package org.middleheaven.quantity.time;

import java.util.Date;

import org.middleheaven.util.Incrementable;
import org.middleheaven.util.NaturalIncrementable;
import org.middleheaven.util.collections.ComparableComparator;
import org.middleheaven.util.collections.Interval;
import org.middleheaven.util.collections.Range;



/**
 * 
 */
public class CalendarDate extends CalendarDateTime  implements Incrementable<ElapsedTime>, NaturalIncrementable<CalendarDate>{

	private static final long serialVersionUID = -5640403398323384454L;
	
	public static CalendarDate today(){
    	return new CalendarDate(TimeContext.getTimeContext(), TimeContext.getTimeContext().now().getMilliseconds());
    }
	
	public static CalendarDate valueOf(Date date){
		return CalendarDateTime.valueOf(date).toDate();
	}
	
    public static CalendarDate date(int year , int month, int day){
    	return date(true,year,month,day);
    }
  
    public static CalendarDate date(boolean lenient , int year , int month, int day){
    	return date(TimeContext.getTimeContext(), lenient, year,month,day);
    }
    
    public static CalendarDate date( TimeContext context,boolean lenient , int year , int month, int day){
    	return new CalendarDate(context,context.getChronology().milisecondsFor(lenient,year, month, day));
    }
    
    protected CalendarDate (TimeContext context, long timeFromEpoc){
        super(context,timeFromEpoc);
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
    public CalendarDate next(){
    	return context.getChronology().add(this, Duration.of().days(1));
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override
    public CalendarDate previous(){
    	return context.getChronology().add(this, Duration.of().days(-1));
    }

    public CalendarDate plus (ElapsedTime elapsed){
    	return this.context.getChronology().add(this, elapsed);
    }
    
    public CalendarDate minus (ElapsedTime elapsed){
    	return this.context.getChronology().add(this, elapsed.negate());
    }
    
	public Interval<CalendarDate> until(CalendarDate date) {
		return Interval.between(this, date);
	}

	public Range<CalendarDate> upTo(CalendarDate date) {
		return to(date,Duration.of().days(1));
	}

	public Range<CalendarDate> downTo(CalendarDate date) {
		return to(date,Duration.of().days(-1));
	}

	public Range<CalendarDate> to(CalendarDate date, ElapsedTime elapsedTime) {
		return Range.over(this, date, new ComparableComparator<CalendarDate>(), new CalendarDateIncrementor(elapsedTime));
	}
	
	public String toString(){
		return TimeUtils.toDate(this).toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public CalendarDate incrementBy(ElapsedTime increment) {
		return this.plus(increment);
	}

	@Override
	public int compareTo(DateHolder other) {
		int comp = this.year().ordinal() - other.year().ordinal();
		if (comp ==0){
			comp = this.month().ordinal() - other.month().ordinal();
			if (comp ==0){
				comp = this.dayOfMonth().getDay() - other.dayOfMonth().getDay();
				
			}
		}
		return comp;
	}



	
}
