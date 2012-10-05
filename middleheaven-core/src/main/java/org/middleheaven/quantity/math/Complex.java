package org.middleheaven.quantity.math;

import org.middleheaven.quantity.math.structure.Field;
import org.middleheaven.quantity.math.structure.MathStructuresFactory;
import org.middleheaven.quantity.measure.AngularMeasure;


public abstract class Complex extends Numeral<Complex> implements Field<Complex> ,Conjugatable<Complex> {

	private static final long serialVersionUID = 5580549238295872790L;
	
	public static Complex polar(Real magnitude, AngularMeasure phase) {
		
		Real real = magnitude.times(phase.cos());
		Real imaginary = magnitude.times(phase.sin());
		return (Complex)MathStructuresFactory.getFactory().numberFor(Complex.class , real ,  imaginary);
	}
	
	public static Complex valueOf(Real real, Real imaginary) {
		return (Complex)MathStructuresFactory.getFactory().numberFor(Complex.class , real ,  imaginary);
	}
	
	public static Complex valueOf(java.lang.Number real, java.lang.Number imaginary) {
		return (Complex)MathStructuresFactory.getFactory().numberFor(Complex.class ,real.toString() + "+i" + imaginary.toString());
	}
	
	public static Complex real(java.lang.Number real) {
		return valueOf(real, 0d);
	}
	
	public static Complex imaginary(java.lang.Number imaginary) {
		return valueOf(0d, imaginary);
	}
	
	public static Complex valueOf(String value) {
		return (Complex)MathStructuresFactory.getFactory().numberFor(Complex.class ,value);
	}
	
	public static Complex ONE(){
		return MathStructuresFactory.getFactory().numberFor(Complex.class , "1+i0");
	}

	public static Complex ZERO(){
		return MathStructuresFactory.getFactory().numberFor(Complex.class , "0+i0");
	}
	
	public static Complex I(){
		return MathStructuresFactory.getFactory().numberFor(Complex.class , "0+i1");
	}
	
	@Override
	public Numeral<Complex> promote(Numeral<?> other) {
		return MathStructuresFactory.getFactory().promote(other, Complex.class); 
	}
	
	/**
	 * @return this complex number conjugate. The conjugate of c is denotes c* and is calculated 
	 * from c (c= a + ib) as c* = a - ib
	 */
	public abstract Complex conjugate();
	
	/**
	 * 
	 * @return the real part of this complex
	 */
	public abstract Real toReal();
	
	/**
	 * 
	 * @return the imaginary part of this complex as a Real number
	 */
	public abstract Real toImaginary();
	
	/**
	 * Returns the magnitude of this complex. The magnitude of c= a +ib is 
	 * |c| = a<sup>2</sup> + b<sup>2</sup> 
	 * @return the magnitude of this complex. 
	 */
	public abstract Real magnitude();
	
	/**
	 * 
	 * @return the imaginary unit. The imaginary unit can be writen as a complex number 
	 * with no real part ( i = 0 + i.1)
	 */
	public abstract Complex i();
	



}
