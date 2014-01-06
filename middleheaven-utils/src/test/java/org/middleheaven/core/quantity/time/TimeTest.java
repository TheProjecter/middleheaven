/**
 * 
 */
package org.middleheaven.core.quantity.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.quantity.time.Duration;
import org.middleheaven.quantity.time.DurationUnit;
import org.middleheaven.quantity.time.Period;


/**
 * 
 */
public class TimeTest {

	@Test
	public void test() {
		
		CalendarDate a = CalendarDate.date(2010, 1, 1);

		assertEquals(1, a.upTo(a).size());
		
		
		CalendarDate b = CalendarDate.date(2012, 6, 20);
		
		assertTrue(a.upTo(b).size() > 30);
		
		
		CalendarDate c = CalendarDate.date(2012, 6, 10);
		
		assertEquals(11, c.upTo(b).size());
	}

	
	@Test
	public void testPeriodUnits(){
		
		Period period = Period.nanoseconds(20);
		
		assertEquals("(20 ± 1) ns", period.toString());
	}
	
	@Test
	public void testDurationCalculation(){

		CalendarDate a = CalendarDate.date(2013, 1, 1);
		CalendarDate b = CalendarDate.date(2013, 1, 30);
		
	    assertTrue(Duration.of().days(29).compareTo(a.differenceTo(b, DurationUnit.DAYS)) == 0);
	    
		CalendarDate c = CalendarDate.date(2013, 3, 30);
		
		assertTrue(Duration.of().days(59).compareTo(b.differenceTo(c, DurationUnit.DAYS)) == 0);
		   
	    assertTrue(Duration.of().days(88).compareTo(a.differenceTo(c, DurationUnit.DAYS)) == 0);
		  
		CalendarDateTime d = CalendarDateTime.at(2013, 1 , 1 , 2 , 0 ,0);
		
		assertTrue(Duration.of().days(0).compareTo(a.differenceTo(d, DurationUnit.DAYS)) == 0);
		assertTrue(Duration.of().hours(2).compareTo(a.differenceTo(d, DurationUnit.HOURS)) == 0);
	}
	
	@Test
	public void testDurationComparison(){
		
		Duration a = Duration.of().hours(20);
		Duration b = Duration.of().days(20);
		Duration c = Duration.of().days(30);
		
	
		assertTrue(b.compareTo(c) < 0);
		assertTrue(a.compareTo(c) < 0);
		assertTrue(a.compareTo(c) < 0);
	}
}
