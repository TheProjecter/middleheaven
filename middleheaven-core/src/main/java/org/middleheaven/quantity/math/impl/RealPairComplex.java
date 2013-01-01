package org.middleheaven.quantity.math.impl;

import java.math.BigDecimal;

import org.middleheaven.quantity.math.BigInt;
import org.middleheaven.quantity.math.Complex;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.math.RealField;
import org.middleheaven.util.Hash;

/**
 * Complex implementation using two {@link Real}s.
 */
class RealPairComplex extends Complex {


	private static final long serialVersionUID = 7330930288749616784L;
	
	 private static final Complex I = new RealPairComplex(RealField.getInstance().zero(),RealField.getInstance().one());

	private Real real;
	private Real imaginary;

	/**
	 * 
	 * Constructor.
	 * @param real
	 * @param imaginary
	 */
	 RealPairComplex(Real real, Real imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	 /**
	  * 
	  * Constructor.
	  * @param value
	  */
	RealPairComplex(String value) {

		final int pos = value.indexOf("+i");
		if ( pos < 0){
			this.real = Real.valueOf(value);
			this.imaginary = RealField.getInstance().zero();
		} else {
			this.real = Real.valueOf(value.substring(0,pos));
			this.imaginary = Real.valueOf(value.substring(pos+2));
			
		}
	}

	
	@Override
	public BigDecimal asNumber() {
		return this.magnitude().asNumber();
	}

	@Override
	public boolean isZero() {
		return real.isZero() && imaginary.isZero();
	}

	@Override
	protected int rank() {
		return 1;
	}

	@Override
	public String toString() {
		return this.real.toString() + "+i" + this.imaginary.toString();
	}

	@Override
	public Complex i() {
		return I;
	}

	@Override
	public Real magnitude() {
		return this.real.times(this.real).plus(this.imaginary.times(this.imaginary)).sqrt();
	}
	
	private Real magnitudeSquare(){
		return this.real.times(this.real).plus(this.imaginary.times(this.imaginary));
	}
	
	@Override
	public Complex times(Complex other) {
		// (a+ib) * (c+di) = (ac-db) + (bc+ad)i
		return  new RealPairComplex(
				this.real.times(other.toReal()).minus(other.toImaginary().times(this.imaginary)) , 
				this.imaginary.times(other.toReal()).plus(this.real.times(other.toImaginary()))
		);
	}
	
	@Override
	public Complex over(Complex other) {
		// (a+ib)/(c+id) = (a+ib) * (c+id)^-1  
		return this.times(other.inverse());
	}

	@Override
	public Complex inverse() {
		// a+ib ->  a/m - bi/m  , m = a*a + b*b
		Real magnitude2 = this.magnitudeSquare();
		if (magnitude2.isZero()){
			throw new ArithmeticException("Inverse of " + this.toString() + " is not defined");
		}
		
		final Real r = this.real.over(magnitude2);
		final Real i = this.imaginary.over(magnitude2);
		
		return new RealPairComplex(r, i);
	}

	
	@Override
	public Complex negate() {
		return new RealPairComplex(this.real.negate(), this.imaginary.negate());
	}

	@Override
	public Complex plus(Complex other) {
		return  new RealPairComplex(this.real.plus(other.toReal()) , this.imaginary.plus(other.toImaginary()));
	}

	@Override
	public Complex minus(Complex other) {
		return  new RealPairComplex(this.real.minus(other.toReal()) , this.imaginary.minus(other.toImaginary()));
	}

	@Override
	public Complex conjugate() {
		return new RealPairComplex(this.real, this.imaginary.negate());
	}

	@Override
	public Real toImaginary() {
		return imaginary;
	}

	@Override
	public Real toReal() {
		return real;
	}

	@Override
	public Complex minus(Number n) {
		return this.minus(new RealPairComplex(Real.valueOf(n), RealField.getInstance().zero()));
	}

	@Override
	public Complex over(Number n) {
		return this.over(new RealPairComplex(Real.valueOf(n), RealField.getInstance().zero()));
	}

	@Override
	public Complex plus(Number n) {
		return this.plus(new RealPairComplex(Real.valueOf(n), RealField.getInstance().zero()));
	}

	@Override
	public Complex times(Number n) {
		return this.times(new RealPairComplex(Real.valueOf(n), RealField.getInstance().zero()));
	}

	protected boolean equalsOther(RealPairComplex other){
		return  this.real.equals(other.real) && this.imaginary.equals(other.imaginary);
	}

	@Override
	protected boolean equalsSame(Complex other) {
		return equalsOther( (RealPairComplex) other);
	}

	/**
	 * 
	 * @return the real parte has a {@link BigInt}.
	 */
	@Override
	public BigInt toBigInt() {
		return toReal().toBigInt();
	}

	/**
	 * 
	 * @return this value
	 */
	@Override
	public Complex toComplex() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Hash.hash(this.real).hash(this.imaginary).hashCode();
	}

	@Override
	public Complex abs() {
		return magnitude().toComplex();
	}




}
