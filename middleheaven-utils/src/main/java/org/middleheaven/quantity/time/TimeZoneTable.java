package org.middleheaven.quantity.time;

public abstract class TimeZoneTable {

	
	public abstract TimeZone getTimeZone(String reference);
	
	public abstract TimeZone convertFromJavaTimeZone(java.util.TimeZone zone);
}
