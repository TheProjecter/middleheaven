package org.middleheaven.quantity.time;

/**
 * Represents a unanchored time.
 */
public class CalendarTime extends AbstractTimePoint implements  TimeHolder {


	private static final long serialVersionUID = 6912748590262711429L;

	protected CalendarTime(TimeContext context, long timeFromEpoc) {
		super(context, timeFromEpoc);
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
	
	@Override
	public long getMilliseconds() {
		return this.milliseconds;
	}


}
