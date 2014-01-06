package org.middleheaven.quantity.convertion;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.unit.Measurable;
import org.middleheaven.quantity.unit.Unit;

public final class MultipltyConverter<E extends Measurable> extends AbstractUnitConverter<E>{


	private Real factor;
	
	@SuppressWarnings("unchecked")
	public static <E extends Measurable> MultipltyConverter<E> convert(Unit<E> originalUnit, Unit<E> resultUnit,Real factor){
		return new MultipltyConverter(originalUnit, resultUnit, factor);
	}

	private MultipltyConverter(Unit<E> originalUnit, Unit<E> resultUnit,Real factor) {
		super(originalUnit, resultUnit);
		this.factor = factor;
	}
	
	@Override
	public DecimalMeasure<E>  convertFoward(DecimalMeasure<E>  value) {
		if (!value.unit().equals(this.originalUnit)){
			throw new IllegalArgumentException("Expected unit " + this.originalUnit + " but was " + value.unit());
		}
		return value.times(factor,this.resultUnit);
	}

	@Override
	public DecimalMeasure<E>  convertReverse(DecimalMeasure<E>  value) {
		if (!value.unit().equals(this.resultUnit)){
			throw new IllegalArgumentException("Expected unit " + this.originalUnit + " but was " + value.unit());
		}
		return value.over(factor,this.originalUnit);
	}
	



}
