/**
 * 
 */
package org.middleheaven.collections;

import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.quantity.time.CalendarDateIncrementor;
import org.middleheaven.quantity.time.Duration;
import org.middleheaven.quantity.time.ElapsedTime;
import org.middleheaven.util.Incrementor;

/**
 * 
 */
public class CalendarDateRangeBuilder<T extends CalendarDate> extends RangeBuilder<T, ElapsedTime> {

	/**
	 * Constructor.
	 * @param start
	 * @param incrementor
	 */
	CalendarDateRangeBuilder(T start) {
		super(start, (Incrementor<T, ElapsedTime>) new CalendarDateIncrementor(Duration.of().days(1)));
	}

}
