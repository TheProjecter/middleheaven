package org.middleheaven.quantity.math;

import org.middleheaven.quantity.math.structure.MathStructuresFactory;
import org.middleheaven.quantity.math.structure.OrderedField;
import org.middleheaven.quantity.math.structure.OrderedFieldElement;
import org.middleheaven.util.Incrementable;
import org.middleheaven.util.NaturalIncrementable;
import org.middleheaven.util.collections.Range;


/**
 * Represents an element of |R  (the real numbers set) 
 * 
 */
public abstract class Real extends Numeral<Real> implements OrderedFieldElement<Real>, Comparable<Numeral<? super Real>> ,Incrementable <Real>, NaturalIncrementable<Real>{

	private static final long serialVersionUID = 5613604361361447882L;

	/**
	 * Returns a real defined by a fraction of to integers.
	 * @param num
	 * @param den
	 * @return
	 */
	public static Real fraction (int num , int den){
		return valueOf(num).over(valueOf(den));
	}
	
	/**
	 * Returns an array of {@link Real}s based on an array of {@link Number}.
	 * @param array
	 * @return
	 */
	public static Real[] valueOf(java.lang.Number ... array){
		MathStructuresFactory factory = MathStructuresFactory.getFactory();
		Real[] res = new Real[array.length];
		for (int i =0 ; i < array.length; i++){
			res[i] = factory.numberFor(Real.class, array[i].toString());
		}
		return res;
	}

	/**
	 * Returns a Real based on a non-formated string.
	 * @param value the value.
	 * @return the corresponding {@link Real}.
	 */
	public static Real valueOf (String value) {
		return MathStructuresFactory.getFactory().numberFor( Real.class, value);
	}

	/**
	 * Returns a Real based on a {@link Numeral}.
	 * @param value the value.
	 * @return the corresponding {@link Real}.
	 */
	public static Real valueOf (Numeral<?> other) {
		if (Real.class.isInstance(other)){
			return Real.class.cast(other);
		} 
		return MathStructuresFactory.getFactory().numberFor(Real.class, other.toString());
	}

	/**
	 * Obtains a {@link Real} than best represents the ratio between the <code>numerator</code> and <code>denominator</code>.
	 * 
	 * @param numerator
	 * @param denominator
	 * @return a {@link Real} than best represents the ratio between the <code>numerator</code> and <code>denominator</code>
	 */
	public static Real valueOf (Numeral<?> numerator , Numeral<?> denominator) {
		return MathStructuresFactory.getFactory().numberFor(Real.class, valueOf(numerator), valueOf(denominator));
	}
	
	/**
	 * Returns a Real based on a {@link Number}.
	 * @param value the value.
	 * @return the corresponding {@link Real}.
	 */
	public static Real valueOf (java.lang.Number other) {
		return MathStructuresFactory.getFactory().numberFor(Real.class,other.toString());
	}
	
	/**
	 * Obtains a {@link Real} than best represents the ratio between the <code>numerator</code> and <code>denominator</code>.
	 * 
	 * @param numerator
	 * @param denominator
	 * @return a {@link Real} than best represents the ratio between the <code>numerator</code> and <code>denominator</code>
	 */
	public static Real valueOf (java.lang.Number numerator , java.lang.Number denominator) {
		return MathStructuresFactory.getFactory().numberFor(Real.class, valueOf(numerator), valueOf(denominator));
	}
	
	@Override
	public Numeral<Real> promote(Numeral<?> other) {
		return MathStructuresFactory.getFactory().promote(other, Real.class); 
	}

	protected final int rank(){
		return 1;
	}
	
	public Range<Real, Real> upTo(double other){
		return  upTo(Real.valueOf(other));
	}
	
	public Range<Real, Real> upTo(Real other){
		return upTo(other, this.getAlgebricStructure().one());
	}
	
	public Range<Real, Real> upTo(Real other, Real increment){
		return Range.<Real, Real>from(this).by(increment).upTo(other);
	}

	/**
	 * 
	 * @return real square root
	 */
	public abstract Real sqrt();
	
	/**
	 * 
	 * @return real arctang 
	 */
	public abstract Real arctan();
	
	/**
	 * 
	 * @return the sine of this;
	 */
	public abstract Real sin();

	/**
	 * 
	 * @return the cosine of this;
	 */
	public abstract Real cos();
	
	/**
	 * 
	 * @return the tanget of this;
	 */
	public abstract Real tan();

	/**
	 * 
	 * @return the number E raised to the power of this. Equivalent to Math.exp(this) if this was a double.
	 */
	public abstract Real exp();
	
	/**
	 * 
	 * @return the logarithm of this.
	 */
	public abstract Real ln();

	/**
	 * Returns the exponent power od this.
	 * @param exponent
	 * @return this value raised to the powr of the exponent.
	 */
	public Real raise(int exponent) {
		if (exponent==0){
			return this.getAlgebricStructure().one();
		} else if (exponent>0){
			 Real a = this;
			 Real s = this;
			 for (int i = 1 ; i < exponent ; i++){
				 s = s.times(a);
			 }
			 return s;
		} else {
			 Real a = this;
			 Real s = this;
			 for (int i = 0 ; i <= -exponent ; i++){
				 s = s.over(a);
			 }
			 return s;
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderedField<Real> getAlgebricStructure() {
		return RealField.getInstance();
	}
	
	public abstract boolean isInteger();
}
