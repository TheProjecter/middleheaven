package org.middleheaven.quantity.time.clocks;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.net.UnknownHostException;

import org.junit.Test;
import org.middleheaven.io.RemoteComunicationTimeoutException;
import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.quantity.time.Clock;
import org.middleheaven.quantity.time.Duration;
import org.middleheaven.quantity.time.Period;
import org.middleheaven.quantity.time.TimePoint;
import org.middleheaven.quantity.time.TimeZone;
import org.middleheaven.quantity.time.clocks.ClockTickListener;
import org.middleheaven.quantity.time.clocks.MachineClock;
import org.middleheaven.quantity.time.clocks.SNTPUniversalTimeClock;
import org.middleheaven.quantity.time.clocks.SpeedyClock;
import org.middleheaven.quantity.time.clocks.StaticClock;
import org.middleheaven.quantity.time.clocks.TimeZoneClock;
import org.middleheaven.work.scheduled.AlarmClock;
import org.middleheaven.work.scheduled.IntervalSchedule;


public class ClockTest {

	@Test
	public void testPeriod() {
		
		BigDecimal b = new BigDecimal("1E-9");
		long g = b.longValue();
		
		Period p = Period.miliseconds(1000);
		
		assertEquals(1L,p.seconds());
		assertEquals(1000L,p.milliseconds());
		assertEquals(1000000000L,p.nanoseconds());
	}
	
	@Test
	public void testStaticClock() {
		long now = System.currentTimeMillis();
		Clock clock = StaticClock.forTime(now);

		assertEquals(now, clock.getTime().milliseconds()); 
	}

	@Test
	public void testRacerClock() {

		StaticClock sclock = StaticClock.forTime(1000);
		Clock clock = SpeedyClock.aSecondIs(2.0,sclock);

		// raceclock = sclock * cadence + 1000

		sclock.setLocalTime(2000);
		assertEquals(3000L, clock.getTime().milliseconds()); 

		sclock.setLocalTime(3000);
		assertEquals(5000L, clock.getTime().milliseconds()); 


		sclock.setLocalTime(0);
		clock = SpeedyClock.aSecondIsAnHour(sclock);

		sclock.setLocalTime(1000);
		assertEquals(60*60*1000L, clock.getTime().milliseconds()); 

		sclock.setLocalTime(0);
		clock = SpeedyClock.aSecondIsADay(sclock);

		sclock.setLocalTime(1000);
		assertEquals(1000*60*60*24L, clock.getTime().milliseconds()); 


	}

	@Test
	public void testTimeZoneClock() {

		CalendarDateTime now = CalendarDateTime.now();

		StaticClock sclock = StaticClock.forTime(now);

		TimeZoneClock local = new TimeZoneClock(sclock.getTimeZone(), sclock);

		assertEquals(now.milliseconds(), local.getTime().milliseconds());

		TimeZoneClock GMT = new TimeZoneClock( TimeZone.getTimeZone("GMT+00:00"), sclock);

		// calculate time at GMT
		TimePoint reduced = now.minus(sclock.getTimeZone().getRawOffsetPeriod());

		assertEquals(0L, GMT.getTime().milliseconds() - reduced.milliseconds() );

		TimeZoneClock newYork = new TimeZoneClock(TimeZone.getTimeZone("GMT-04:00"), sclock);

		// calculate time at New York
		assertEquals(0L, now.minus(sclock.getTimeZone().getRawOffsetPeriod()).minus(Duration.of().hours(4)).milliseconds() - newYork.getTime().milliseconds() );

	}
	
	@Test(expected=RemoteComunicationTimeoutException.class)
	public void testSNPTClock() throws UnknownHostException{

		// server does not exist
		Clock clock = SNTPUniversalTimeClock.forServer("0.0.0.1");
		clock.getTime();

	}
	
	//@Test
	public void testTickListenrs() {
		AlarmClock clock = new AlarmClock(new MachineClock());
		CalendarDateTime time = CalendarDateTime.now();
		
		TestClockTickListener listener = new TestClockTickListener();
		
		clock.addClockTickListener(listener, IntervalSchedule.schedule(time, time.plus(Duration.of().seconds(5)) , Period.seconds(1)));
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// no-op
		}
		
		assertEquals(4, listener.count());
		
		
	    clock = new AlarmClock(SpeedyClock.aSecondIsAnHour(new MachineClock()));
	    time = CalendarDateTime.now();
		
	    listener = new TestClockTickListener();
		
		clock.addClockTickListener(listener, IntervalSchedule.schedule(time, time.plus(Duration.of().hours(5)) , Period.seconds(1)));
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// no-op
		}
		
		assertEquals(5, listener.count());
	}

	
	private static class TestClockTickListener implements ClockTickListener{

		private int count=0;
		@Override
		public void onTick(TimePoint point) {
			//System.out.println(point);
			count++;
		}
		
		public int count(){
			return count;
		}
	}
}
