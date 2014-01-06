package org.middleheaven.quantity.time.ephemeris;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.quantity.time.DateHolder;
import org.middleheaven.quantity.time.DayOfWeek;
import org.middleheaven.quantity.time.Duration;
import org.middleheaven.quantity.time.Year;


/**
 * Determines Easter ephemeris. 
 *
 * Days marked as Saturday, Sunday and Easter are considered non-working days 
 */
public class EasterBasedCalculatedEphemerisModel extends EphemerisModel {

	private static final long serialVersionUID = -1927771312432233408L;

	/**
	 * @param year
	 * @return Easter date
	 * @see http://www.tondering.dk/claus/cal/node3.html#SECTION003132000000000000000
	 */
	public static DateHolder calculaEaster(Year aYear){
		
		int year = aYear.ordinal();
		
		// Use Gregorian Calendar
		int G = year % 19;
		int C = year / 100;
		int H = (C-C/4 - (8*C+13)/25 + 19* G + 15) % 30;
		int I = H - (H/28) * (1 - (29 / (H+1))* ((21-G)/11));
		int J = (year + year/4 + I + 2 - C + C/4) % 7;
		int L = I - J;
		
		int month = 3 + (L + 40)/44;
		int day = L + 28 - 31 *(month/4);
		
		return CalendarDate.date(year,month,day);
	}
	
	private final Map<DateHolder, Ephemeris> dateEphemeris = new HashMap<DateHolder, Ephemeris>();
	
	@Override
	public Set<Ephemeris> getEphemeris(DateHolder date) {
		Ephemeris dayEphemeris = dateEphemeris.get(date);
		
		if (dayEphemeris==null){
			DateHolder easterSunday =calculaEaster(date.year());
			dateEphemeris.put(easterSunday, new DefaultEphemeris("Easter",false,easterSunday));

			// 2 days before easter
			DateHolder goodFriday = easterSunday.minus(Duration.of().days(2));
			dateEphemeris.put(goodFriday, new DefaultEphemeris("Good Friday",true,goodFriday));
			
			// 42 days before easter
			DateHolder ashWendnesday = easterSunday.minus(Duration.of().days(42)).nearestBefore(DayOfWeek.WEDNESDAY);
			
			dateEphemeris.put(ashWendnesday, new DefaultEphemeris("Ash Wednesday",true,ashWendnesday));
			
			// 7 weeks (49 days) after easter
			DateHolder whitSunday = easterSunday.plus(Duration.of().days(49));
			dateEphemeris.put(whitSunday, new DefaultEphemeris("Pentecost",false,whitSunday));
			
			// 39 days after easter, 10 days before whit sunday
			DateHolder ascensionDay = whitSunday.minus(Duration.of().days(10));
			dateEphemeris.put(ascensionDay, new DefaultEphemeris("Ascension Day",false,ascensionDay));
			
			// 43 days before easter , 1 day before Ash Wednesday
			DateHolder mardiGras = ashWendnesday.previousDate();
			dateEphemeris.put(mardiGras, new DefaultEphemeris("Mardi Gras",false,mardiGras));
			
		} 

	    dayEphemeris = dateEphemeris.get(date);
		if (dayEphemeris==null){
			return Collections.emptySet();
		} else {
			return Collections.singleton(dayEphemeris);
		}

	}




}
