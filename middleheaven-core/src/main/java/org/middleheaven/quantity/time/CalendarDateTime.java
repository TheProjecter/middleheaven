package org.middleheaven.quantity.time;

import java.util.Date;



public class CalendarDateTime extends AbstractTimePoint implements  DateHolder , TimeHolder  {

	public static CalendarDateTime now(){
		return new CalendarDateTime(TimeContext.getTimeContext(), TimeContext.getTimeContext().now().getMilliseconds());
	}
	
	
	static CalendarDateTime origin(){
		return new CalendarDateTime(TimeContext.getTimeContext(), 0);
	}
	

	public static CalendarDateTime at(int year, int month, int day, int hour, int minute, int second) {
		return new CalendarDate(TimeContext.getTimeContext(),TimeContext.getTimeContext().getChronology().milisecondsFor(false,year, month, day, hour, minute, second));
	}
	
	public static CalendarDateTime midnight(int year, int month, int day) {
		return at(year, month, day, 0, 0, 0);
	}
	
	public static CalendarDateTime midday(int year, int month, int day) {
		return at(year, month, day, 12, 0, 0);
	}
	
	public static CalendarDateTime valueOf(Date date){
		return new CalendarDateTime(TimeContext.getTimeContext(), date.getTime());
	}
	
	public static CalendarDateTime valueOf(CalendarDate date){
		return new CalendarDateTime(TimeContext.getTimeContext(), date.milliseconds);
	}
	
	public static CalendarDateTime valueOf(CalendarDate date , CalendarTime time){
		return new CalendarDateTime(TimeContext.getTimeContext(), date.milliseconds + time.milliseconds);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8177265376553572402L;

	protected CalendarDateTime (TimeContext context, long timeFromEpoc){
		super(context,timeFromEpoc);
	}
	

    public DayOfMonth day() {
       return context.getChronology().monthDay(this);
    }
    
    public DayOfMonth dayOfMonth() {
        return context.getChronology().monthDay(this);
     }
    
	@Override
	public int dayOfYear() {
		 return context.getChronology().dayOfYear(this);
	}
    
	
    public DayOfWeek dayOfWeek(){
        return context.getChronology().weekDay(this);
    }
    
    public Month month(){
        return context.getChronology().monthOf(this);
    }
    
    public Year year(){
        return context.getChronology().yearOf(this);
    }
    
	@Override
	public int hour() {
		return context.getChronology().timeOf(this).hour();
	}

	@Override
	public int minute() {
		return context.getChronology().timeOf(this).minute();
	}

	@Override
	public int second() {
		return context.getChronology().timeOf(this).second();
	}
    
    public boolean isWeekend(){
        return context.getReferenceWorkCalendar().isWeekend(this);
    }
    
    public boolean isHoliday(){
        return context.getReferenceWorkCalendar().isHoliday(this);
    }
    
    public boolean isWorkingDay(){
        return context.getReferenceWorkCalendar().isWorkingDay(this);
    }
    
    public CalendarDateTime nextDate(){
    	return context.getChronology().add(this, Duration.of().days(1));
    }
    
    public CalendarDateTime previousDate(){
    	return context.getChronology().add(this, Duration.of().days(-1));
    }
    

    public CalendarDateTime plus (ElapsedTime elapsed){
    	return this.context.getChronology().add(this, elapsed);
    }
    
    public CalendarDateTime minus (ElapsedTime elapsed){
    	return this.context.getChronology().add(this, (ElapsedTime)elapsed.negate());
    }

    public Duration differenceTo(TimePoint other, DurationUnit unit){
    	return this.context.getChronology().differenceBetween(this, other, unit);
    }
    
	@Override
	public boolean isBefore(DateHolder other) {
		return this.compareTo(other)<0;
	}

	@Override
	public boolean isAfter(DateHolder other) {
		return this.compareTo(other)>0;
	}

	@Override
	public DateHolder nearestBefore(DayOfWeek weekDay) {
		DateHolder day = this;
		
		while (!day.dayOfWeek().equals(weekDay)){
			day = day.previousDate();
		}
		
		return day;
	}
	
	@Override
	public DateHolder nearestAfter(DayOfWeek weekDay) {
		DateHolder day = this;
		
		while (!day.dayOfWeek().equals(weekDay)){
			day = day.nextDate();
		}
		
		return day;
	}
	
	@Override
	public DateHolder nearest(DayOfWeek weekDay) {
		
		int diff= weekDay.calendarOrdinal() -  this.dayOfWeek().calendarOrdinal();
		
		return diff==0? this : this.plus(Duration.of().days(diff));
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
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

	
	public String toString(){
		return new Date(this.milliseconds).toString();
	}

	public CalendarTime toTime() {
		return new CalendarTime(this.context, this.milliseconds);
	}

    public CalendarDate toDate(){
    	return new CalendarDate(this.context, this.milliseconds);
    }




	

}
