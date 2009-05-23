package org.middleheaven.quantity.convertion;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measurables.Measurable;
import org.middleheaven.quantity.measurables.Volume;
import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.measure.Scalable;
import org.middleheaven.quantity.unit.IncompatibleUnitsException;
import org.middleheaven.quantity.unit.MultipleUnit;
import org.middleheaven.quantity.unit.NonSI;
import org.middleheaven.quantity.unit.SI;
import org.middleheaven.quantity.unit.Unit;
import org.middleheaven.util.conversion.ConvertionException;

@SuppressWarnings("unchecked")
public class UnitConversion {


	
	private static Map<MapKey , UnitConverter> converters = new HashMap<MapKey , UnitConverter>();
	

	static {
	
		addConverter(new AngleConverter());
		addConverter( AditiveConverter.convert(SI.KELVIN, NonSI.CELSIUS,Real.valueOf("273.15")));
		addConverter(new FahrenheitCelciusConverter());
		addConverter( MultipltyConverter.convert(SI.CUBIC_METER, NonSI.LITRE,Real.valueOf("1000"))); // 1m3 = 1000L
		
	}
	

	private static  void addConverter(UnitConverter converter){
		
		converters.put(new MapKey(converter.originalUnit(),converter.resultUnit()), converter);
	}
	
	public static <E extends Measurable,T extends Scalable<E,T>> T convert(T value,Unit<E> to){
		Unit<E> from = value.unit();
		if (from.equals(to)){
			return value;
		}
		
		if (from instanceof MultipleUnit && to instanceof MultipleUnit){

			DecimalMeasure<E> df = ((MultipleUnit)from).reduceToUnit();
			DecimalMeasure<E> dto = ((MultipleUnit)to).reduceToUnit();
			
			return value.times(dto.times(df).amount(), df.unit());
		
		} else if (from instanceof MultipleUnit && !(to instanceof MultipleUnit)){
			MultipleUnit mfrom = (MultipleUnit)from;
			if(mfrom.getBaseUnit().equals(to)){
				DecimalMeasure<E> df = ((MultipleUnit)from).reduceToUnit();
				return value.times(df.amount(), df.unit());
			} else {
				throw new ConvertionException(new IncompatibleUnitsException(from, to));
			}
			
		} else if (!(from instanceof MultipleUnit) && to instanceof MultipleUnit){
			MultipleUnit mto = (MultipleUnit)to;
			if(mto.getBaseUnit().equals(from)){
				DecimalMeasure<E> dto = ((MultipleUnit)to).reduceToUnit();
				return value.over(dto.amount(), to);
			} else {
				throw new ConvertionException(new IncompatibleUnitsException(from, to));
			}
		}else {
			MapKey key = new MapKey(from ,to);
			UnitConverter<E,T> converter = converters.get(key);
			if (converter.resultUnit().equals(from)){
				converter =  converter.inverse();
			}
			
			return converter.convertFoward(value);
		}
		
		
		
	}
	
	public static class MapKey {

		private  Object A; 
		private  Object B;
		
		public MapKey(Object a, Object b) {
			A = a;
			B = b;
		}
		
		public int hashCode(){
			return A.hashCode() ^ B.hashCode();
		}
		
		public boolean equals(Object other) {
			return other instanceof MapKey && equals((MapKey) other);
		}

		public boolean equals(MapKey other) {
			 return  (A.equals(other.A) &&  B.equals(other.B)) || (A.equals(other.B) &&  B.equals(other.A));
		}
	}
}
