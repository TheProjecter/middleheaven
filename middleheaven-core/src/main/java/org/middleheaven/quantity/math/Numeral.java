package org.middleheaven.quantity.math;


import java.io.Serializable;
import java.math.BigDecimal;

import org.middleheaven.quantity.Quantity;
import org.middleheaven.quantity.math.structure.Field;
import org.middleheaven.quantity.math.structure.GroupAdditive;
import org.middleheaven.quantity.math.structure.Ring;
import org.middleheaven.quantity.measurables.Dimensionless;
import org.middleheaven.quantity.unit.SI;
import org.middleheaven.quantity.unit.Unit;

/**
 * Represents a dimensionless <code>Quantity</code>.
 * 
 * @author Sergio M.M. Taborda
 * 
 *
 */
public abstract class Numeral<T extends Numeral<T>> implements Field<T> , Quantity<Dimensionless> , Serializable, GroupAdditive<T>, Ring<T>  {


	private static final long serialVersionUID = -3007304512447112381L;

	public final Unit<Dimensionless> unit() {
		return SI.DIMENTIONLESS;
	}
	
	protected abstract Numeral<?> promote (Numeral<?> other);
	
	public abstract BigInt toBigInt();
	public abstract Real toReal();
	public abstract Complex toComplex();
	
	public Numeral<?> plus (Numeral<?> other){
		return Real.valueOf(this).plus(Real.valueOf(other));
	}


	public Numeral<?> times (Numeral<?> other){
		return Real.valueOf(this).times(Real.valueOf(other));
	}
	
	public abstract T plus (java.lang.Number n);
	
	public abstract T minus (java.lang.Number n);
	
	public abstract T times (java.lang.Number n);
	
	public abstract T over (java.lang.Number n);
	
	public T minus (T other){
		return this.plus(other.negate());
	}
	
	public T incrementBy(T increment) {
		return this.plus(increment);
	}
	
	protected abstract int rank();
	
	public abstract T inverse();

	public abstract T over(T other);
	
	public abstract BigDecimal asNumber();
	
	/*
	public boolean equals(T other){
		return this.asNumber().compareTo(other.asNumber())==0;
	}
	*/

	public final boolean equals(Object other){
		return other instanceof Numeral && equals((Numeral<?>)other);
	}
	

	protected boolean equals(Numeral<?> other){
		if (this.getClass().isInstance(other)){
			@SuppressWarnings("unchecked") T n = (T) this.getClass().cast(other);
			return this.equalsSame(n);
		} else {
			return this.asNumber().compareTo(other.asNumber())==0;
		}
	}
	
	protected abstract boolean equalsSame(T other);

	public abstract boolean isZero();
	
	public abstract String toString();

   

	

}
