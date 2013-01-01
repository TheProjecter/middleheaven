/**
 * 
 */
package org.middleheaven.quantity.math;

import java.util.Comparator;
import java.util.Random;

import org.middleheaven.quantity.math.structure.MathStructuresFactory;
import org.middleheaven.quantity.math.structure.OrderedField;

/**
 * The Field of Reals
 */
public class BigIntField implements OrderedField<BigInt> {

	
	private static final BigIntField ME = new BigIntField();
	
	public static BigIntField getInstance(){
		return ME;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigInt zero() {
		return MathStructuresFactory.getFactory().numberFor( BigInt.class, "0");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigInt one() {
		return MathStructuresFactory.getFactory().numberFor( BigInt.class , "1");
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
	public BigInt fromNumber(Number n) {
		return BigInt.valueOf(n);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigInt random() {
		return BigInt.valueOf(Math.random());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigInt random(Random random) {
		return BigInt.valueOf(random.nextInt());
	}

	@Override
	public Comparator<BigInt> getComparator() {
		return new Comparator<BigInt>(){

			@Override
			public int compare(BigInt a, BigInt b) {
				return a.compareTo(b);
			}
			
		};
	}


}
