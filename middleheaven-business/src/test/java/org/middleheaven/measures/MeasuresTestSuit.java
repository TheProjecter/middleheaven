package org.middleheaven.measures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.quantity.math.BigInt;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measurables.Aceleration;
import org.middleheaven.quantity.measurables.Area;
import org.middleheaven.quantity.measurables.Distance;
import org.middleheaven.quantity.measurables.Energy;
import org.middleheaven.quantity.measurables.Force;
import org.middleheaven.quantity.measurables.Mass;
import org.middleheaven.quantity.measurables.Time;
import org.middleheaven.quantity.measurables.Velocity;
import org.middleheaven.quantity.measurables.Volume;
import org.middleheaven.quantity.measure.AngularMeasure;
import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.money.CentsMoney;
import org.middleheaven.quantity.unit.Dimension;
import org.middleheaven.quantity.unit.IncompatibleDimentionException;
import org.middleheaven.quantity.unit.IncompatibleUnitsException;
import org.middleheaven.quantity.unit.NonSI;
import org.middleheaven.quantity.unit.SI;
import org.middleheaven.quantity.unit.Unit;

public class MeasuresTestSuit {

	@Test
	public void testUnitOperation(){
		
		SI.METER.plus(SI.METER);
		SI.SECOND.plus(SI.SECOND);
		
	}
	
	@Test(expected=IncompatibleUnitsException.class)
	public void testFailUnitOperation(){
		Unit<Distance> meter = SI.METER;
		Unit<Distance> milimeter = SI.MILI(SI.METER);
		
		meter.plus(milimeter);
	}
	
	@Test
	public void testExactMeasureOperations(){
		
		DecimalMeasure<Distance> threeM = DecimalMeasure.exact(Real.valueOf(3), SI.METER);
		DecimalMeasure<Distance> sixM = DecimalMeasure.exact(Real.valueOf(6), SI.METER);
		
		assertEquals(sixM , threeM.plus(threeM));
		
		DecimalMeasure<Distance> mM = DecimalMeasure.exact(Real.valueOf("0.001"), SI.METER);
		DecimalMeasure<Distance> threemM = DecimalMeasure.exact(Real.valueOf("3.001"), SI.METER);
		
		assertEquals(threemM , threeM.plus(mM));
		
		mM = DecimalMeasure.exact(Real.valueOf("1"), SI.MILI(SI.METER));
		
		assertEquals(threemM , threeM.plus(mM));
		
		// assume the first operand unit
		assertEquals( SI.METER , threeM.plus(mM).unit());
		assertEquals( SI.MILI(SI.METER) , mM.plus(threeM).unit());
		
	}
	
	@Test
	public void testMeasureOperations(){
		DecimalMeasure<Distance> threeM = DecimalMeasure.measure(Real.valueOf(3), Real.fraction(1, 5), SI.METER);
		DecimalMeasure<Distance> sixM = DecimalMeasure.measure(Real.valueOf(6), Real.fraction(1, 4), SI.METER);
		DecimalMeasure<Distance> fiveExactM = DecimalMeasure.exact(Real.valueOf(5), SI.METER);
		
		assertEquals( DecimalMeasure.measure(Real.valueOf(9), Real.fraction(9, 20), SI.METER) , threeM.plus(sixM));
		
		
		assertEquals( DecimalMeasure.measure(Real.valueOf(8), Real.fraction(1,5), SI.METER) , fiveExactM.plus(threeM));
		assertEquals( DecimalMeasure.measure(Real.valueOf(15), Real.valueOf(1), SI.METER.raise(2)) , fiveExactM.times(threeM));
		
		assertEquals( DecimalMeasure.measure(Real.valueOf(4), Real.valueOf(1), SI.METER) , fiveExactM);
		
	}
	
	@Test
	public void testDifferentCompatibleUnits(){
		DecimalMeasure<Volume> litres = DecimalMeasure.exact(Real.valueOf("5"), NonSI.LITRE);
		DecimalMeasure<Volume> resultA = DecimalMeasure.exact(Real.valueOf("1005"), NonSI.LITRE);
		Unit<Volume> d = SI.METER.raise(3);
		DecimalMeasure<Volume> meters = DecimalMeasure.exact(Real.valueOf("1"), d);
		DecimalMeasure<Volume> resultB = DecimalMeasure.exact(Real.valueOf("1.005"), d);
		
		assertEquals(resultA , litres.plus(meters) );
		assertEquals(resultB , meters.plus(litres) );
		
		
	}
	
	@Test
	public void testAngularPosition(){
		
		AngularMeasure ap = AngularMeasure.degrees(180);
		AngularMeasure apc = ap.toRadians();
		
		assertEquals(AngularMeasure.radians(Math.PI), apc );
		
		
		AngularMeasure diff = AngularMeasure.degrees(360*2.25);
		
		assertEquals(AngularMeasure.degrees(270), ap.plus(diff).reduce());
		
		assertEquals(AngularMeasure.degrees(90), ap.plus(diff.negate()).reduce());
	}
	
  
	@Test
	public void testDimentions(){
		

		// create speed
		Dimension<Velocity> V = Dimension.DISTANCE.over(Dimension.TIME) ;
		// assert right dimensions
		assertEquals("LT^-1", V.toString());
		assertEquals(Dimension.VELOCITY, V);
		// create acceleration
		Dimension<Aceleration> A = V.over(Dimension.TIME) ;
		// assert right dimensions
		assertEquals("LT^-2", A.toString());
		
		// create force
		Dimension<Force> F = A.times(Dimension.MASS) ;
		
		assertTrue(F.equals(A.times(Dimension.MASS)));
		assertFalse(F.equals(A));
		
		// get fundamental from calculus
		Dimension<Distance> L = V.times(Dimension.TIME);
		// assert is the same object
		assertSame(L , Dimension.DISTANCE);
		
		try {
			L = L.plus(Dimension.DISTANCE);
		} catch (IncompatibleDimentionException e){
			assertFalse (true);
		}

	}	
	
	@Test
	public void testUnits(){
		Unit<Distance> m = Unit.unit( Dimension.DISTANCE, "m");
		Unit<Time> s = Unit.unit( Dimension.TIME, "s");
		
		m.plus(m);
		s.minus(s);
		
		Unit<Velocity> v = m.over(s);
		
		assertEquals("m" , m.symbol());
			
		assertEquals("ms^-1" , v.symbol());
		
		
	}	
	
	@Test
	public void testMeasures(){
		DecimalMeasure<Distance> L = DecimalMeasure.measure(3, 0.2 , SI.METER);
		DecimalMeasure<Distance> F = DecimalMeasure.measure(5, 0.1 , SI.METER);
		
		DecimalMeasure<Distance> S = F.plus(L);
		assertEquals (DecimalMeasure.measure(8, 0.3, SI.METER) , S);
		
		DecimalMeasure<Area> D = F.times(L);
		assertEquals (DecimalMeasure.measure(15, 1.30, SI.METER.raise(2)) , D);
		
		
		BigInt cem =  BigInt.valueOf(100);
		BigInt tres =  BigInt.valueOf(3);
		BigInt seis =  BigInt.valueOf(6);
		BigInt tresentos =  BigInt.valueOf(300);
		
		assertEquals(seis,tres.plus(tres));
		
		assertEquals(tresentos,cem.times(tres));
		
		DecimalMeasure<Distance> l = DecimalMeasure.exact(200, SI.METER );
		DecimalMeasure<Time> t = DecimalMeasure.exact(10, SI.SECOND);
		DecimalMeasure<Velocity> v = l.over(t);
		
		assertEquals(DecimalMeasure.exact(20,  SI.METER.over(SI.SECOND) ), v);
		assertEquals(Dimension.VELOCITY, v.unit().dimension());
		
		DecimalMeasure<?> v2 = v.times(v);
		Dimension<?> dim =  Dimension.VELOCITY.times(Dimension.VELOCITY);
		assertEquals(dim,v2.unit().dimension());
		DecimalMeasure<Mass> m = DecimalMeasure.exact(50, SI.KILOGRAM );
		DecimalMeasure<Energy> y = m.times(v2);
		DecimalMeasure<Energy> EC = y.times(Real.valueOf(0.5));
		
		assertEquals(Dimension.ENERGY, EC.unit().dimension());
		
	}
	
	@Test(expected=IncompatibleUnitsException.class)
	public void testAdditionMoneyDifferentCurrency(){

		CentsMoney t = CentsMoney.valueOf(330, "USD");
		CentsMoney c = CentsMoney.valueOf(330, "EUR");
		
	    // can only add money of the same currency
		// raise exception
	    t.plus(c); 

	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWrongIsoCode(){

		CentsMoney.valueOf(330, "EU"); // eu is not a iso code

	}
	

	@Test
	public void testDurationAndPeriod(){
		
		/*
		Quantity<TimeInterval> days = Duration.days(30);
		Quantity<TimeInterval> months = Duration.months(1);
		Period p = new Period (2000);
		
		Quantity<TimeInterval> d = days.plus(months);
		
		assertEquals("1 months 30 days", d.toString());
		
		Quantity<TimeInterval> dp = d.minus(p);
		
		assertEquals("1 months 30 days -2000 miliseconds", dp.toString());
		
		*/
	}
	
	
}
