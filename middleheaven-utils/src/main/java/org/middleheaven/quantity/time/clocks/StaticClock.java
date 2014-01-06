package org.middleheaven.quantity.time.clocks;


import java.util.HashMap;
import java.util.Map;

import org.middleheaven.quantity.time.EpocTimePoint;
import org.middleheaven.quantity.time.TimePoint;
import org.middleheaven.quantity.time.TimeZone;

/**
 * A clock with fixed, immutable, time.
 *
 */
public class StaticClock extends Clock {


	TimeZone timeZone;
	TimePoint point;
	
	public static StaticClock forTime(TimePoint point){
		return new StaticClock(point);
	}
	
	public static StaticClock forTime(long miliseconds){
		return new StaticClock(new EpocTimePoint(miliseconds));
	}
	
	private StaticClock(TimePoint point) {
		this(point,TimeZone.getDefault());
	}
	
	public StaticClock(TimePoint point,TimeZone timeZone) {
		super();
		this.timeZone = timeZone;
		this.point = point;
	}
	
	@Override
	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
	
	@Override
	public TimePoint getTime() {
		return point;
	}

	public void setTime(TimePoint point){
		this.point = point;
		tick();
	}


	public void setLocalTime(long miliseconds){
		this.point = new EpocTimePoint(miliseconds);
		tick();
	}
	
	private void tick(){
		for (Schedule c : timers.keySet()){
			if (c.contains(this.point)){
				ClockTicked ticked = timers.get(c);
				if (ticked!=null){
					ticked.tick(this.point);
				}
			}
		}
	}
	/**
	 * @return 0 (zero) as this clock never changes its current time
	 */
	@Override
	public double getCadence() {
		return 0;
	}
	Map<Schedule, ClockTicked> timers = new  HashMap<Schedule, ClockTicked>();
	
	@Override
	protected ClockTicked schedule(Schedule chronogram) {
		ClockTicked ticked = timers.get(chronogram);
		if (ticked==null){
			ticked =new ClockTicked();
			timers.put(chronogram, ticked);
		}
		return ticked;
	}
	

}
