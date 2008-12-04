package org.middleheaven.test.measures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.math.structure.LUDecomposition;
import org.middleheaven.math.structure.Matrix;
import org.middleheaven.math.structure.Vector;
import org.middleheaven.util.measure.AngularMeasure;
import org.middleheaven.util.measure.DecimalMeasure;
import org.middleheaven.util.measure.Dimension;
import org.middleheaven.util.measure.IncompatibleDimentionException;
import org.middleheaven.util.measure.IncompatibleUnitsException;
import org.middleheaven.util.measure.Integer;
import org.middleheaven.util.measure.Real;
import org.middleheaven.util.measure.SI;
import org.middleheaven.util.measure.Unit;
import org.middleheaven.util.measure.measures.Aceleration;
import org.middleheaven.util.measure.measures.Area;
import org.middleheaven.util.measure.measures.Distance;
import org.middleheaven.util.measure.measures.Energy;
import org.middleheaven.util.measure.measures.Force;
import org.middleheaven.util.measure.measures.Mass;
import org.middleheaven.util.measure.measures.Time;
import org.middleheaven.util.measure.measures.Velocity;
import org.middleheaven.util.measure.money.Money;

public class MeasuresTestSuit {

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
		Dimension<Velocity> V = Dimension.LENGTH.over(Dimension.TIME) ;
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
		assertSame(L , Dimension.LENGTH);
		
		try {
			L = L.plus(Dimension.LENGTH);
		} catch (IncompatibleDimentionException e){
			assertFalse (true);
		}

	}	
	
	@Test
	public void testUnits(){
		Unit<Distance> m = Unit.unit( Dimension.LENGTH, "m");
		Unit<Time> s = Unit.unit( Dimension.TIME, "s");
		
		m.plus(m);
		s.minus(s);
		
		Unit<Velocity> v = m.over(s);
		assertEquals("ms^-1" , v.symbol());
		
		
	    //m = v.times(s);
	    assertEquals("m" , m.symbol());
	    
	    // assert v not changed
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
		
		
		Integer cem =  Integer.valueOf(100);
		Integer tres =  Integer.valueOf(3);
		Integer seis =  Integer.valueOf(6);
		Integer tresentos =  Integer.valueOf(300);
		
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

		Money t = Money.money(330, "USD");
		Money c = Money.money(330, "EUR");
		
	    // can only add money of the same currency
		// raise exception
	    t.plus(c); 

	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWrongIsoCode(){

		Money.money(330, "EU"); // eu is not a iso code

	}
	
	@Test
	public void testMoneyOperations (){
		
		Money a = Money.money(100, "USD");
		Money b = Money.money(230, "USD");
		Money t = Money.money(330, "USD");
		
		Money c = Money.money(330, "EUR");
		
		Money m = a.plus(b);

		assertEquals(t, m);
		
		// money are equal if both amount and currency are equal
		assertFalse(t.equals(c));

		// multiply by a real
		Real n = Real.valueOf(3);
		Money y = t.over(n);
		assertEquals (Money.money(110, "USD"), y);
		
		/*
		Scalar L = Scalar.scalar(20, SI.HOUR); 
		Scalar q = a.over(L);
		assertEquals ("5.00 USDh^-1" , q.toString());
		
		Duration h = Duration.hours(2); 
		Scalar total = h.times(q);
		Money ten = Money.money(10, "USD");
		assertEquals ("10.00 USD" , total.toString());
		assertEquals (ten , total);
		*/
		
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