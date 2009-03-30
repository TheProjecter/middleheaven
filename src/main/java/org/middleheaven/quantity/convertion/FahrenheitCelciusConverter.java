package org.middleheaven.quantity.convertion;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measurables.Temperature;
import org.middleheaven.quantity.measure.Scalable;
import org.middleheaven.quantity.unit.NonSI;

public class FahrenheitCelciusConverter<T extends Scalable<Temperature,T>> extends AbstractUnitConverter<Temperature,T> {

	protected FahrenheitCelciusConverter() {
		super(NonSI.FAHRENHEIT, NonSI.CELSIUS);
	}


	public T convertFoward(T farhrenheit) {
		final T ZERO_CELCIUS =  farhrenheit.one().times(Real.valueOf(32), farhrenheit.unit());
		return farhrenheit.minus(ZERO_CELCIUS).times(Real.valueOf(5),this.resultUnit).over(Real.valueOf(9), this.resultUnit);
	}


	public  T convertReverse(T celcius) {
		final T ZERO_CELCIUS =  celcius.one().times(Real.valueOf(32),this.originalUnit);
		return celcius.times(Real.fraction(9,5),this.originalUnit).plus(ZERO_CELCIUS);
	}

}
