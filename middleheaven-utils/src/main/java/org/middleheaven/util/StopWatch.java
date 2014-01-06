package org.middleheaven.util;

import org.middleheaven.quantity.time.Period;


/**
 * An utility to compute relative time intervals. 
 * 
 */
public final class StopWatch {

	/**
	 * 
	 * @return
	 */
	public static StopWatch start(){
		return new StopWatch();
	}

	private long mark;
	
	private StopWatch(){
		mark = System.nanoTime();
	}
	
	public StopWatch reset(){
		mark = System.nanoTime();
		return this;
	}
	
	/**
	 * 
	 * @return Duration in nanoseconds
	 */
	public Period mark(){
		return Period.nanoseconds(System.nanoTime() - this.mark);
	}
}
