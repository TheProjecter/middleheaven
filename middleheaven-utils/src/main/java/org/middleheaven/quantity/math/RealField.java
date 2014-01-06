/**
 * 
 */
package org.middleheaven.quantity.math;

import java.util.Comparator;
import java.util.Random;

import org.middleheaven.collections.ComparableComparator;
import org.middleheaven.quantity.math.structure.MathStructuresFactory;
import org.middleheaven.quantity.math.structure.OrderedField;

/**
 * The Field of Reals
 */
public class RealField implements OrderedField<Real> {

	private static final RealField ME = new RealField();

	public static RealField getInstance(){
		return ME;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Real zero() {
		return MathStructuresFactory.getFactory().numberFor( Real.class, "0");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Real one() {
		return MathStructuresFactory.getFactory().numberFor( Real.class , "1");
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
	 */
	@Override
	public Real fromNumber(Number n) {
		return Real.valueOf(n);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Real random() {
		return Real.valueOf(Math.random());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Real random(Random random) {
		return Real.valueOf(random.nextDouble());
	}

	@Override
	public Comparator<Real> getComparator() {
		return ComparableComparator.<Real>getInstance();
	}
	
	
}
