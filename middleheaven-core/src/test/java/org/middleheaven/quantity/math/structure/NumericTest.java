package org.middleheaven.quantity.math.structure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.math.RealField;
import org.middleheaven.quantity.math.structure.BigDecimalMath;
import org.middleheaven.quantity.math.structure.BigDecimalReal;


public class NumericTest {

	@Test
	public void testSimplify(){
	

		BigDecimalReal real = (BigDecimalReal) BigDecimalReal.valueOf("3").over(BigDecimalReal.valueOf("15"));
	
		assertEquals(BigDecimalReal.valueOf("5") , real.inverse());
		
	}
	
	@Test
	public void testIsInteger(){
		assertTrue( BigDecimalMath.isInteger(BigDecimal.valueOf(84)));
		assertFalse( BigDecimalMath.isInteger(new BigDecimal("0.012")));
		assertFalse( BigDecimalMath.isInteger(new BigDecimal("1.2")));
	}
	
	@Test
	public void testLcm(){
		
		BigDecimal a = BigDecimal.valueOf(21 );
		BigDecimal b = BigDecimal.valueOf(6);
		BigDecimal c = BigDecimal.valueOf(42);

		assertEquals(c , BigDecimalMath.lcm(a, b));
		assertEquals(c , BigDecimalMath.lcm(b, a));
		assertEquals(c.negate() , BigDecimalMath.lcm(a.negate(), b.negate()));
		
		assertEquals(c , BigDecimalMath.lcm(a.negate(), b));
		

	}
	
	@Test
	public void testGcd(){
		BigDecimal a = BigDecimal.valueOf(84);
		BigDecimal b = BigDecimal.valueOf(18);
		BigDecimal c = BigDecimal.valueOf(6);
		
		BigDecimal k = BigDecimal.valueOf(13); 
		
		assertEquals(c , BigDecimalMath.gcd(a, b));
		assertEquals(c , BigDecimalMath.gcd(b, a));
		assertEquals(c.negate() , BigDecimalMath.gcd(a.negate(), b.negate()));
		
		assertEquals(c , BigDecimalMath.gcd(a.negate(), b));
		
		// cgd (a.k, b.k) = k .gcd(a,b)
		assertEquals(c.multiply(k) , BigDecimalMath.gcd(a.multiply(k), b.multiply(k)));
	
		
	}
	
	@Test
	public void testCos(){
		BigDecimal pi = BigDecimal.valueOf(Math.PI);
		final BigDecimal halfSqrt2 = new BigDecimal("0.707106781186547");
		
		int scale = 15;
		assertNumberEquals(BigDecimal.ONE,BigDecimalMath.cos(BigDecimal.ZERO, scale));
		assertNumberEquals(BigDecimal.ONE.negate(),BigDecimalMath.cos(pi, scale));
		assertNumberEquals(BigDecimal.ZERO,BigDecimalMath.cos(pi.divide(BigDecimal.valueOf(2)), scale));
		
		assertNumberEquals(halfSqrt2,BigDecimalMath.cos(pi.divide(BigDecimal.valueOf(4)), scale));
		
	}
	
	@Test
	public void testSin(){
		BigDecimal pi = BigDecimal.valueOf(Math.PI);
		final BigDecimal halfSqrt2 = new BigDecimal("0.707106781186547");
		
		int scale = 15;
		assertNumberEquals(BigDecimal.ZERO,BigDecimalMath.sin(BigDecimal.ZERO, scale));
		assertNumberEquals(BigDecimal.ZERO,BigDecimalMath.sin(pi, scale));
		assertNumberEquals(BigDecimal.ONE,BigDecimalMath.sin(pi.divide(BigDecimal.valueOf(2)), scale));
		assertNumberEquals(halfSqrt2,BigDecimalMath.sin(pi.divide(BigDecimal.valueOf(4)), scale));
		
	}
	
	@Test
	public void testArcTan(){
		
		Real ZERO = RealField.getInstance().zero();
		Real ONE = RealField.getInstance().one();
		
		assertEquals(ZERO,ZERO.arctan());

		// arcTan(1/sqrt(3)) = pi/6
		
		final Real k = Real.valueOf(3);
		
		final Real expected = Real.valueOf(Math.PI).over(6);
		
		final Real value = k.sqrt().inverse().arctan();
		
		assertInRange(expected,value, 0.001);
		
		// arcTan(1) = pi/4
		
		Real pi4 = Real.valueOf(Math.PI);

		
		assertInRange(pi4,ONE.arctan().times(4), 1E-20);
	
	}
	
	/**
	 * @param expected
	 * @param value
	 */
	private void assertInRange(Real expected, Real value, double range) {
		
		final Real err = Real.valueOf("1").minus(value.over(expected));
		if (err.compareTo(Real.valueOf(range)) > 0 ){
			throw new AssertionError("Expected value ("+ expected+ ") not in range of observed value (" + value +") error was = " + err + ")");
		}
		
	}

	@Test
	public void testSinSQRT(){
		// sin(pi/4) = sqrt(2)/2
		
		Real pi4 = Real.valueOf(Math.PI).over(4);
		Real two = Real.valueOf(2);
		
		Real a = two.sqrt().over(2);
		Real b = pi4.sin();

		assertInRange(a,b, 1E-15);
	}

	@Test
	public void testCosSQRT(){
		// cos(pi/4) = sqrt(2)/2
		
		Real pi4 = Real.valueOf(Math.PI).over(4);
		Real two = Real.valueOf(2);
		
		Real a = two.sqrt().over(2);
		Real b = pi4.cos();
		
		assertInRange(a,b, 1E-20);
	}
	
	
	@Test
	public void testCosSenSQRT(){
		
		Real pi4 = Real.valueOf(Math.PI).over(4);
	
		assertInRange( Real.valueOf(1), pi4.cos().raise(2).plus( pi4.sin().raise(2)), 1E-19);
	}
	@Test
	public void testExp(){
		
		
		assertInRange(Real.valueOf(Math.E), RealField.getInstance().one().exp(), 1E-20);
	}
	
	private void assertNumberEquals(BigDecimal a, BigDecimal b) {
		if(a.compareTo(b)!=0){
			throw new AssertionError("Expected <" + a + "> but was <" + b +">");
		}
	}
}
