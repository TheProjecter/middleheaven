package org.middleheaven.quantity.time;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measurables.Time;
import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.unit.IncompatibleUnitsException;
import org.middleheaven.quantity.unit.SI;
import org.middleheaven.quantity.unit.Unit;

public class Period extends ElapsedTime implements Comparable<Period>{

	
	public static Period miliseconds (long miliseconds){
		return new Period(DecimalMeasure.measure(miliseconds, 1, SI.MILI(SI.SECOND)));
	}
	
	public static Period seconds (long seconds){
		return new Period(DecimalMeasure.measure(seconds, 1, SI.SECOND));
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
	
	public ElapsedTime over(org.middleheaven.quantity.math.Number other) {
		return new Period (this.measure.over(Real.valueOf(other)));
	}

	public ElapsedTime times(org.middleheaven.quantity.math.Number other) {
		return new Period (this.measure.times(Real.valueOf(other)));
	}

	public String toString(){
		return measure.toString();
	}

	@Override
	public int compareTo(Period other) {
		return this.measure.compareTo(other.measure);
	}
	
}
