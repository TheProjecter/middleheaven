package org.middleheaven.collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.collections.interval.Interval;


public class IntervalTest {

	
	@Test
	public void testInterval(){
		
		Interval<Integer> t = Interval.between(2, 5);
		
		
		assertFalse(t.contains(5,true,false));
		assertFalse(t.contains(2,false,true));
		assertTrue(t.contains(3,true,true));
		assertTrue(t.contains(3,false,false));
	}
}
