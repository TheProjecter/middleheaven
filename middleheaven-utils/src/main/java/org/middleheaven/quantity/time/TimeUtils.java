package org.middleheaven.quantity.time;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;


public final class TimeUtils {

	private TimeUtils(){};
	
	public static long universalToLocalTime (long universalTime , TimeZone a){
		return universalTime + a.getRawOffset() + a.getOffset(universalTime);
	}
	
	public static long localToUniversalTime (long localtime , TimeZone a){
		return localtime - a.getRawOffset() - a.getOffset(System.currentTimeMillis());
	}
	
	/**
	 * Convert the time as displayed in a local time zone to the time it should be displayed at another time zone
	 * @param localTime
	 * @param localZone
	 * @param remoteZone
	 * @return
	 */
	public static long convertLocalTime (long localTime , TimeZone localZone , TimeZone remoteZone){
		return localTime + remoteZone.getRawOffset() - localZone.getRawOffset() ;
	}
	
	public static Date toDate(TimePoint date){
		return date == null ? null:  new Date(date.getMilliseconds());
	}
	
	public static CalendarDateTime from(Object value) {
		final long time;
		
		if(value == null){
			return null;
		} else if (value instanceof Date) {
			time = ((Date)value).getTime();				
		} else if (value instanceof Calendar) {
			time = ((Calendar)value).getTime().getTime();
		} else if (value instanceof CalendarDate) {
			time = ((CalendarDate)value).getMilliseconds();
		} else if (value instanceof CalendarDateTime) {
			time = ((CalendarDateTime)value).getMilliseconds();
		} else if (value instanceof Long) {
			time = ((Long)value).longValue();
		} else {
			throw new IllegalArgumentException(value.getClass()+ " does not represent a point in time");
		}
		
		return new CalendarDateTime(TimeContext.getTimeContext(),time);
	}
	
	public static java.sql.Date toSQLDate(TimePoint date){
		return new java.sql.Date(date.getMilliseconds());
	}
	
	public static java.sql.Time toSQLTime(TimePoint date){
		return new java.sql.Time(date.getMilliseconds());
	}
	
	public static Timestamp toSQLTimestamp(TimePoint date){
		return new java.sql.Timestamp(date.getMilliseconds());
	}
	
	public static int compare(Date a, TimePoint b){
		return (int)(a.getTime() - b.getMilliseconds());
	}


}
