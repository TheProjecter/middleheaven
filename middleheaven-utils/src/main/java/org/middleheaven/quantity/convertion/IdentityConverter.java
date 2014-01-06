package org.middleheaven.quantity.convertion;

import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.unit.Measurable;
import org.middleheaven.quantity.unit.Unit;

class IdentityConverter<E extends Measurable> implements UnitConverter<E> {
	
	private Unit<E> unit;

	public IdentityConverter(Unit<E> unit) {
		super();
		this.unit = unit;
	}
	
	@Override
	public Unit<E> originalUnit() {
		return unit;
	}

	@Override
	public Unit<E> resultUnit() {
		return unit;
	}

	@Override
	public DecimalMeasure<E>  convertFoward(DecimalMeasure<E>  value) {
		return value;
	}

	@Override
	public DecimalMeasure<E>  convertReverse(DecimalMeasure<E>  value) {
		return value;
	}

	@Override
	public UnitConverter<E> inverse() {
		return this;
	}

}
