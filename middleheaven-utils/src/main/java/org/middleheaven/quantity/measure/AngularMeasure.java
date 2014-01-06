package org.middleheaven.quantity.measure;

import java.math.BigDecimal;

import org.middleheaven.collections.interval.Interval;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.math.RealField;
import org.middleheaven.quantity.measurables.Angle;
import org.middleheaven.quantity.unit.IncompatibleUnitsException;
import org.middleheaven.quantity.unit.SI;
import org.middleheaven.quantity.unit.Unit;

/**
 * Angular position in radians
 * 
 * 
 */
public class AngularMeasure extends DecimalMeasure<Angle>   {


	public static AngularMeasure fullCircle(){
		return radians(2*Math.PI);
	}
	
	public static AngularMeasure halfCircle(){
		return radians(Math.PI);
	}
	
	
	/**
	 * 
	 * @param value a decimal value for the tangent
	 * @return the correspondent angular value 
	 */
	public static AngularMeasure arctan(DecimalMeasure<?> value){
		return AngularMeasure.radians(value.amount.arctan());
	}

	/**
	 * @param amount angle in radians
	 * @return <code>AngularMeasure</code> with value equal to amount in radians
	 */
	public static AngularMeasure radians(double amount){
		return new AngularMeasure(
				Real.valueOf(amount),
				RealField.getInstance().zero(),
				SI.RADIANS
		);
	}

	/**
	 * @param amount angle in degrees
	 * @return <code>AngularMeasure</code> with value equal to amount in degrees
	 */
	public static AngularMeasure degrees(double amount){
		return new AngularMeasure(
				Real.valueOf(amount),
				RealField.getInstance().zero(),
				SI.DEGREE
		);
	}

	/**
	 * Creates a <code>AngularMeasure</code> from a degrees amount expressed in dd mm'ss'' notation
	 *
	 * @param degree integer degrees part of angle
	 * @param minutes minute part of angle in degrees
	 * @param seconds seconds part of angle in degrees
	 * @return
	 */
	public static AngularMeasure degrees(int degree, int minutes, double seconds) {
		double amount = degree + minutes / 60d + seconds/3600d;
		return new AngularMeasure(
				Real.valueOf(amount),
				RealField.getInstance().zero(),
				SI.DEGREE
		);
	}
	
	/**
	 * @param amount angle in radians
	 * @return <code>AngularMeasure</code> with value equal to amount in radians
	 */
	public static AngularMeasure radians(Real amount){
		return new AngularMeasure(amount, amount.getAlgebricStructure().zero(),SI.RADIANS);
	}

	/**
	 * @param amount angle in degrees
	 * @return <code>AngularMeasure</code> with value equal to amount in degrees
	 */
	public static AngularMeasure degrees(Real amount){
		// convert to radians
		return new AngularMeasure(
				Real.valueOf(amount.asNumber().doubleValue()),
				RealField.getInstance().zero(),
				SI.DEGREE
		);
	}

	protected AngularMeasure(Real amount, Real uncertainty, Unit<Angle> unit) {
		super(amount, uncertainty, unit);
	}

	public AngularMeasure negate() {
		return new AngularMeasure (this.amount.negate(),this.absUncertainty, this.unit);
	}


	public AngularMeasure  over(Real other) {
		return new AngularMeasure (this.amount.over(other), this.absUncertainty.over(other), this.unit);
	}

	public AngularMeasure  times(Real other) {
		return new AngularMeasure (this.amount.times(other), this.absUncertainty.times(other), this.unit);
	}

	public AngularMeasure   one() {
		return new AngularMeasure (RealField.getInstance().one(), RealField.getInstance().zero(), this.unit);
	}

	public AngularMeasure   zero() {
		return new AngularMeasure (RealField.getInstance().zero(), RealField.getInstance().zero(), this.unit);
	}

	public AngularMeasure  plus(AngularMeasure  other) throws IncompatibleUnitsException {
		assertCompatible (other);
		return new AngularMeasure (other.amount().plus(this.amount), this.absUncertainty.plus(other.absUncertainty) , this.unit.plus(other.unit()));
	}

	public AngularMeasure  minus(AngularMeasure  other) throws IncompatibleUnitsException {
		assertCompatible (other);
		return new AngularMeasure (other.amount().minus(this.amount),this.absUncertainty.plus(other.absUncertainty),this.unit.minus(other.unit()));
	}

	public AngularMeasure toRadians(){
		return (AngularMeasure)super.convertTo(SI.RADIANS);
	}

	public AngularMeasure toDegrees(){
		return (AngularMeasure)super.convertTo(SI.DEGREE);
	}

	public Real sin(){
		return this.amount.sin();
	}

	public Real cos(){
		return this.amount.cos();
	}

	public Real tan(){
		return this.amount.tan();
	}

	public int compareTo(AngularMeasure other) {
		assertCompatible (other);
		return this.amount.compareTo(other.amount);
	}

	/**
	 * 
	 * @return returns an angular position in [0 , 2pi[ 
	 */

	public AngularMeasure reduce(){


		Interval<Real> range ;

		if (this.unit.equals(SI.RADIANS)){
			range = Interval.between(RealField.getInstance().zero(), Real.valueOf(Math.PI*2));
		} else { 
			range = Interval.between(RealField.getInstance().zero(), Real.valueOf(360));
		}

		BigDecimal top = range.end().asNumber();
		
		if (this.amount.compareTo(range.start())<0){
			// under range
			BigDecimal cycles = this.amount.asNumber().abs().divideToIntegralValue(top);
			
			cycles = cycles.add(BigDecimal.ONE);

			return new AngularMeasure(Real.valueOf(amount.plus(Real.valueOf(cycles.multiply(top)))) , this.absUncertainty , this.unit);

		} else if (this.amount.compareTo(range.end())>=0){
			// out of range
			BigDecimal cycles = this.amount.asNumber().divideToIntegralValue(top);

			return new AngularMeasure(Real.valueOf(amount.minus(Real.valueOf(cycles.multiply(top)))) , this.absUncertainty , this.unit);

		} else {
			return this;
		}


	}

	@Override
	public AngularMeasure over(Real other, Unit<Angle> unit) {
		return new AngularMeasure(this.amount.over(other), this.absUncertainty.over(other), unit);
	}

	@Override
	public AngularMeasure times(Real other, Unit<Angle> unit) {
		return new AngularMeasure(this.amount.times(other), this.absUncertainty.times(other), unit);
	}




}
