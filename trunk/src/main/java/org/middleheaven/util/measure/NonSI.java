package org.middleheaven.util.measure;

import org.middleheaven.util.measure.measures.Temperature;

public final class NonSI extends UnitSystem{

	public static final Unit<Temperature> CELSIUS = Unit.unit(Dimension.TEMPERATURE,"�C");
	public static final Unit<Temperature> FAHRENHEIT = Unit.unit(Dimension.TEMPERATURE,"�F");
	public static final Unit<Temperature> RANKINE = Unit.unit(Dimension.TEMPERATURE,"R");
	
	private static NonSI me = new NonSI();
	public static NonSI getInstance(){
		return me;
	}
	

}
