package org.middleheaven.quantity.time.clocks.alarm;

import org.middleheaven.quantity.time.clocks.Clock;
import org.middleheaven.quantity.time.clocks.ClockTickListener;
import org.middleheaven.quantity.time.clocks.Schedule;
import org.middleheaven.quantity.time.clocks.WrappedClock;

/**
 * Clock that warns when a determinated timepoint has arrived. 
 * The time elapsed is determinated by another clock, the reference clock
 * <code>AlarmClock</code> can be used to implements scheduling 
 * 
 */
public class AlarmClock extends WrappedClock {

	
	public AlarmClock(Clock referenceClock) {
		super(referenceClock);
	}

	public void addClockTickListener (ClockTickListener listener, Schedule schedule){
		schedule(schedule).addClockTickListener(listener);
	}
	
	public void removeClockTickListener (ClockTickListener listener,Schedule schedule){
		schedule(schedule).removeClockTickListener(listener);
	}


	
}
