package org.middleheaven.quantity.convertion;

import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.unit.Measurable;
import org.middleheaven.quantity.unit.Unit;

/**
 * Converts units 
 *
 * @param <E>
 */
public interface UnitConverter<E extends Measurable> {

	/**
	 * Convert from measure given in unit A to measure in unit B
	 * where A.equals(this.originalUnit()) and B.equals(this.resultUnit()) 
	 * @param original
	 * @param <M>
	 * @return
	 */
	public DecimalMeasure<E> convertFoward( DecimalMeasure<E> value);
	public DecimalMeasure<E> convertReverse( DecimalMeasure<E> value);
	
	public UnitConverter<E> inverse();
 
	public Unit<E> originalUnit();

	public Unit<E> resultUnit();
}
