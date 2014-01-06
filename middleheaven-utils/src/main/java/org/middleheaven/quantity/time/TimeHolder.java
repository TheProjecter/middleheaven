package org.middleheaven.quantity.time;

/**
 * Holds information on time (hour, minute, second. milisecond)
 *
 */
public interface TimeHolder {

	
	public int hour();
	public int minute();
	public int second();
	public long getMilliseconds();
}
