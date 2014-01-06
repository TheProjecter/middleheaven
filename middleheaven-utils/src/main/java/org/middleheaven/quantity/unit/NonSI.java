package org.middleheaven.quantity.unit;

import org.middleheaven.quantity.measurables.Temperature;
import org.middleheaven.quantity.measurables.Volume;


public final class NonSI extends UnitSystem{

	public static final Unit<Temperature> CELSIUS = Unit.unit(Dimension.TEMPERATURE,"\u00BAC");
	public static final Unit<Temperature> FAHRENHEIT = Unit.unit(Dimension.TEMPERATURE,"\u00BAF");
	public static final Unit<Temperature> RANKINE = Unit.unit(Dimension.TEMPERATURE,"R");
	public static final Unit<Volume> LITRE = Unit.unit(Dimension.VOLUME,"L");
	
	private static NonSI me = new NonSI();
	public static NonSI getInstance(){
		return me;
	}
	

}
