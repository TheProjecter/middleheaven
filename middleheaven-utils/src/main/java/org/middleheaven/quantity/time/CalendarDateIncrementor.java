package org.middleheaven.quantity.time;

import org.middleheaven.util.Incrementor;

/**
 * 
 */
public class CalendarDateIncrementor implements Incrementor<CalendarDate, ElapsedTime> {

	private ElapsedTime elapsedTime;
    private boolean reversed;
    
    /**
     * 
     * Constructor.
     * @param elapsedTime the step for the increment
     */
	public CalendarDateIncrementor(ElapsedTime elapsedTime){
    	 this(elapsedTime, false);
	}
	
	 CalendarDateIncrementor(ElapsedTime elapsedTime, boolean reversed){
	   	 this.elapsedTime = elapsedTime;
	   	 this.reversed = reversed;
	}

	@Override
	public CalendarDate increment(CalendarDate date) {
		return date.plus(elapsedTime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Incrementor<CalendarDate, ElapsedTime> reverse() {
		return new CalendarDateIncrementor(this.elapsedTime.negate(), !this.reversed);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Incrementor<CalendarDate, ElapsedTime> withStep(ElapsedTime step) {
		CalendarDateIncrementor inc = new CalendarDateIncrementor(step);
		if (this.reversed){
			return inc.reverse();
		}
		return inc;
	}
	
	 

}
