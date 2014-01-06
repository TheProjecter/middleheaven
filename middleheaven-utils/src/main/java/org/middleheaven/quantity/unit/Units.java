package org.middleheaven.quantity.unit;

import java.util.HashMap;
import java.util.Map;


public class Units {


	private static Map<String,Unit> units= new HashMap<String,Unit>(); 
	
	static {
		addUnit(Unit.unit(Dimension.TEMPERATURE , "\u00BAC")); // Celcius
		addUnit(Unit.unit(Dimension.TEMPERATURE , "K")); // Kelvin
		addUnit(Unit.unit(Dimension.TEMPERATURE , "R")); // Rankine
		addUnit(Unit.unit(Dimension.TEMPERATURE , "\u00BAF")); // Fareihight
	}

	private static <E extends Measurable> void addUnit(Unit<E> unit){
		units.put(unit.symbol(), unit);
	}
	
	public static <E extends Measurable> Unit<E> getUnit(String unitSymbol){
		return units.get(unitSymbol.trim());
	}
}
