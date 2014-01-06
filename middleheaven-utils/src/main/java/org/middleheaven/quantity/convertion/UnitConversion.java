package org.middleheaven.quantity.convertion;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.unit.IncompatibleUnitsException;
import org.middleheaven.quantity.unit.Measurable;
import org.middleheaven.quantity.unit.MultipleUnit;
import org.middleheaven.quantity.unit.NonSI;
import org.middleheaven.quantity.unit.SI;
import org.middleheaven.quantity.unit.Unit;
import org.middleheaven.util.coersion.CoersionException;

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
	
	public static <E extends Measurable> DecimalMeasure<E> convert(DecimalMeasure<E> value,Unit<E> to){
		Unit<E> from = value.unit();
		if (from.equals(to)){
			return value;
		}
		
		if (from instanceof MultipleUnit && to instanceof MultipleUnit){

			DecimalMeasure<E> df = ((MultipleUnit)from).reduceToUnit();
			DecimalMeasure<E> dto = ((MultipleUnit)to).reduceToUnit();
			
			final DecimalMeasure<Measurable> result = df.times(dto).inverse().over(value);
			return DecimalMeasure.measure(result.amount(), result.uncertainty(), to);

		} else if (from instanceof MultipleUnit && !(to instanceof MultipleUnit)){
			MultipleUnit mfrom = (MultipleUnit)from;
			if(mfrom.getBaseUnit().equals(to)){
				DecimalMeasure<E> df = ((MultipleUnit)from).reduceToUnit();
				return value.times(df.amount(), df.unit());
			} else {
				throw new CoersionException(new IncompatibleUnitsException(from, to));
			}
			
		} else if (!(from instanceof MultipleUnit) && to instanceof MultipleUnit){
			MultipleUnit mto = (MultipleUnit)to;
			if(mto.getBaseUnit().equals(from)){
				DecimalMeasure<E> dto = ((MultipleUnit)to).reduceToUnit();
				return value.over(dto.amount(), to);
			} else {
				throw new CoersionException(new IncompatibleUnitsException(from, to));
			}
		}else {
			MapKey key = new MapKey(from ,to);
			UnitConverter<E> converter = converters.get(key);
			if (converter.resultUnit().equals(from)){
				converter =  converter.inverse();
			}
			
			return converter.convertFoward(value);
		}
		
		
		
	}
	
	public static class MapKey {

		private  Object a; 
		private  Object b;
		
		public MapKey(Object a, Object b) {
			this.a = a;
			this.b = b;
		}
		
		public int hashCode(){
			return a.hashCode() ^ b.hashCode();
		}
		
		public boolean equals(Object other) {
			return other instanceof MapKey && equalsOther((MapKey) other);
		}

		public boolean equalsOther(MapKey other) {
			 return  (a.equals(other.a) &&  b.equals(other.b)) || (a.equals(other.b) &&  b.equals(other.a));
		}
	}
}
