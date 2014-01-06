package org.middleheaven.quantity.convertion;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measurables.Angle;
import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.unit.SI;

public class AngleConverter extends AbstractUnitConverter<Angle> {

	protected AngleConverter() {
		super(SI.RADIANS, SI.DEGREE);
	}


	public DecimalMeasure<Angle> convertFoward(DecimalMeasure<Angle> radians) {
		return radians.times(Real.valueOf(180),this.resultUnit).over(Real.valueOf(Math.PI), this.resultUnit);
	}


	public DecimalMeasure<Angle> convertReverse(DecimalMeasure<Angle> degree) {
		return degree.times(Real.valueOf(Math.PI),this.originalUnit).over(Real.valueOf(180), this.originalUnit);
	}





}
