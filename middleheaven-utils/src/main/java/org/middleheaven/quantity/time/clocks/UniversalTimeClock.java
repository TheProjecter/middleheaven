package org.middleheaven.quantity.time.clocks;

import org.middleheaven.quantity.time.TimeZone;



public final class UniversalTimeClock extends TimeZoneClock{

	public UniversalTimeClock(Clock referenceClock) {
		super(TimeZone.getTimeZone("GMT+0"),referenceClock);
	}


	

}
