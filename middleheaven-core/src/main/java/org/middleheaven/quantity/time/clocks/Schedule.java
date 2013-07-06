package org.middleheaven.quantity.time.clocks;

import org.middleheaven.quantity.time.Period;
import org.middleheaven.quantity.time.TimePoint;

public interface Schedule {

	/**
	 * 
	 * @return the first execution time
	 */
	public TimePoint getStartTime();
	
	/**
	 * 
	 * @return the last execution time, or <code>null</code> if there is no limit time
	 */
	public TimePoint getEndTime();

	public boolean include(TimePoint point);

	/**
	 * 
	 * @return <code>true</code> if this {@link Schedule} has a stop time.<code>false</code> otherwise.
	 */
	public boolean isLimited();
	
	public Period repetitionPeriod();
}
