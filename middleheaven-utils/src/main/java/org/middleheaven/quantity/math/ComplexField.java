/**
 * 
 */
package org.middleheaven.quantity.math;

import java.util.Random;

import org.middleheaven.quantity.math.structure.Field;
import org.middleheaven.quantity.math.structure.MathStructuresFactory;

/**
 * The Field of Complexes.
 */
public class ComplexField implements Field<Complex> {

	
	private static final ComplexField ME = new ComplexField();
	
	public static ComplexField getInstance(){
		return ME;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Complex zero() {
		return MathStructuresFactory.getFactory().numberFor( Complex.class, "0");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Complex one() {
		return MathStructuresFactory.getFactory().numberFor( Complex.class , "1");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isGroupAdditive() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRing() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isField() {
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * Retruns a pure real complex number.
	 */
	@Override
	public Complex fromNumber(Number n) {
		return Complex.real(n);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Complex random() {
		return Complex.rectangular(Math.random(), Math.random());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Complex random(Random random) {
		return Complex.rectangular(random.nextDouble(), random.nextDouble());
	}
}
