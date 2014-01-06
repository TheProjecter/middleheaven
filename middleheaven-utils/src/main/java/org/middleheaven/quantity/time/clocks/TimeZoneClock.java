package org.middleheaven.quantity.time.clocks;


import org.middleheaven.quantity.time.EpocTimePoint;
import org.middleheaven.quantity.time.TimePoint;
import org.middleheaven.quantity.time.TimeUtils;
import org.middleheaven.quantity.time.TimeZone;


public class TimeZoneClock extends WrappedClock {

	TimeZone timeZone;
	
	public TimeZoneClock(TimeZone timeZone, Clock referenceClock){
		super(referenceClock);
		this.timeZone = timeZone;
	}

	@Override
	public TimeZone getTimeZone() {
		return timeZone;
	}

	@Override
	public TimePoint getTime() {
		return calculateTimeFromReference(getReferenceClock().getTime());
	}

	@Override
	protected TimePoint calculateTimeFromReference(TimePoint referenceTime) {
		return new EpocTimePoint(TimeUtils.convertLocalTime(
				referenceTime.getMilliseconds(),
				getReferenceClock().getTimeZone(),
				this.timeZone)
		);
	}
}

