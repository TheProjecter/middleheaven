package org.middleheaven.quantity.time;

import java.util.Date;

import org.middleheaven.util.Hash;

public class EpocTimePoint implements TimePoint  {


	private final long timeFromEpoc;
	
	public EpocTimePoint(long timeFromEpoc) {
		this.timeFromEpoc = timeFromEpoc;
	}

	@Override
	public long getMilliseconds() {
		return timeFromEpoc;
	}

	@Override
	public TimeInterval until(TimePoint other) {
		 return new TimeInterval(this,other);
	}

	@Override
	public int compareTo(TimePoint other) {
		return (int)(this.timeFromEpoc - other.getMilliseconds());
	}

	public String toString(){
		return new Date(this.timeFromEpoc).toString();
	}
	public boolean equals(Object other){
		return other instanceof EpocTimePoint && equalsOther((EpocTimePoint)other);
	}
	
	/**
	 * @param other
	 * @return
	 */
	private boolean equalsOther(EpocTimePoint other) {
		return this.timeFromEpoc == other.timeFromEpoc;
	}

	public int hashCode (){
		return Hash.hash(this.timeFromEpoc).hashCode();
	}
}
