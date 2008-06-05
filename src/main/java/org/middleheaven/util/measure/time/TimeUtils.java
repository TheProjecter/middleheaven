package org.middleheaven.util.measure.time;

import java.sql.Timestamp;
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
		return new Date(date.milliseconds());
	}
	
	public static java.sql.Date toSQLDate(TimePoint date){
		return new java.sql.Date(date.milliseconds());
	}
	
	public static java.sql.Time toSQLTime(TimePoint date){
		return new java.sql.Time(date.milliseconds());
	}
	
	public static Timestamp toSQLTimestamp(TimePoint date){
		return new java.sql.Timestamp(date.milliseconds());
	}
	
	public static int compare(Date a, TimePoint b){
		return (int)(a.getTime() - b.milliseconds());
	}
}
