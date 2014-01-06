package org.middleheaven.quantity.convertion;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measurables.Temperature;
import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.unit.NonSI;

public class FahrenheitCelciusConverter extends AbstractUnitConverter<Temperature> {

	protected FahrenheitCelciusConverter() {
		super(NonSI.FAHRENHEIT, NonSI.CELSIUS);
	}


	public DecimalMeasure<Temperature> convertFoward(DecimalMeasure<Temperature>  farhrenheit) {
		if (!farhrenheit.unit().dimension().equals(this.originalUnit.dimension())){
			throw new IllegalArgumentException("Dimention is not Temperature");
		}
		
		final DecimalMeasure<Temperature>  ZERO_CELCIUS =  farhrenheit.one().times(Real.valueOf(32), farhrenheit.unit());
		return farhrenheit.minus(ZERO_CELCIUS).times(Real.valueOf(5),this.resultUnit).over(Real.valueOf(9), this.resultUnit);
	}


	public  DecimalMeasure<Temperature>  convertReverse(DecimalMeasure<Temperature>  celcius) {
		final DecimalMeasure<Temperature>  ZERO_CELCIUS =  celcius.one().times(Real.valueOf(32),this.originalUnit);
		return celcius.times(Real.fraction(9,5),this.originalUnit).plus(ZERO_CELCIUS);
	}

}
