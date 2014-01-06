package org.middleheaven.quantity.time;


import org.middleheaven.quantity.Quantity;
import org.middleheaven.quantity.measurables.Time;

/**
 * Represents an amount of time elapsed from an epoch
 * The epoch used is January 1 , 1970 at 00:00:00 hours
 *
 */
public abstract class ElapsedTime implements Quantity<Time>{

	
	public abstract ElapsedTime negate();
}
