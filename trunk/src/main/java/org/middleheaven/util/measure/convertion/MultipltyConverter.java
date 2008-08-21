package org.middleheaven.util.measure.convertion;

import org.middleheaven.util.measure.Real;
import org.middleheaven.util.measure.Scalable;
import org.middleheaven.util.measure.Unit;
import org.middleheaven.util.measure.measures.Measurable;

public final class MultipltyConverter<E extends Measurable> extends AbstractUnitConverter<E>{

	private Real factor;
	
	public static <T,E extends Measurable> MultipltyConverter<E> convert(Unit<E> originalUnit, Unit<E> resultUnit,Real factor){
		return new MultipltyConverter<E>(originalUnit, resultUnit, factor);
	}

	private MultipltyConverter(Unit<E> originalUnit, Unit<E> resultUnit,Real factor) {
		super(originalUnit, resultUnit);
		this.factor = factor;
	}
	
	@Override
	public <T extends Scalable<E, T>> T convertFoward(T value) {
		if (!value.unit().equals(this.originalUnit)){
			throw new IllegalArgumentException("Expected unit " + this.originalUnit + " but was " + value.unit());
		}
		return value.times(factor,this.resultUnit);
	}

	@Override
	public <T extends Scalable<E, T>> T convertReverse(T value) {
		if (!value.unit().equals(this.resultUnit)){
			throw new IllegalArgumentException("Expected unit " + this.originalUnit + " but was " + value.unit());
		}
		return value.over(factor,this.originalUnit);
	}
	



}
