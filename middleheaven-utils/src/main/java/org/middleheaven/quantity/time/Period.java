package org.middleheaven.quantity.time;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measurables.Time;
import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.unit.IncompatibleUnitsException;
import org.middleheaven.quantity.unit.SI;
import org.middleheaven.quantity.unit.Unit;

/**
 * A continuous {@link ElapsedTime}.
 */
public class Period extends ElapsedTime implements Comparable<Period>{

	
	public static Period miliseconds (long miliseconds){
		return new Period(DecimalMeasure.measure(miliseconds, 1, SI.MILI(SI.SECOND)));
	}
	
	public static Period seconds (long seconds){
		return new Period(DecimalMeasure.measure(seconds, 1, SI.SECOND));
	}
	
	public static Period minutes (long hours){
		return new Period(DecimalMeasure.measure(hours * 60 , 1, SI.SECOND));
	}
	
	public static Period hours (long hours){
		return minutes(hours * 60);
	}
	
	/**
	 * 
	 * @param days
	 * @return
	 */
	public static Period days (int days){
		return hours(days * 24L);
	}
	
	/**
	 * 
	 * @param weeks
	 * @return
	 */
	public static Period weeks (int weeks){
		return days(weeks * 7);
	}
	
	public static Period nanoseconds (long nanoseconds){
		return new Period(DecimalMeasure.measure(nanoseconds, 1, SI.NANO(SI.SECOND)));
	}
	
    private DecimalMeasure<Time> measure;
    
    private Period(DecimalMeasure<Time> measure) {
		this.measure = measure;
	}

	public Period negate() {
		return new Period(measure.negate());
	}
	
	public long milliseconds(){
		return measure.convertTo(SI.MILI(SI.SECOND)).amount().asNumber().longValue();
	}

	public long seconds(){
		return measure.convertTo(SI.SECOND).amount().asNumber().longValue();
	}
	
	public long nanoseconds(){
		return measure.convertTo(SI.NANO(SI.SECOND)).amount().asNumber().longValue();
	}
	
	public Period plus(Period other) throws IncompatibleUnitsException {
		return new Period (this.measure.plus(other.measure));
	}
	
	public Duration plus(Duration other) throws IncompatibleUnitsException {
		return other.plus(this);
	}

	public Unit<Time> unit() {
		return measure.unit();
	}

	@Override
	public Object clone() {
		return this; // imutable
	}
	
	public ElapsedTime over(org.middleheaven.quantity.math.Numeral other) {
		return new Period (this.measure.over(Real.valueOf(other)));
	}

	public ElapsedTime times(org.middleheaven.quantity.math.Numeral other) {
		return new Period (this.measure.times(Real.valueOf(other)));
	}

	public String toString(){
		return measure.toString();
	}

	@Override
	public int compareTo(Period other) {
		return this.measure.compareTo(other.measure);
	}
	
    public boolean equals(Object other){
		return (other instanceof Period) && equalsOther((Period) other);
	}

	/**
	 * @param other
	 * @return
	 */
	private boolean equalsOther(Period other) {
		return this.measure.compareTo(other.measure) == 0;
	}
	
	public int hashCode(){
		return this.measure.hashCode();
	}

	public DecimalMeasure<Time> asMeasure() {
		return this.measure;
	}
	
}
