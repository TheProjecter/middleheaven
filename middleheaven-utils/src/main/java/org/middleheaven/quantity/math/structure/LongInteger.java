package org.middleheaven.quantity.math.structure;

import java.math.BigDecimal;

import org.middleheaven.quantity.math.BigInt;
import org.middleheaven.quantity.math.Complex;
import org.middleheaven.quantity.math.Numeral;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.math.RealField;
import org.middleheaven.util.Hash;
import org.middleheaven.util.Incrementable;

class LongInteger extends BigInt {


	private static final long serialVersionUID = 7891405602158515389L;
	
	private static final LongInteger ONE = new LongInteger(1);
	private static final LongInteger ZERO = new LongInteger(0);
	
    private long value = 0;
	
	public LongInteger(String value) {
		this.value = Long.parseLong(value);
	}
	
    LongInteger(long value) {
    	this.value = value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BigInt next() {
		return new LongInteger(value+1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BigInt previous() {
		return new LongInteger(value-1);
	}
	
	@Override
	public boolean isZero() {
		return value ==0;
	}

	@Override
	public BigDecimal asNumber() {
		return new BigDecimal(value);
	}

	@Override
	public BigInt inverse() {
		return new LongInteger(1/value);
	}

	@Override
	public BigInt negate() {
		return new LongInteger(-value);
	}

	@Override
	public BigInt plus(BigInt other) {
		if (other instanceof LongInteger){
			return new LongInteger(this.value + ((LongInteger)other).value);
		} else {
			return other.plus(this);
		}
	}

	@Override
	public BigInt times(BigInt other) {
		if (other instanceof LongInteger){
			return new LongInteger(this.value * ((LongInteger)other).value);
		} else {
			return other.plus(this);
		}
	}

	@Override
	public String toString() {
		return Long.toString(value);
	}


	
	@Override
	public BigInt over(BigInt other) {
		return new LongInteger(this.value/other.asNumber().longValue());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Incrementable<BigInt>> T incrementBy(BigInt increment) {
		return (T)this.plus(increment);
	}

	@Override
	public boolean isEven() {
		return this.value % 2 == 0 ;
	}
	
	@Override
	public BigInt minus(java.lang.Number n) {
		return this.minus(new LongInteger(n.longValue()));
	}

	@Override
	public BigInt over(java.lang.Number n) {
		return this.over(new LongInteger(n.longValue()));
	}

	@Override
	public BigInt plus(java.lang.Number n) {
		return this.plus(new LongInteger(n.longValue()));
	}

	@Override
	public BigInt times(java.lang.Number n) {
		return this.times(new LongInteger(n.longValue()));
	}


	protected boolean equalsOther(LongInteger other){
		return  this.value == other.value;
	}

	@Override
	protected boolean equalsSame(BigInt other) {
		return equalsOther( (LongInteger) other);
	}

	@Override
	public int compareTo(Numeral<? super BigInt> o) {
		return this.asNumber().compareTo(o.asNumber());
	}
	
	@Override
	public int compareTo(BigInt other) {
		return this.asNumber().compareTo(other.asNumber());
	}


	@Override
	public BigInt toBigInt() {
		return this;
	}

	@Override
	public Complex toComplex() {
		return Complex.rectangular(toReal(), RealField.getInstance().zero());
	}

	@Override
	public Real toReal() {
		return Real.valueOf(Long.valueOf(this.value));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Hash.hash(this.value).hashCode();
	}

	@Override
	public BigInt abs() {
		return new LongInteger(this.value < 0 ? -this.value : this.value);
	}




}
