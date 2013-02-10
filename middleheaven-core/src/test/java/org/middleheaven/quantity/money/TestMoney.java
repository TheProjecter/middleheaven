package org.middleheaven.quantity.money;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.quantity.math.Real;

public class TestMoney {

	@Test
	public void testMoney(){
		
		CentsMoney oneCent = CentsMoney.valueOf("0.01", "USD");
		Real half = Real.fraction(1, 2);
		Real third = Real.fraction(1, 3);
		
		assertTrue(oneCent.times(half).isZero());
		
		assertTrue(oneCent.times(third).isZero());
		
	}
	
	@Test
	public void testMoneyBag(){
		
		CentsMoney oneUSD = CentsMoney.valueOf(1, "USD");
		CentsMoney oneBRL = CentsMoney.valueOf(1, "BRL");
		CentsMoney oneEUR = CentsMoney.valueOf(1, "EUR");
		
		MoneyBag bag = MoneyBag.of(oneUSD, oneBRL, oneEUR);
		MoneyBag bagTwice = MoneyBag.of(oneUSD.times(2), oneBRL.times(2), oneEUR.times(2));
		
		MoneyBag result = bag.plus(bag);
		
		assertFalse(bagTwice.equals(bag));
		
		assertEquals(bagTwice , result);
		
		result = bag.minus(bag);
		
		assertEquals(MoneyBag.empty() , result);
		
		assertTrue(result.isZero());
		
		assertEquals(bagTwice , bag.times(2));
		
		assertEquals(MoneyBag.of(oneUSD, oneBRL), MoneyBag.of(oneUSD, oneBRL, CentsMoney.valueOf(0,"EUR")));
		assertEquals(MoneyBag.of(oneUSD, oneBRL, CentsMoney.valueOf(0,"EUR")), MoneyBag.of(oneUSD, oneBRL));
		
		assertFalse(bag.equals(MoneyBag.of(oneUSD, oneBRL)));
		assertFalse(MoneyBag.of(oneUSD, oneBRL).equals(bag));
	}
	

	@Test
	public void testMoneyOperations (){
		
		CentsMoney a = CentsMoney.valueOf(100, "USD");
		CentsMoney b = CentsMoney.valueOf(230, "USD");
		CentsMoney t = CentsMoney.valueOf(330, "USD");
		
		CentsMoney c = CentsMoney.valueOf(330, "EUR");
		
		Money m = a.plus(b);

		assertEquals(t, m);
		
		// money are equal if both amount and currency are equal
		assertFalse(t.equals(c));

		// divide by a real
		Real n = Real.valueOf(3);
		Money y = t.over(n);
		assertEquals (CentsMoney.valueOf(110, "USD"), y);
		
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
	
}


