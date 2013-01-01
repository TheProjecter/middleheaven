package org.middleheaven.quantity.measure;

import org.middleheaven.quantity.convertion.Convertable;
import org.middleheaven.quantity.convertion.UnitConversion;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.math.RealField;
import org.middleheaven.quantity.math.structure.GroupAdditive;
import org.middleheaven.quantity.unit.IncompatibleUnitsException;
import org.middleheaven.quantity.unit.SI;
import org.middleheaven.quantity.unit.Unit;

/**
 * A real measure. <code>DecimalMeasure</code> is the generic and default form for explictly
 * handle measures. You may find useful to use <code>AngularMeasure</code> for angular measures
 *
 * @param <E>
 * @see {@link org.middleheaven.quantity.measure.AngularMeasure}
 */
public class DecimalMeasure<E extends Measurable> extends Measure<E,Real> 
	implements Comparable<DecimalMeasure<E>>, 
		Convertable<E,DecimalMeasure<E>> ,  
		Amount<DecimalMeasure<E>,E>, Scalable<E,DecimalMeasure<E>> {


	/**
	 * 
	 * @param unit
	 * @return
	 */
	public static <T extends Measurable> DecimalMeasure<T> zero(Unit<T> unit) {
		return exact(RealField.getInstance().zero(), unit);
	}
	
	/**
	 * 
	 * @param unit
	 * @return
	 */
	public static <T extends Measurable> DecimalMeasure<T> one(Unit<T> unit) {
		return exact(RealField.getInstance().zero(), unit);
	}
	
	/**
	 * 
	 * @param value
	 * @param uncertainty
	 * @param unit
	 * @return
	 */
	public static <T extends Measurable> DecimalMeasure<T> measure(java.lang.Number value,java.lang.Number uncertainty, Unit<T> unit){
		return measure( Real.valueOf(value),Real.valueOf(uncertainty), unit);
	}
	
	/**
	 * 
	 * @param value
	 * @param unit
	 * @return
	 */
	public static <T extends Measurable> DecimalMeasure<T> exact(java.lang.Number value, Unit<T> unit){
		return measure( Real.valueOf(value),RealField.getInstance().zero(), unit);
	}
	
	/**
	 * 
	 * @param value
	 * @param uncertainty
	 * @param unit
	 * @return
	 */
	public static <T extends Measurable> DecimalMeasure<T> measure(Real value, Real uncertainty,Unit<T> unit){
		return new DecimalMeasure<T>(value,uncertainty,unit);
	}

	public static <T extends Measurable> DecimalMeasure<T> exact(Real value, Unit<T> unit){
		return new DecimalMeasure<T>(value,value.minus(value),unit);
	}
	
	protected DecimalMeasure(Real amount, Real uncertainty, Unit<E> unit) {
		super(amount, uncertainty, unit);
	}


	public DecimalMeasure<E> negate() {
		return new DecimalMeasure<E>(this.amount.negate(),this.absUncertainty, this.unit);
	}
	
	public DecimalMeasure<E> over(Real other) {
		return new DecimalMeasure<E>(this.amount.over(other), this.absUncertainty.over(other), this.unit);
	}

	public  DecimalMeasure<E> times(Real other) {
		return new DecimalMeasure<E>(this.amount.times(other), this.absUncertainty.times(other), this.unit);
	}

	public DecimalMeasure<E>  one() {
		return exact(RealField.getInstance().one(),this.unit);
	}

	public DecimalMeasure<E>  zero() {
		return exact(RealField.getInstance().zero(),this.unit);
	}
	
	public <T extends Measurable> DecimalMeasure<T> times(DecimalMeasure<?> other) {
		Unit<T> u = this.unit.times(other.unit());
		return new DecimalMeasure<T>(this.amount().times(other.amount()),timesError(other),u);
	}
	
	public <T extends Measurable> DecimalMeasure<T> over(DecimalMeasure<?> other) {
		Unit<T> u = this.unit.over(other.unit());
		return new DecimalMeasure<T>(this.amount().over(other.amount()),overError(other),u);
	}

	public <T extends Measurable > DecimalMeasure<T>  inverse() {
		Unit<T> unit = SI.DIMENTIONLESS.over(this.unit);
		return new DecimalMeasure<T>(this.amount.inverse(), this.absUncertainty, unit);
	}
	
	public DecimalMeasure<E> plus(DecimalMeasure<E> other) throws IncompatibleUnitsException {
		assertCompatible (other);
		DecimalMeasure<E> convertedOther = other.convertTo(this.unit);
		return new DecimalMeasure<E>(convertedOther.amount().plus(this.amount), this.absUncertainty.plus(convertedOther.absUncertainty) , this.unit.plus(convertedOther.unit()));
	}
	
	public DecimalMeasure<E> minus(DecimalMeasure<E> other) throws IncompatibleUnitsException {
		assertCompatible (other);
		return new DecimalMeasure<E>(this.amount.minus(other.amount()),this.absUncertainty.plus(other.absUncertainty),this.unit.minus(other.unit()));
	}

	@Override
	public  int compareTo(DecimalMeasure<E> other) {
		assertCompatible (other);
		return this.amount.compareTo(other.amount);
	}

	@SuppressWarnings("unchecked")
	public <T extends Measurable > DecimalMeasure<T> cast() {
		return (DecimalMeasure<T>) this;
	}

	@Override
	public DecimalMeasure<E> convertTo(Unit<E> newUnit) {
		return UnitConversion.convert(this, newUnit);
	}
	
	@Override
	public DecimalMeasure<E> over(Real other, Unit<E> unit) {
		return new DecimalMeasure<E>(this.amount.over(other), this.absUncertainty.over(other), unit);
	}

	@Override
	public DecimalMeasure<E> times(Real other, Unit<E> unit) {
		return new DecimalMeasure<E>(this.amount.times(other), this.absUncertainty.times(other), unit);
	}

	/**
	 * 
	 * @param unit
	 * @return
	 */
	public <T extends Measurable> DecimalMeasure<T> sqrt(Unit<T> unit) {
		Real r = Real.valueOf(Math.sqrt(this.amount.asNumber().doubleValue()));
		Real u = Real.valueOf(Math.sqrt(this.absUncertainty.asNumber().doubleValue()));
		return new DecimalMeasure<T>(r, u, unit);  
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public GroupAdditive<DecimalMeasure<E>> getAlgebricStructure() {
		return new  GroupAdditive<DecimalMeasure<E>>(){

			@Override
			public boolean isGroupAdditive() {
				return true;
			}

			@Override
			public boolean isRing() {
				return false;
			}

			@Override
			public boolean isField() {
				return false;
			}

			@Override
			public DecimalMeasure<E> zero() {
				return DecimalMeasure.zero(unit);
			}
			
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isZero() {
		return this.equals(this.getAlgebricStructure().zero());
	}


}
