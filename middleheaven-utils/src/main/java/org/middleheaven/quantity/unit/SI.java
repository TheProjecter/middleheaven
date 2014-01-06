package org.middleheaven.quantity.unit;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measurables.Angle;
import org.middleheaven.quantity.measurables.Area;
import org.middleheaven.quantity.measurables.Dimensionless;
import org.middleheaven.quantity.measurables.Distance;
import org.middleheaven.quantity.measurables.ElectricCurrent;
import org.middleheaven.quantity.measurables.Force;
import org.middleheaven.quantity.measurables.Mass;
import org.middleheaven.quantity.measurables.Temperature;
import org.middleheaven.quantity.measurables.Time;
import org.middleheaven.quantity.measurables.Volume;

/**
 * The International Standard Unit System.
 * 
 *
 * <a href="http://physics.nist.gov/cuu/Units/">http://physics.nist.gov/cuu/Units/</a>
 */
public final class SI extends UnitSystem{

	// Basic
	public static final Unit<Dimensionless> DIMENTIONLESS = Unit.unit(Dimension.DIMENTIONLESS,"");
	public static final Unit<Distance> METER = Unit.unit(Dimension.DISTANCE,"m");
	
	public static final Unit<Time> SECOND = Unit.unit(Dimension.TIME,"s");
	public static final Unit<Mass> KILOGRAM = Unit.unit(Dimension.MASS,"kg");
	
	public static final Unit<Force> NEWTON = Unit.unit(Dimension.FORCE,"N");
	public static final Unit<ElectricCurrent> AMPERE = Unit.unit(Dimension.CURRENT_INTENSITY,"A");
	
	public static final Unit<Angle> RADIANS = Unit.unit(Dimension.ANGLE,"rad");
	
	public static final Unit<Temperature> KELVIN = Unit.unit(Dimension.TEMPERATURE,"K");
	
	// common derivations
	public static final Unit<Volume> CUBIC_METER = SI.METER.raise(3);
	public static final Unit<Area> SQUARE_METER = SI.METER.raise(2);

	public static final Unit<Time> HOUR = Unit.unit(Dimension.TIME,"h");
	
	public static final Unit<Angle> DEGREE = Unit.unit(Dimension.ANGLE,"\u00BA");
	
	public static <E extends Measurable> Unit<E> MEGA(Unit<E> other){
		return MultipleUnit.multipleOf(other,Real.valueOf(1000000));
	}
	
	public static <E extends Measurable> Unit<E> KILO(Unit<E> other){
		return MultipleUnit.multipleOf(other,Real.valueOf(1000));
	}
	
	public static <E extends Measurable> Unit<E> MILI(Unit<E> other){
		return MultipleUnit.multipleOf(other,Real.valueOf("0.001"));
	}
	
	public static <E extends Measurable> Unit<E> MICRO(Unit<E> other){
		return MultipleUnit.multipleOf(other,Real.valueOf("0.000001"));
	}
	
	public static <E extends Measurable> Unit<E> NANO(Unit<E> other){
		return MultipleUnit.multipleOf(other,Real.valueOf("0.000000001"));
	}
	
	public static <E extends Measurable> Unit<E> FENTO(Unit<E> other){
		return MultipleUnit.multipleOf(other,Real.valueOf("0.000000000001"));
	}
	
	static {
		ALL_UNITS.put(Time.class.getName(), SECOND);
		ALL_UNITS.put(Distance.class.getName(), METER);
		ALL_UNITS.put(Area.class.getName(), SQUARE_METER);
		ALL_UNITS.put(Volume.class.getName(), CUBIC_METER);
		ALL_UNITS.put(Dimensionless.class.getName(), DIMENTIONLESS);
		// TODO complete
	}
	
	private static SI me = new SI();
	public static SI getInstance(){
		return me;
	}
	
	private SI(){}
	




}
