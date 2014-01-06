/*
 * Created on 10/Mar/2007
 */
package org.middleheaven.quantity.time;

/**
 * A time instance represented by the number of miliseconds to an epoc.
 * Epoc is Java default epoc.
 * 
 */
public interface TimePoint extends Comparable<TimePoint>{

    /**
     * Creates an interval from this point to another.
     * The algoritm will order the timepoints so the start of the interval 
     * is the minor of the two and the end of the interval is the greater of the two
     * @param other
     * @return
     */
    public TimeInterval until(TimePoint other);
    
    /**
     * @return The number of milisecounds elapsed from the epoc reference
     * 
     */
    public long getMilliseconds();
}
