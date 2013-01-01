package org.middleheaven.quantity.unit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.middleheaven.quantity.measure.Measurable;



public class CompositeUnit<E extends Measurable> extends Unit<E>{

	
	public static <T extends Measurable> Unit<T> raise (Unit<?> unit , int exponent){
		if (exponent==0){
			return SI.DIMENTIONLESS.cast();
		} else if (exponent>0){
			 Unit<T> a = unit.cast();
			 Unit<T> s = unit.cast();
			 for (int i = 1 ; i < exponent ; i++){
				 s = s.times(a);
			 }
			 return s;
		} else {
			Unit<T> a = unit.cast();
			Unit<T> s = unit.cast();
			 for (int i = 0 ; i <= -exponent ; i++){
				 s = s.over(a);
			 }
			 return s;
		}
	}
	
	public static <T extends Measurable> Unit<T> root (Unit<?> unit , int exponent){
		if (exponent==0){
			return SI.DIMENTIONLESS.cast();
		} else if (exponent>0){
			 Unit<T> a = unit.cast();
			 for (int i = 1 ; i < exponent ; i++){
				 a = a.times(a);
			 }
			 return a;
		} else {
			Unit<T> a = unit.cast();
			 for (int i = 1 ; i < -exponent ; i++){
				 a = a.over(a);
			 }
			 return a;
		}
	}
	
	public static <T extends Measurable> Unit<T> times(Unit<?> a , Unit<?> b){
		CompositeUnit<T> c = new CompositeUnit<T>();
		c.add(a,1);
		c.add(b,1);
		return c.simplify();
	}
	
	public static <A extends Measurable, B extends Measurable, T extends Measurable> Unit<T>  over(Unit<A> a , Unit<B> b){
		CompositeUnit<T> c = new CompositeUnit<T>();
		c.add(a,1);
		c.add(b,-1);
		return c.simplify();
	}
	
	public static <T extends Measurable> Unit<T> invert(Unit<?> unit) {
		CompositeUnit<T> c = new CompositeUnit<T>();
		c.add(unit,-1);
		return c.simplify();
	}
	

	private Map<String,UnitPower> units = new LinkedHashMap<String,UnitPower>();
	
	private Dimension<E> currentDimention = Dimension.DIMENTIONLESS.simplify();
	
	CompositeUnit(){}
	CompositeUnit(CompositeUnit<E> other){
		this.units =  new LinkedHashMap<String,UnitPower>(other.units);
		this.currentDimention = other.currentDimention;

	}
	
	private void add(Unit<?> other , int sign){
		if (other instanceof CompositeUnit){
			//	 merge compositions
    		CompositeUnit<?> c = (CompositeUnit<?>)other;

    		for (Map.Entry<String,UnitPower> entry : c.units.entrySet()){
    			UnitPower p = entry.getValue();
    			UnitPower q = units.get(p.unit.symbol());
    			if (q==null){
    				p = new UnitPower(p.unit(),sign * p.exponent());
    				units.put(p.unit.symbol(), p);
    			} else {
    				p = new UnitPower(p.unit(),q.exponent + sign * p.exponent());
    				units.put(p.unit.symbol(), p);
    			}
    		}

    	} else {
    		
    		final String symbol = other.symbol();
			UnitPower c = units.get(symbol);
    		if (c==null){
    			units.put(symbol, new UnitPower(other,sign));
    		} else {
    			units.put(symbol, new UnitPower(c.unit,c.exponent+sign));
    		}
    		
    		
    	}
		
		if (sign>0){
			currentDimention = currentDimention.times(other.dimension());
		} else {
			currentDimention = currentDimention.over(other.dimension());
		}
	}
	
	public Dimension<E> dimension() {
		return currentDimention;
	}

	public boolean isCompatible(Unit<?> other) {
		return this.dimension().equals(other.dimension());
	}

	@Override
	public boolean equalsOther(Unit<?> other) {
		return other instanceof CompositeUnit && equalsOther((CompositeUnit<?>)other);
	}

	public boolean equalsOther(CompositeUnit<?> other) {
		return this.currentDimention.equals(other.currentDimention);
	}


	public String symbol() {
		StringBuilder builder = new StringBuilder();
		for (Iterator<UnitPower> it = units.values().iterator(); it.hasNext();){
			builder.append(it.next().toString());
		}
		return builder.toString();
	}
	
	public Unit<E> simplify(){
		//	elimina todos os dimensionless
		for (Iterator<UnitPower> it = units.values().iterator(); it.hasNext();){
			UnitPower d = it.next();
			if (d.exponent==0){
				it.remove();
			}
		}
		
		// else 
		return this;
	}
	
	public String toString(){
		return symbol();
	}


	@Override
	public <C extends Measurable> Unit<C> cast() {
		return (Unit<C>) this;
	}

	@Override
	public <T extends Measurable> Unit<T> times(Unit<?> other) {
		CompositeUnit<T> result = new CompositeUnit<T>();
		result.add(this, 1);
    	result.add(other, 1);
    	return result.simplify();
	}



	@Override
	public Unit<E> minus(Unit<E> other) throws IncompatibleUnitsException {
		return other;
	}

	@Override
	public Unit<E> plus(Unit<E> other) throws IncompatibleUnitsException {
		return other;
	}

	@Override
	public <T extends Measurable> Unit<T> over(Unit<?> other) {
		CompositeUnit<T> result = new CompositeUnit<T>();
		result.add(this, 1);
    	result.add(other, -1);
    	return result.simplify();
	}


	@Override
	public <C extends Measurable> Unit<C> raise(int exponent) {
		return raise (this, exponent);
	}





}
