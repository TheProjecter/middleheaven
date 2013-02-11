package org.middleheaven.quantity.math.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.middleheaven.quantity.math.BigInt;
import org.middleheaven.quantity.math.Complex;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.util.Hash;
import org.middleheaven.util.Incrementable;

/**
 * 
 */
public class BigDecimalReal extends Real{

	private static final long serialVersionUID = 1L;

	private static final int SCALE = 22;
	
	private final static BigDecimalReal ZERO  = new BigDecimalReal(BigDecimal.ZERO , BigDecimal.ONE);
	private final static BigDecimalReal ONE  = new BigDecimalReal(BigDecimal.ONE , BigDecimal.ONE);

	// Ratio pattern like implementation to adjourn the 
	// division as much as possible.
	private BigDecimal numerator; // only contains integer value
	private BigDecimal denominator; // only contains integer value

	public static BigDecimalReal valueOf (String value){
		return valueOf(new BigDecimal(value));
	}
	
	public static BigDecimalReal valueOf (BigDecimal value){
		final BigDecimal scale = BigDecimalMath.intPower(BigDecimal.TEN, value.scale(), 0);
		return valueOf(value.multiply( scale), scale);
	}
	
	public static BigDecimalReal valueOf (long value){
		return valueOf(BigDecimal.valueOf(value), BigDecimal.ONE);
	}
	
	public static BigDecimalReal valueOf (BigDecimal numerator,BigDecimal denominator){
		if (numerator.compareTo(BigDecimal.ZERO) == 0){
			return ZERO;
		} else if (numerator.compareTo(denominator) == 0){
			return ONE;
		}
		return new BigDecimalReal(numerator, denominator);
	}

	private BigDecimalReal (BigDecimal numerator,BigDecimal denominator){
		
		BigDecimal gcd = BigDecimalMath.gcd ( numerator, denominator);

		if (gcd.signum() !=0 ){
			this.numerator = numerator.divide(gcd);
			this.denominator = denominator.divide(gcd);
		} else {
			this.numerator = numerator;
			this.denominator = denominator;
		}
	}

	public BigDecimal asNumber() {
		return numerator.signum()==0 
		? BigDecimal.ZERO 
				: denominator.compareTo(BigDecimal.ONE) == 0
				? numerator 
					: numerator.divide(denominator, SCALE, RoundingMode.HALF_EVEN);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInteger() {
		return this.numerator.signum() == 0 || this.numerator.divideAndRemainder(denominator)[1].compareTo(BigDecimal.ONE) == 0;
	}

	@Override
	public boolean isZero() {
		return numerator.signum()==0;
	}

	@Override
	public Real inverse() {
		return new BigDecimalReal(denominator, numerator);
	}

	@Override
	public Real negate() {
		if (denominator.signum()>0){
			// negate numerator
			return new BigDecimalReal(numerator.negate(), denominator);
		} else {
			// negate denominator
			return new BigDecimalReal(numerator, denominator.negate());
		}
	}

	@Override
	public Real plus(Real other) {
		if (other instanceof BigDecimalReal){
			return plus (((BigDecimalReal)other).numerator , ((BigDecimalReal)other).denominator);
		} else {
			return plus (new BigDecimal(other.toString()), BigDecimal.ONE);
		}
	}

	private BigDecimalReal plus(BigDecimal otherNumerator, BigDecimal otherDenominator){
		BigDecimal[] multipliers = this.multipliers(this.denominator,otherDenominator );

		return new BigDecimalReal(
				this.numerator.multiply(multipliers[0]).add(otherNumerator.multiply(multipliers[1])),
				this.denominator.multiply(multipliers[0])
		).simplify();
	}

	private BigDecimalReal simplify() {
		if (denominator.compareTo(BigDecimal.ONE) == 0 || numerator.signum()==0){
			return this; // is zero or divided by 1
		}

		BigDecimal gcd = BigDecimalMath.gcd ( numerator, denominator);

		if (gcd.signum()==0){
			return this;
		}

		try{
			return new BigDecimalReal(
					numerator.divide(gcd),
					denominator.divide(gcd)
			);
		} catch (ArithmeticException e){
			return this;
		}
	}

	
	private BigDecimal[] multipliers (BigDecimal d1 , BigDecimal d2){
		int compare = d1.compareTo(d2);
		BigDecimal[] division;
		if ( compare==0){
			return new BigDecimal[]{BigDecimal.ONE,BigDecimal.ONE};
		} else if ( compare <0 ){
			division = d2.divideAndRemainder(d1);
			if (division[1].signum()==0){
				return new BigDecimal[]{division[0],BigDecimal.ONE};
			} 
		} else {
			division = d1.divideAndRemainder(d2);
			if (division[1].signum()==0){
				return new BigDecimal[]{BigDecimal.ONE,division[0]};
			} 
		}
		return new BigDecimal[]{d2,d1};
	}


	@Override
	public Real times(Real other) {
		if (other instanceof BigDecimalReal){
			return times(((BigDecimalReal)other).numerator, ((BigDecimalReal)other).denominator);
		} else {
			return times(new BigDecimal(other.toString()) , BigDecimal.ONE);
		}
	}

	private BigDecimalReal times(BigDecimal otherNumerator, BigDecimal otherDenominator){
		if (otherNumerator.compareTo(BigDecimal.ZERO) ==0){
			return BigDecimalReal.ZERO;
		} else {
			return new BigDecimalReal(
					this.numerator.multiply(otherNumerator),
					this.denominator.multiply(otherDenominator)
			).simplify();
		}
	}

	@Override
	public Real over(Real other) {
		
		if (other.isZero()){
			throw new ArithmeticException("Division by zero");
		}
		
		// times the inverse
		if (other instanceof BigDecimalReal){
			return times( ((BigDecimalReal)other).denominator,((BigDecimalReal)other).numerator);
		} else {
			return times( BigDecimal.ONE , new BigDecimal(other.toString()));
		}
	}


	@Override
	public String toString() {
		return this.denominator.compareTo(BigDecimal.ONE) == 0 ? this.numerator.toString() : this.numerator.toString() + "/" + this.denominator.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Incrementable<Real>> T incrementBy(Real increment) {
		return (T)this.plus(increment);
	}

	@Override
	public Real minus(Number n) {
		return this.minus(Real.valueOf(n));
	}

	@Override
	public Real over(Number n) {
		return this.over(Real.valueOf(n));
	}

	@Override
	public Real plus(Number n) {
		return this.plus(Real.valueOf(n));
	}

	@Override
	public Real times(Number n) {
		return this.times(Real.valueOf(n));
	}

	private int compareToSame(BigDecimalReal other){

		BigDecimal denominatorProduct = denominator.multiply(other.numerator);
		BigDecimal numeratorProduct = numerator.multiply(other.denominator);

		return numeratorProduct.compareTo(denominatorProduct);

	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected boolean equalsSame(Real other) {
		return compareToSame((BigDecimalReal)other)==0;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode(){
		return Hash.hash(numerator).hash(denominator).hashCode();
	}

	@Override
	public int compareTo(org.middleheaven.quantity.math.Numeral<? super Real> o) {
		if (BigDecimalReal.class.isInstance(o)){
			return this.compareToSame(BigDecimalReal.class.cast(o));
		}
		return this.asNumber().compareTo(o.asNumber());
	}
	
	@Override
	public int compareTo(Real o) {
		if (BigDecimalReal.class.isInstance(o)){
			return this.compareToSame(BigDecimalReal.class.cast(o));
		}
		return this.asNumber().compareTo(o.asNumber());
	}

	@Override
	public BigInt toBigInt() {
		return BigInt.valueOf(this.asNumber().longValue());
	}

	@Override
	public Complex toComplex() {
		return Complex.rectangular(this, this.getAlgebricStructure().zero());
	}

	@Override
	public Real toReal() {
		return this;
	}



	@Override
	public Real arctan() {
		return new BigDecimalReal(
				BigDecimalMath.arctan(this.asNumber(), SCALE), BigDecimal.ONE);
	}



	@Override
	public Real sqrt() {
		return new BigDecimalReal(
				BigDecimalMath.sqrt(this.asNumber() , SCALE), 
				BigDecimal.ONE
		).simplify();
	}

	@Override
	public Real cos() {
		return new BigDecimalReal(
				BigDecimalMath.cos(this.asNumber(), SCALE), BigDecimal.ONE);
	}


	@Override
	public Real sin() {
		return new BigDecimalReal(
				BigDecimalMath.sin(this.asNumber(), SCALE), BigDecimal.ONE);
	}

	@Override
	public Real exp() {
		return new BigDecimalReal(
				BigDecimalMath.exp(this.asNumber(), SCALE), BigDecimal.ONE);
	}

	@Override
	public Real ln() {
		return new BigDecimalReal(
				BigDecimalMath.ln(this.asNumber(), SCALE), BigDecimal.ONE);
	}


	@Override
	public Real tan() {
		return sin().over(cos());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Real next() {
		return this.plus(valueOf(Integer.valueOf(1)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Real previous() {
		return this.minus(valueOf(Integer.valueOf(1)));
	}

	@Override
	public Real abs() {
		return new BigDecimalReal(this.numerator.abs(), this.denominator.abs());
	}


}
