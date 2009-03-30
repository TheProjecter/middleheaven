package org.middleheaven.quantity.math;

import org.middleheaven.quantity.math.structure.MathStructuresFactory;
import org.middleheaven.util.Incrementable;
import org.middleheaven.util.Range;


/**
 * Represents an element of |R  (the real numbers set) 
 * 
 */
public abstract class Real extends Number<Real> implements  Comparable<Number<? super Real>> ,Incrementable <Real>{

	public static Real fraction (int num , int den){
		return valueOf(num).over(valueOf(den));
	}
	
	public static Real[] valueOf(java.lang.Number ... array){
		MathStructuresFactory factory = MathStructuresFactory.getFactory();
		Real[] res = new Real[array.length];
		for (int i =0 ; i < array.length; i++){
			res[i] = factory.numberFor(Real.class, array[i].toString());
		}
		return res;
	}
	

	public static Real ONE(){
		return MathStructuresFactory.getFactory().numberFor( Real.class , "1");
	}

	public static Real ZERO(){
		return MathStructuresFactory.getFactory().numberFor( Real.class, "0");
	}
	
	public static Real valueOf (String value) {
		return MathStructuresFactory.getFactory().numberFor( Real.class, value);
	}

	public static Real valueOf (Number<?> other) {
		if (Real.class.isInstance(other)){
			return Real.class.cast(other);
		} 
		return MathStructuresFactory.getFactory().numberFor(Real.class, other.toString());
	}

	public static Real valueOf (java.lang.Number other) {
		return MathStructuresFactory.getFactory().numberFor(Real.class,other.toString());
	}

	public static Real valueOf (double other) {
		return MathStructuresFactory.getFactory().numberFor( Real.class, Double.toString(other));
	}

	@Override
	public Number<Real> promote(Number<?> other) {
		return MathStructuresFactory.getFactory().promote(other, Real.class); 
	}

	protected final int rank(){
		return 1;
	}
	
	public Range<Real> upTo(Real other){
		return Range.over(this, other, other.over(other));
	}
	
	public Range<Real> upTo(Real other, Real increment){
		return Range.over(this, other, increment);
	}

	/**
	 * 
	 * @return real square root
	 */
	public abstract Real sqrt();
	

}
