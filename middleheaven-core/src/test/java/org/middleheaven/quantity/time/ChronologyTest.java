/**
 * 
 */
package org.middleheaven.quantity.time;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.middleheaven.quantity.time.chono.JavaCalendarCronology;

/**
 * 
 */
public class ChronologyTest {

	
	@Test
	public void testMiliseconds (){
		
		JavaCalendarCronology c= new JavaCalendarCronology();
		
		Date d = new Date(c.milisecondsFor(true, 2010, 2, 3));
		
		assertEquals(new Date (110,1,3) , d);
	}
}
