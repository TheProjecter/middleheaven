/*
 * Created on 10/Mar/2007
 */
package org.middleheaven.quantity.time;


import org.middleheaven.collections.interval.Interval;


/**
 * An interval between <code>TimePoint</code>s 
 */
public class TimeInterval extends Interval<TimePoint>{

    /**
     * @param start
     * @param end
     */
    protected TimeInterval(TimePoint start, TimePoint end) {
        super(new TimePointComparator());
        // reorder if necessary
        if (start.compareTo(end)>0){
            this.start = end;
            this.end = start;
        } else {
            this.start = start;
            this.end = end;
        }
    }
    
//    public Duration duration(){
//    	return Duration.of().miliseconds(end.getMilliseconds()-start.getMilliseconds());
//    }
//
//    public Period pediod(){
//    	return Period.miliseconds(end.getMilliseconds()-start.getMilliseconds());
//    }
}
