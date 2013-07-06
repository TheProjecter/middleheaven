package org.middleheaven.quantity.math;

import org.middleheaven.collections.Range;
import org.middleheaven.quantity.math.structure.MathStructuresFactory;
import org.middleheaven.quantity.math.structure.OrderedField;
import org.middleheaven.quantity.math.structure.OrderedFieldElement;
import org.middleheaven.util.Incrementable;
import org.middleheaven.util.NaturalIncrementable;


/**
 * Represents an integer number that ranges from Long.MIN_VALUE to Long.MAX_VALUE.
 * 
 *
 */
public abstract class BigInt extends Numeral<BigInt> implements OrderedFieldElement<BigInt>, Comparable<Numeral<? super BigInt>>, DiscreetOrderable<BigInt> ,NaturalIncrementable<BigInt> , Incrementable <BigInt>{


	private static final long serialVersionUID = 8636156681654308959L;

	public static BigInt valueOf (String value) {
		try{
		return MathStructuresFactory.getFactory().numberFor(BigInt.class , value);
		}catch (NumberFormatException e){
			throw new NumberFormatException(value + " is not an integer");
		}
	}
	
	public static BigInt valueOf (Numeral<?> other) {
		if (BigInt.class.isInstance(other)){
			return BigInt.class.cast(other);
		} 
		return valueOf(other.asNumber().longValue());
	}
	
	public static BigInt valueOf (Number other) {
		return valueOf(other.longValue());
	}
	
	public static BigInt valueOf (long other) {
		return (BigInt)MathStructuresFactory.getFactory().numberFor(BigInt.class, Long.toString(other));
	}
   
	protected final int rank(){
		return 0;
	}
	
	public final boolean isOdd(){
		return !isEven();
	}
	
	public abstract boolean isEven();
	
	@Override
	public Numeral<BigInt> promote(Numeral<?> other) {
		return valueOf(other);
	}
	
	public Range<BigInt, BigInt> upTo(BigInt other){
		return Range.<BigInt, BigInt>from(this).upTo(other);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderedField<BigInt> getAlgebricStructure() {
		return BigIntField.getInstance();
	}
}
