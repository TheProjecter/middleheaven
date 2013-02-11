package org.middleheaven.sequences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.util.collections.Range;

public class RangeTest {

	Range<Integer, Integer> R;
	Range<Integer, Integer> S;
	Range<Integer, Integer> E;
	Range<Integer, Integer> T;
	Range<Double, Double> D;
	
	@Before 
	public void before(){
		R = Range.<Integer, Integer>from(2).upTo(4);
		S = Range.<Integer, Integer>from(7).upTo(10);
		T = Range.<Integer, Integer>from(5).upTo(9);
		E = Range.emptyRange();
		D = Range.<Double, Double>from(1.2).upTo(4.8);
	}
	
	@Test
	public void range(){
		
		// not empty
		Range<Integer, Integer> range = Range.<Integer, Integer>from(1).upTo(1);
		
		assertFalse("Range is empty" , range.isEmpty());

		assertEquals("Range is not 1 " , 1 , range.size());
		
		List<Integer> list = range.into(new ArrayList<Integer>());
		
		assertEquals(1, list.size());

		Range<Integer, Integer> emptyRange = Range.<Integer, Integer>from(1).upTo(1).excludeEnd(); // <=> [1, 1[ = []
		
		assertTrue("Range is not empty" , emptyRange.isEmpty());

		list = emptyRange.into(new ArrayList<Integer>());
		
		assertEquals(0, list.size());
		
		assertEquals("Range is not 0 " , 0 , emptyRange.size());
		
		emptyRange = Range.<Integer, Integer>from(1).upTo(1).excludeStart(); // <=> ]1, 1] = []
		
		assertTrue("Range is not empty" , emptyRange.isEmpty());

		assertEquals("Range is not 0 " , 0 , emptyRange.size());
		
	
		range = Range.<Integer, Integer>from(1).upTo(6);
		
		list = range.into(new ArrayList<Integer>());
		
		assertEquals(6, range.size());
		
		assertEquals(range.size(), list.size());
		
		List<Integer> selected = new ArrayList<Integer>();
		for(int i=0; i < 10 ;i++){
			selected.add(range.random());
		}
		
		assertEquals(10,selected.size());
		
	}
	
	
	@Test
	public void testReverseOrder() {
			

			final Range<Integer, Integer> range = Range.<Integer, Integer>from(4).upTo(2);
			
			
			assertTrue(range.contains(4));
			assertTrue(range.contains(3));
			assertTrue(range.contains(2));
			
			List<Integer> list = range.into(new ArrayList<Integer>());
			List<Integer> proof = Arrays.<Integer>asList(new Integer[]{4, 3, 2});
	
			assertEquals(proof, list);
	}
	
	@Test
	public void testIsEmpty() {
		assertTrue(E.isEmpty());
	}
	
	@Test
	public void testIntersects() {
		assertTrue(S.intersects(T));
		assertTrue(T.intersects(S));
		assertFalse(S.intersects(R));
		assertFalse(S.intersects(E));
		
	}
	
	@Test
	public void testRaise() {
		assertEquals(Real.valueOf(1),Real.valueOf(2).raise(0));
		assertEquals(Real.valueOf(8),Real.valueOf(2).raise(3));
		assertEquals(Real.valueOf("0.1"),Real.valueOf(10).raise(-1));
	}
	
	@Test
	public void testIteration() {
		
		List<Integer> a = new ArrayList<Integer>();
		for (Integer i : R){
			a.add(i);
		}
		assertEquals(3, a.size());
	
		List<Double> d = new ArrayList<Double>();
		
		for (Double i : Range.from(1.2).upTo(4.1)){
			d.add(i);
		}
		assertTrue(d.size()==3);
		d.clear();
		
		// 0.1 is not correctly represented in double.
		for (Double i : Range.from(1.0).by(0.1).upTo(1.6)){
			d.add(i);
		}
		assertFalse(7==d.size());
		
		
		// 0.1 is correctly represented in BigDecimal.
		List<BigDecimal> b = new ArrayList<BigDecimal>();
		Range<BigDecimal, BigDecimal> range = Range.<BigDecimal, BigDecimal>from(new BigDecimal("1.0")).by(new BigDecimal("0.1")).upTo(new BigDecimal("1.6"));
		
		for (BigDecimal i : range){
			b.add(i);
		}
		assertEquals(7,b.size());
		
		// the toList method does the same  
		
		assertEquals(7, range.into(new ArrayList<BigDecimal>()).size());
		
		
		b = Range.<BigDecimal, BigDecimal>from(new BigDecimal("1.0")).upTo(new BigDecimal("3")).into(new ArrayList<BigDecimal>());
		assertEquals(3,b.size());
	
		
		Range<BigDecimal, BigDecimal> rb =Range.<BigDecimal, BigDecimal>from(new BigDecimal("1")).upTo(new BigDecimal("3"));
		assertEquals(3,rb.size());
		
		
		assertEquals(7, Range.from(Real.valueOf("1.0")).by(Real.valueOf("0.1")).upTo(Real.valueOf("1.6")).size());
		
		assertEquals(7, Real.valueOf("1.0").upTo(Real.valueOf("1.6"),Real.valueOf("0.1")).size());
		
		// Range over CalendarDate
		List <CalendarDate> list = CalendarDate.date(2007,1,5).upTo(CalendarDate.date(2007,1,10)).into(new ArrayList<CalendarDate>());
		assertEquals(6,list.size());

		assertEquals(6,CalendarDate.date(2007,1,5).upTo(CalendarDate.date(2007,1,10)).size());
	}

	@Test
	public void testIntersect() {
		Range<Integer, Integer>  I = S.intersection(T);
		
		assertEquals(I, Range.from(7).upTo(9));
	
	}

	@Test
	public void testContainsT() {
		assertTrue(S.contains(8));
		assertFalse(R.contains(8));
	}



}
