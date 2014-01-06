package org.middleheaven.quantity.time.clocks;


import org.middleheaven.quantity.time.EpocTimePoint;
import org.middleheaven.quantity.time.TimePoint;

/**
 * A clock with variable cadence. <code>SpeedyClock</code> uses another clock
 * as reference for the real time.
 * This clock can be used to simulate faster or slower elapse of time
 * 
 */
public class SpeedyClock extends WrappedClock {

	private double cadence;
	private TimePoint mark;
	
	public static SpeedyClock aSecondIs(double cadence,Clock reference){
		return new SpeedyClock(cadence,reference);
	}
	
	public static SpeedyClock aSecondIsAnHour(Clock reference){
		return new SpeedyClock(60*60,reference); // 60s * 60 min
	}
	
	public static SpeedyClock aSecondIsADay(Clock reference){
		return new SpeedyClock(24*60*60,reference); // 60s * 60 min * 24h
	}
	
	private SpeedyClock(double cadence, Clock reference){
		super(reference);
		this.cadence = cadence;
		this.mark = reference.getTime();
	}
	
	@Override
	public double getCadence() {
		return cadence;
	}

	/**
	 * Resets the difference between this clock and the reference clock
	 */
	public synchronized  void mark(){
		this.mark = this.getReferenceClock().getTime();
	}
	
	@Override
	public synchronized TimePoint getTime() {
		
		return calculateTimeFromReference(this.getReferenceClock().getTime());
		
	}

	protected TimePoint calculateTimeFromReference(TimePoint tick){
		return new EpocTimePoint(mark.getMilliseconds() + (long)((tick.getMilliseconds() - mark.getMilliseconds()) * cadence));
	}

	

}
