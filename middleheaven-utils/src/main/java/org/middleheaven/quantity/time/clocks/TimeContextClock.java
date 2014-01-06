package org.middleheaven.quantity.time.clocks;

import org.middleheaven.quantity.time.TimeContext;

public final class TimeContextClock extends WrappedClock{

	public TimeContextClock() {
		super(null);
	}
	
	protected Clock getReferenceClock(){
		return TimeContext.getTimeContext().getReferenceClock();
	}
	

}
