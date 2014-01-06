package org.middleheaven.quantity.time.clocks;

import org.middleheaven.quantity.time.TimePoint;
import org.middleheaven.quantity.time.TimeZone;


/**
 * A clock. A centralized point to obtain elapsed time information.
 */
public abstract class Clock {

	/**
	 * @return Number of seconds elapsed in the clock for each universal second  
	 * Normal clock have cadence equal to 1s/s (second per second)
	 */
	public abstract double getCadence ();
	
	/**
	 * 
	 * @return the current time as dictated by this clock
	 */
	public abstract TimePoint getTime();

	/**
	 * 
	 * @return the time zone this clock is in
	 */
	public abstract TimeZone getTimeZone();
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public String toString(){
		return getTime().toString();
	}
	
	protected abstract ClockTicked schedule( Schedule schedule );
	
   
}
