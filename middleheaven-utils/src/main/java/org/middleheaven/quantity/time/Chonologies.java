package org.middleheaven.quantity.time;

import org.middleheaven.culture.Culture;
import org.middleheaven.quantity.time.chono.JavaCalendarCronology;

public class Chonologies {

	
	
	public static Chronology getChonology(Culture culture){
		return new JavaCalendarCronology(culture.toLocale());
	}
}
