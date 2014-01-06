package org.middleheaven.quantity.unit;

import java.io.Serializable;

import org.middleheaven.quantity.measurables.Aceleration;
import org.middleheaven.quantity.measurables.Action;
import org.middleheaven.quantity.measurables.Angle;
import org.middleheaven.quantity.measurables.Area;
import org.middleheaven.quantity.measurables.Currency;
import org.middleheaven.quantity.measurables.Density;
import org.middleheaven.quantity.measurables.Dimensionless;
import org.middleheaven.quantity.measurables.Distance;
import org.middleheaven.quantity.measurables.ElectricCarge;
import org.middleheaven.quantity.measurables.ElectricCurrent;
import org.middleheaven.quantity.measurables.Energy;
import org.middleheaven.quantity.measurables.Entropy;
import org.middleheaven.quantity.measurables.Force;
import org.middleheaven.quantity.measurables.Mass;
import org.middleheaven.quantity.measurables.Power;
import org.middleheaven.quantity.measurables.Pressure;
import org.middleheaven.quantity.measurables.Temperature;
import org.middleheaven.quantity.measurables.Time;
import org.middleheaven.quantity.measurables.Velocity;
import org.middleheaven.quantity.measurables.Volume;

public abstract class Dimension<E extends Measurable> implements Serializable {

	// special 
	public static final Dimension<Dimensionless> DIMENTIONLESS = BaseDimension.base('1',0);
	public static final Dimension<Currency> CURRENCY = BaseDimension.base('$');
	// fundamental
	public static final Dimension<Angle> ANGLE = BaseDimension.base('\u0398');
	
	public static final Dimension<Distance> DISTANCE = BaseDimension.base('L');
	public static final Dimension<Time> TIME = BaseDimension.base('T');
	public static final Dimension<Mass> MASS = BaseDimension.base('M');
	public static final Dimension<Temperature> TEMPERATURE = BaseDimension.base('K');
	public static final Dimension<ElectricCarge> ELECTRIC_CARGE = BaseDimension.base('C');
	
	
	// derived
	public static final Dimension<ElectricCurrent> CURRENT_INTENSITY = ELECTRIC_CARGE.over(TIME);
	public static final Dimension<Area> AREA = BaseDimension.base('L',2);
	public static final Dimension<Volume> VOLUME = BaseDimension.base('L',3);
	public static final Dimension<Density> DENSITY = MASS.over(VOLUME);
	public static final Dimension<Velocity> VELOCITY = DISTANCE.over(TIME);
	public static final Dimension<Aceleration> ACELERATION = VELOCITY.over(TIME);
	public static final Dimension<Force> FORCE = ACELERATION.times(MASS);
	public static final Dimension<Pressure> PRESSURE = FORCE.over(AREA);
	public static final Dimension<Energy> ENERGY = FORCE.times(DISTANCE);
	public static final Dimension<Action> ACTION = ENERGY.times(TIME);
	public static final Dimension<Power> POWER = ENERGY.over(TIME);
	public static final Dimension<Entropy> ENTROPY = ENERGY.over(TEMPERATURE);
	
	public Dimension<E> plus(Dimension<E> dimension) throws IncompatibleDimentionException{
		if (dimension.equals(this)){
			return this;
		}
		throw new IncompatibleDimentionException();
	}
	public Dimension<E> minus(Dimension<E> dimension) throws IncompatibleDimentionException{
		if (dimension.equals(this)){
			return this;
		}
		throw new IncompatibleDimentionException();
	}
	
	public abstract <T extends Measurable> Dimension<T> times(Dimension<?> dimension) ;
	
	public abstract <T extends Measurable> Dimension<T> over(Dimension<?> dimension);

	
    protected abstract <T extends Measurable> Dimension<T> simplify();
    

}
