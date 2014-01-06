package org.middleheaven.quantity.time.ephemeris;

import org.middleheaven.quantity.time.DateHolder;


/**
 * Information for the event that occurs in a specific day
 * 
 */
public interface Ephemeris {

	/**
	 * 
	 * @return date when the event occurred 
	 */
	public DateHolder getDate();
	
	/**
	 * Descriptive name for the event. The name is returned in
	 * the language defined the <code>EphemerisService</code> that provided this  
	 * @return descriptive name for the event
	 */
	public String getName();
	
	/**
	 * 
	 * @return <code>true</code> if this day is a day on which work is suspended by law or custom.<code>false</code> otherwise.
	 */
	public boolean isHoliday();
}
