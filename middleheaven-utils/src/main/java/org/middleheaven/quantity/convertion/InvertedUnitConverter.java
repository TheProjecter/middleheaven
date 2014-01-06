package org.middleheaven.quantity.convertion;

import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.unit.Measurable;
import org.middleheaven.quantity.unit.Unit;

public final class InvertedUnitConverter<E extends Measurable> implements UnitConverter<E> {

	private UnitConverter<E> original;
	
	public InvertedUnitConverter(UnitConverter<E> original){
		this.original = original;
	}

	@Override
	public UnitConverter<E> inverse() {
		return original;
	}

	@Override
	public Unit<E> originalUnit() {
		return original.resultUnit();
	}

	@Override
	public Unit<E> resultUnit() {
		return original.originalUnit();
	}


	@Override
	public DecimalMeasure<E> convertFoward(DecimalMeasure<E>  value) {
		return original.convertReverse(value);
	}


	@Override
	public DecimalMeasure<E>  convertReverse(DecimalMeasure<E>  value) {
		return original.convertFoward(value);
	}

	

}
