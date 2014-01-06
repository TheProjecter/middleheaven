package org.middleheaven.quantity.time;

import java.util.EnumMap;
import java.util.Map;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.core.exception.UnimplementedMethodException;
import org.middleheaven.quantity.measurables.Time;
import org.middleheaven.quantity.unit.Dimension;
import org.middleheaven.quantity.unit.IncompatibleUnitsException;
import org.middleheaven.quantity.unit.SI;
import org.middleheaven.quantity.unit.Unit;
import org.middleheaven.util.Hash;

public final class Duration extends ElapsedTime implements Comparable<Duration>{

	private EnumMap<DurationUnit,Number> fields = new EnumMap<DurationUnit,Number>(DurationUnit.class);

	private Duration() {
		for (DurationUnit t : DurationUnit.values()){
			fields.put(t, 0);
		}
	}
	
	private Duration(Duration other) {
		this();
		for (Map.Entry<DurationUnit,Number> e : other.fields.entrySet()){
			this.fields.put(e.getKey(), e.getValue()); 
		}
	}
	
	public static Duration of(){
		return new Duration ();
	}
	
    public Duration add(DurationUnit type, Number value) {
		fields.put(type,value);
		return this;
	}
	
	public Duration weeks(int weeks){
		return new Duration ( this ).add(DurationUnit.DAYS, 7*weeks);
	}
	
	public Duration days(int days){
		return new Duration ( this ).add( DurationUnit.DAYS, days);
	}
	
	public Duration date(int years, int months,int days){
		return new Duration (this)
		.add(DurationUnit.YEARS,years)
		.add(DurationUnit.MONTHS, months)
		.add(DurationUnit.DAYS, days);
	}

	public Duration time(int hours, int minutes,int seconds){
		return new Duration (this)
		.add(DurationUnit.HOURS,hours)
		.add(DurationUnit.MINUTES, minutes)
		.add(DurationUnit.MILISECONDS, seconds * 1000);
	}

	public Duration years(int ammount){
		return new Duration ( this ).add(DurationUnit.YEARS,ammount);
	}

	public Duration months(int ammount){
		return new Duration ( this ).add(DurationUnit.MONTHS,ammount);
	}

	public Duration hours(int ammount){
		return new Duration ( this ).add(DurationUnit.HOURS,ammount);
	}

	public Duration minutes(int ammount){
		return new Duration ( this ).add(DurationUnit.MINUTES,ammount);
	}

	public Duration seconds(int ammount){
		return new Duration ( this ).add(DurationUnit.MILISECONDS,ammount*1000);
	}

	public Duration miliseconds(long ammount){
		return new Duration ( this ).add(DurationUnit.MILISECONDS,ammount);
	}
	
	public int years(){
		return fields.get(DurationUnit.YEARS).intValue();
	}

	public int months(){
		return fields.get(DurationUnit.MONTHS).intValue();
	}

	public int days(){
		return fields.get(DurationUnit.DAYS).intValue();
	}

	public int hours(){
		return fields.get(DurationUnit.HOURS).intValue();
	}

	public int minutes(){
		return fields.get(DurationUnit.MONTHS).intValue();
	}

	public double secounds(){
		return fields.get(DurationUnit.MILISECONDS).longValue() / 1000d;
	}

	public double miliseconds(){
		return fields.get(DurationUnit.MILISECONDS).longValue();
	}

	/**
	 * @return the unit of this time duration.
	 */
	@Override
	public Unit<Time> unit() {
		int count =0;
		for (DurationUnit t : DurationUnit.values()){
			if (fields.get(t)==null){
				count++;
			}
		}
		
		if (count==1){
			for (DurationUnit t : DurationUnit.values()){
				if (fields.get(t)==null){
					switch (t){
					case YEARS:
						return Unit.unit(Dimension.TIME,"year");
					case MONTHS:
						return Unit.unit(Dimension.TIME,"month");
					case DAYS:
						return Unit.unit(Dimension.TIME,"day");
					case HOURS:
						return SI.HOUR;
					case MINUTES:
						return Unit.unit(Dimension.TIME,"minute");
					case MILISECONDS:
						return SI.MILI(SI.SECOND);
					}
				}
			}
		}
		return Unit.unit(Dimension.TIME,"Duration");

	}

	public Duration negate() {
		Duration d = new Duration();
		for (Map.Entry<DurationUnit,Number> e : this.fields.entrySet()){
			if (e.getValue()!=null){
				d.fields.put(e.getKey(), -1*e.getValue().longValue());
			}
		}
		return d;
	}

	public org.middleheaven.quantity.math.Numeral<?> amount() {
		return reduce();
	}
	
	protected org.middleheaven.quantity.math.Numeral<?> reduce (){
		int count =0;
		DurationUnit field=null;
		for (DurationUnit t : DurationUnit.values()){
			if (fields.get(t)==null){
				count++;
				field = t;
			}
		}
		
		if (count==1){
			return  org.middleheaven.quantity.math.BigInt.valueOf(fields.get(field).longValue());
		} else {
			throw new IllegalStateException("Cannot reduce a multipart duration");
		}
	}


	public Duration times(org.middleheaven.quantity.math.Numeral<?> other) {
		long factor = other.asNumber().longValue();
		Duration d = new Duration(this);
		for (Map.Entry<DurationUnit,Number> e : this.fields.entrySet()){
			if (e.getValue()!=null){
				d.fields.put(e.getKey(), factor*e.getValue().longValue());
			}
		}
		return d;
	}

	public ElapsedTime over(org.middleheaven.quantity.math.Numeral<?> other) {
		// TODO Implements Duration#over(Number); 
		throw new UnimplementedMethodException();
	}
	
	private static Number sum (Number a , Number b){
		if (a!=null){
			if (b!=null){
				return a.longValue() + b.longValue();
			} else {
				return a;
			}
		}
		return b;
	}
	
	public Duration plus(Duration other) throws IncompatibleUnitsException {
		Duration r = new Duration();
		for (DurationUnit t : DurationUnit.values()){
			if (fields.get(t)==null){
				r.fields.put(t, sum(this.fields.get(t) , other.fields.get(t)));
			}
		}
		return r;
	}

	public Duration plus(Period other) throws IncompatibleUnitsException {
		Duration r = new Duration(this);
		Long p =  Long.valueOf(other.milliseconds());
		for (DurationUnit t : DurationUnit.values()){
			if (fields.get(t)==null){
				r.fields.put(t, sum(this.fields.get(t) , p ));
			}
		}
		return r;
	}


	public String toString(){
		StringBuilder builder = new StringBuilder();
		for (DurationUnit t : DurationUnit.values()){
			if (this.fields.get(t)!=null && fields.get(t).longValue()>0){
				builder.append(fields.get(t)).append(' ');
				switch (t){
				case YEARS:
					builder.append("years");
					break;
				case MONTHS:
					builder.append("months");
					break;
				case DAYS:
					builder.append("days");
					break;
				case HOURS:
					builder.append("h");
					break;
				case MINUTES:
					builder.append("minute");
					break;
				case MILISECONDS:
					builder.append("ms");
					break;
				default:
					builder.append(t.name());
				}
				builder.append(' ');
			}
		}
		return builder.toString().trim();
	}

	public Duration duplicate() {
		return new Duration(this);
	}

	/**
	 * Given a TimePoint t , duration x > y iff t + x > t + y and duration x < y iff t + x < t + y 
	 *  
	 * For more information visit http://www.w3.org/TR/xmlschema-2/#duration-order.
	 * @param other the Duration to compare with.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Duration other) { 
		// The underlying chronology is not important as t is a generic time point
		TimePoint t1 = TimeContext.getTimeContext().getChronology().add(CalendarDateTime.origin(), this);
		TimePoint t2 = TimeContext.getTimeContext().getChronology().add(CalendarDateTime.origin(), other);
		
		return t1.getMilliseconds() > t2.getMilliseconds() ? 1 : (t1.getMilliseconds() < t2.getMilliseconds() ? -1 : 0);
	}

    public boolean equals(Object other){
		return (other instanceof Duration) && equalsOther((Duration) other);
	}

	/**
	 * @param other
	 * @return
	 */
	private boolean equalsOther(Duration other) {
		return CollectionUtils.equalContents(this.fields, other.fields);
	}
	
	public int hashCode(){
		return Hash.hash(this.fields.size()).hashCode();
	}

	



}
