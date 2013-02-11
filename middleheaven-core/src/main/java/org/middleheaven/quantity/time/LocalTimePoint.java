package org.middleheaven.quantity.time;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.middleheaven.util.Hash;


public class LocalTimePoint implements TimePoint , Comparable<TimePoint>  {


	private long milliseconds;

	private TimeZone timeZone;

	public static LocalTimePoint fromUniversalTime(long universalTime , TimeZone timeZone){
		return new LocalTimePoint(universalTime,timeZone);
	}
	
	public static LocalTimePoint fromLocalTime(long local , TimeZone timeZone){
		return new LocalTimePoint(TimeUtils.localToUniversalTime(local, timeZone),timeZone);
	}
	
	protected LocalTimePoint(long milliseconds,TimeZone timeZone){
		this.milliseconds = milliseconds;
		this.timeZone = timeZone;
	}
	
	@Override
	public long getMilliseconds() {
		return TimeUtils.universalToLocalTime(this.milliseconds, timeZone);
	}

	@Override
	public TimeInterval until(TimePoint other) {
		return new TimeInterval (this,other);
	}

	@Override
	public int compareTo(TimePoint other) {
		return (int)(this.milliseconds - other.getMilliseconds());
	}

	public boolean equals(Object other){
		return other instanceof LocalTimePoint && equalsOther((LocalTimePoint)other);
	}
	
	/**
	 * @param other
	 * @return
	 */
	private boolean equalsOther(LocalTimePoint other) {
		return this.milliseconds == other.milliseconds && this.timeZone.equals(other.timeZone);
	}

	public int hashCode (){
		return Hash.hash(this.milliseconds).hash(this.timeZone).hashCode();
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public String toString(){
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(this.milliseconds);
		calendar.setTimeZone(this.timeZone.toTimeZone());
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ").format(calendar.getTime());
	}
	
	
}
