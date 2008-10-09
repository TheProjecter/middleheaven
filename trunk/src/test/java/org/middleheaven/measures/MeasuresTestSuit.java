package org.middleheaven.measures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.util.measure.DecimalMeasure;
import org.middleheaven.util.measure.Dimension;
import org.middleheaven.util.measure.IncompatibleDimentionException;
import org.middleheaven.util.measure.IncompatibleUnitsException;
import org.middleheaven.util.measure.Integer;
import org.middleheaven.util.measure.Real;
import org.middleheaven.util.measure.SI;
import org.middleheaven.util.measure.Unit;
import org.middleheaven.util.measure.money.Money;
import org.middleheaven.util.measure.structure.LUDecomposition;
import org.middleheaven.util.measure.structure.Matrix;
import org.middleheaven.util.measure.structure.Vector;

public class MeasuresTestSuit {

    //@Test 
    public void matrixLU(){
    	Matrix<Real> A = Matrix.matrix(3,3, Real.valueOf(
    			6 , -2 , 0, 
    			9 ,-1 ,1, 
    			3, 7, 5
    			));
		
    	Matrix<Real> U = Matrix.matrix(3,3, Real.valueOf(
    			1, 0.3 , 0, 
    			0 ,1 ,0.5, 
    			0, 0, 1
    			));
		
    	
    	Matrix<Real> L = Matrix.matrix(3,3, Real.valueOf(
    			6 , 0 , 0, 
    			9 ,2 , 0, 
    			3, -8, 1
    			));
		
    	LUDecomposition<Real> lud = new LUDecomposition<Real>(A);
    	
    	assertEquals(L , lud.getL());
    	assertEquals(U , lud.getU());
    	
    	
    	Matrix<Real> N = Matrix.matrix(3,3, Real.valueOf(1 , 1 , 2, 1 ,2 ,1,2, 1, 1));
		
    	lud = new LUDecomposition<Real>(N);
    	
    	assertEquals(N , lud.getL().times(lud.getU()));
    }
    
	@Test
	public void matrix(){

		Vector<Real> v1 = Vector.vector(1,1,2);
		Vector<Real> v2 = Vector.vector(1,2,1);
		Vector<Real> v3 = Vector.vector(2,1,1);
		
		Matrix<Real> M = Matrix.matrix(v1,v2,v3);
		
		Vector<Real> v4 = Vector.vector(2,2,4);
		Vector<Real> v5 = Vector.vector(2,4,2);
		Vector<Real> v6 = Vector.vector(4,2,2);
		
		Matrix<Real> N = Matrix.matrix(3,3, Real.valueOf(2 , 2 , 4, 2 ,4 ,2,4, 2, 2));
		
	
		Real det = M.determinant();
		
		assertEquals(Real.valueOf(-4),det);
		
		assertEquals(Real.valueOf(4),M.trace());
		
		assertEquals(M , M.transpose());
		
		assertEquals(N , M.times(Real.valueOf(2.0)));
		
		assertEquals(N , M.plus(M));
		
		Vector<Real> v7 = Vector.vector(12,10,10);
		assertEquals(v7, M.times(v4));
		
		Matrix<Real> P = Matrix.matrix(3,3, Real.valueOf(1 , 1 , 2, 1 ,2 ,1, 2, 1, 1));
		assertEquals(M, P);
		
		Matrix<Real> Q = Matrix.matrix(3,3, Real.valueOf(12 , 10 , 10, 10 ,12 ,10, 10, 10, 12));
		assertEquals(Q, M.times(N));
	
		Matrix<Real> A = Matrix.matrix(3,3, Real.valueOf(1 , 1 , -3, 1 ,-3 ,1, -3, 1, 1));
		assertEquals(A, M.adjoint());
		
		Matrix<Real> I = Matrix.identity(3);
		assertEquals(I, I.times(I));
		
		assertEquals(I.getRow(1), I.getColumn(1));
		assertEquals(I, M.times(M.inverse()));
		
		assertEquals(M, M.times(I));

	}

	@Test 
	public void randomMatrix (){
		Matrix<Real> R = Matrix.random(3,3,2);
	
		// same seed produces the same matrix
		assertEquals(R, Matrix.random(3,3,2));
		
		// the matrix is invertible
		assertTrue(R.hasInverse());
		
		//big matrix
		Matrix<Real> P = Matrix.random(10,10,2);
		Matrix<Real> I = Matrix.identity(10);
//		assertEquals(I, P.times(P.inverse()));
	}
	
	@Test
	public void testDimentions(){
		

		// create speed
		Dimension V = Dimension.LENGTH.over(Dimension.TIME) ;
		// assert right dimensions
		assertEquals("LT^-1", V.toString());
		assertEquals(Dimension.VELOCITY, V);
		// create acceleration
		Dimension A = V.over(Dimension.TIME) ;
		// assert right dimensions
		assertEquals("LT^-2", A.toString());
		
		// create force
		Dimension F = A.times(Dimension.MASS) ;
		
		assertTrue(F.equals(A.times(Dimension.MASS)));
		assertFalse(F.equals(A));
		
		// get fundamental from calculus
		Dimension L = V.times(Dimension.TIME);
		// assert is the same object
		assertSame(L , Dimension.LENGTH);
		
		try {
			L = L.plus(Dimension.LENGTH);
		} catch (IncompatibleDimentionException e){
			assertFalse (true);
		}
		try {
			L = L.plus(Dimension.TIME);
		} catch (IncompatibleDimentionException e){
			assertTrue (true);
		}
	}	
	
	@Test
	public void testUnits(){
		Unit m = Unit.unit( Dimension.LENGTH, "m");
		Unit s = Unit.unit( Dimension.TIME, "s");
		
		m.plus(m);
		s.minus(s);
		
		Unit v = m.over(s);
		assertEquals("ms^-1" , v.symbol());
		
		
	    //m = v.times(s);
	    assertEquals("m" , m.symbol());
	    
	    // assert v not changed
	    assertEquals("ms^-1" , v.symbol());
		
	}	
	
	@Test
	public void testMeasures(){
		DecimalMeasure L = DecimalMeasure.measure(3, 0.2 , SI.METER);
		DecimalMeasure F = DecimalMeasure.measure(5, 0.1 , SI.METER);
		
		DecimalMeasure S = F.plus(L);
		assertEquals (DecimalMeasure.measure(8, 0.3, SI.METER) , S);
		
		DecimalMeasure area = F.times(L);
		assertEquals (DecimalMeasure.measure(15, 1.30, SI.METER.raise(2)) , area);
		
		
		Integer cem =  Integer.valueOf(100);
		Integer tres =  Integer.valueOf(3);
		Integer seis =  Integer.valueOf(6);
		Integer tresentos =  Integer.valueOf(300);
		
		assertEquals(seis,tres.plus(tres));
		
		assertEquals(tresentos,cem.times(tres));
		
		DecimalMeasure l = DecimalMeasure.exact(200, SI.METER );
		DecimalMeasure t = DecimalMeasure.exact(10, SI.SECOND);
		DecimalMeasure v = l.over(t);
		
		assertEquals(DecimalMeasure.exact(20,  SI.METER.over(SI.SECOND) ), v);
		assertEquals(Dimension.VELOCITY, v.unit().dimension());
		
		DecimalMeasure v2 = v.times(v);
		Dimension dim =  Dimension.VELOCITY.times(Dimension.VELOCITY);
		assertEquals(dim,v2.unit().dimension());
		DecimalMeasure m = DecimalMeasure.exact(50, SI.KILOGRAM );
		DecimalMeasure y = m.times(v2);
		DecimalMeasure EC = y.times(Real.valueOf(0.5));
		
		assertEquals(Dimension.ENERGY, EC.unit().dimension());
		
	}
	
	@Test
	public void testMoney (){
		
		Money a = Money.money(100, "USD");
		Money b = Money.money(230, "USD");
		Money t = Money.money(330, "USD");
		
		Money c = Money.money(330, "EUR");
		
		Money m = a.plus(b);

		assertEquals(Money.class, m.getClass());
		assertEquals(t, m);
		assertFalse(t.equals(c));
		
		try {
			t.plus(c);
			assertFalse(true);
		} catch (IncompatibleUnitsException e){
			assertTrue(true);
		} catch (Exception e){
			assertFalse(true);
		}

		try {
			Money.money(330, "EU");
			assertFalse(true);
		} catch (IllegalArgumentException e){
			assertTrue(true);
		} catch (Exception e){
			assertFalse(true);
		}
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
	public void testNumbers(){
		
		Real a = Real.valueOf(1.2);
		Real b = Real.valueOf(1.2);
		Real c = Real.valueOf(2.4);
		
		assertEquals(c, a.plus(b));
		
		Integer i = Integer.valueOf(10);
		Integer j = Integer.valueOf(12);
		
		assertEquals(Real.valueOf(11.2), a.plus(i));
		assertEquals(j, i.times(a));
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