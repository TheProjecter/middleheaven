package org.middleheaven.quantity.time;

import org.middleheaven.quantity.time.chono.JavaCalendarCronology;
import org.middleheaven.quantity.time.clocks.Clock;
import org.middleheaven.quantity.time.clocks.MachineClock;
import org.middleheaven.quantity.time.ephemeris.DefaultEphemerisModel;
import org.middleheaven.quantity.time.ephemeris.EphemerisModel;
import org.middleheaven.quantity.time.zones.JavaTimeZoneTable;

public final class TimeContext {

	
	private static TimeContext current = new TimeContext(
			MachineClock.getInstance(),
			new DefaultEphemerisModel(),
			new JavaCalendarCronology(),
			new JavaTimeZoneTable()
	);
	
	public static void setTimeContext(TimeContext context){
		current = context;
	}
	
	public static TimeContext getTimeContext(){
		return current;
	}
	

	private Clock referenceClock;
	private EphemerisModel referenceWorkCalendar;
	private Chronology chronology;
	private TimeZoneTable timezoneTable;
	
	public TimeContext(Clock referenceClock,
			EphemerisModel referenceWorkCalendar, 
			Chronology chronology,
			TimeZoneTable timezoneTable) {
		
		this.timezoneTable = timezoneTable;
		this.referenceClock = referenceClock;
		this.chronology = chronology;
		this.chronology.setClock(referenceClock);
		this.referenceWorkCalendar = referenceWorkCalendar;
		this.referenceWorkCalendar.setChronology(chronology);
		
	}

	public TimeZoneTable getTimeZoneTable(){
		return timezoneTable;
	}
	
	public TimeZone getDefaultTimeZone(){
		return timezoneTable.convertFromJavaTimeZone(java.util.TimeZone.getDefault());
	}
	
	public TimePoint now (){
		return new CalendarDateTime(this, referenceClock.getTime().getMilliseconds());
	}
	
	/**
	 * @return
	 * 
	 */
	public Clock getReferenceClock() {
		return referenceClock;
	}

	/**
	 * @param referenceClock
	 * 
	 */
	public void setReferenceClock(Clock referenceClock) {
		this.referenceClock = referenceClock;
	}

	/**
	 * @return
	 * 
	 */
	public EphemerisModel getReferenceWorkCalendar() {
		return referenceWorkCalendar;
	}

	/**
	 * @param referenceWorkCalendar
	 * 
	 */
	public void setReferenceWorkCalendar(EphemerisModel referenceWorkCalendar) {
		this.referenceWorkCalendar = referenceWorkCalendar;
	}

	/**
	 * @return
	 * 
	 */
	public Chronology getChronology() {
		return chronology;
	}

	/**
	 * @param chronology
	 * 
	 */
	public void setChronology(Chronology chronology) {
		this.chronology = chronology;
	}
	
	
}
