package org.middleheaven.quantity.time;

import java.util.EnumMap;
import java.util.Map;

import org.middleheaven.core.exception.UnimplementedMethodException;
import org.middleheaven.quantity.measurables.Time;
import org.middleheaven.quantity.unit.Dimension;
import org.middleheaven.quantity.unit.IncompatibleUnitsException;
import org.middleheaven.quantity.unit.SI;
import org.middleheaven.quantity.unit.Unit;

public class Duration extends ElapsedTime implements Comparable<Duration>{

	private enum DurationType{
		MILISECONDS(null),
		MINUTES(MILISECONDS),
		HOURS(MINUTES),
		DAYS(HOURS), 
		MONTHS(DAYS),
		YEARS(MONTHS), ;

		private DurationType next;
		private DurationType(DurationType next){
			this.next = next;
		}
		
		public DurationType getNext(){
			return next;
		}
	}
	
	private EnumMap<DurationType,Number> fields = new EnumMap<DurationType,Number>(DurationType.class);

	protected Duration() {
		for (DurationType t : DurationType.values()){
			fields.put(t, 0);
		}
	}
	
	protected Duration(Duration other) {
		this();
		for (Map.Entry<DurationType,Number> e : other.fields.entrySet()){
			this.fields.put(e.getKey(), e.getValue()); 
		}
	}
	
	public static Duration of(){
		return new Duration ();
	}
	
	private Duration add(DurationType type, Number value) {
		fields.put(type,value);
		return this;
	}
	
	public Duration weeks(int weeks){
		return new Duration ( this ).add(DurationType.DAYS, 7*weeks);
	}
	
	public Duration days(int days){
		return new Duration ( this ).add( DurationType.DAYS, days);
	}
	
	public Duration date(int years, int months,int days){
		return new Duration (this)
		.add(DurationType.YEARS,years)
		.add(DurationType.MONTHS, months)
		.add(DurationType.DAYS, days);
	}

	public Duration time(int hours, int minutes,int seconds){
		return new Duration (this)
		.add(DurationType.HOURS,hours)
		.add(DurationType.MINUTES, minutes)
		.add(DurationType.MILISECONDS, seconds * 1000);
	}

	public Duration years(int ammount){
		return new Duration ( this ).add(DurationType.YEARS,ammount);
	}

	public Duration months(int ammount){
		return new Duration ( this ).add(DurationType.MONTHS,ammount);
	}

	public Duration hours(int ammount){
		return new Duration ( this ).add(DurationType.HOURS,ammount);
	}

	public Duration minutes(int ammount){
		return new Duration ( this ).add(DurationType.MINUTES,ammount);
	}

	public Duration seconds(int ammount){
		return new Duration ( this ).add(DurationType.MILISECONDS,ammount*1000);
	}

	public Duration miliseconds(long ammount){
		return new Duration ( this ).add(DurationType.MILISECONDS,ammount);
	}
	
	public int years(){
		return fields.get(DurationType.YEARS).intValue();
	}

	public int months(){
		return fields.get(DurationType.MONTHS).intValue();
	}

	public int days(){
		return fields.get(DurationType.DAYS).intValue();
	}

	public int hours(){
		return fields.get(DurationType.HOURS).intValue();
	}

	public int minutes(){
		return fields.get(DurationType.MONTHS).intValue();
	}

	public double secounds(){
		return fields.get(DurationType.MILISECONDS).longValue() / 1000d;
	}

	public double miliseconds(){
		return fields.get(DurationType.MILISECONDS).longValue();
	}

	@Override
	public Unit<Time> unit() {
		int count =0;
		for (DurationType t : DurationType.values()){
			if (fields.get(t)==null){
				count++;
			}
		}
		
		if (count==1){
			for (DurationType t : DurationType.values()){
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
		for (Map.Entry<DurationType,Number> e : this.fields.entrySet()){
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
		DurationType field=null;
		for (DurationType t : DurationType.values()){
			if (fields.get(t)==null){
				count++;
				field = t;
			}
		}
		
		if (count==1){
			return  org.middleheaven.quantity.math.Integer.valueOf(fields.get(field).longValue());
		} else {
			throw new IllegalStateException("Cannot reduce a multipart duration");
		}
	}


	public Duration times(org.middleheaven.quantity.math.Numeral<?> other) {
		long factor = other.asNumber().longValue();
		Duration d = new Duration(this);
		for (Map.Entry<DurationType,Number> e : this.fields.entrySet()){
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
		for (DurationType t : DurationType.values()){
			if (fields.get(t)==null){
				r.fields.put(t, sum(this.fields.get(t) , other.fields.get(t)));
			}
		}
		return r;
	}

	public Duration plus(Period other) throws IncompatibleUnitsException {
		Duration r = new Duration(this);
		Long p = new Long(other.milliseconds());
		for (DurationType t : DurationType.values()){
			if (fields.get(t)==null){
				r.fields.put(t, sum(this.fields.get(t) , p ));
			}
		}
		return r;
	}


	public String toString(){
		StringBuilder builder = new StringBuilder();
		for (DurationType t : DurationType.values()){
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
				}
				builder.append(' ');
			}
		}
		return builder.toString().trim();
	}

	@Override
	public Object clone() {
		return new Duration(this);
	}

	/**
	 * Given a TimePoint t , duration x > y iff t + x > t + y and duration x < y iff t + x < t + y 
	 *  
	 * {@link http://www.w3.org/TR/xmlschema-2/#duration-order}
	 * @param o
	 * @return
	 */
	@Override
	public int compareTo(Duration other) { 
		// The underlying chronology is not important as t is a generic time point
		TimePoint t1 = TimeContext.getTimeContext().getChronology().add(CalendarDateTime.origin(), this);
		TimePoint t2 = TimeContext.getTimeContext().getChronology().add(CalendarDateTime.origin(), other);
		
		return (int)(t2.milliseconds()-t1.milliseconds());
	}



	



}
