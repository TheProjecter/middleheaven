package org.middleheaven.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.Enumerable;
import org.middleheaven.util.function.BinaryOperator;
import org.middleheaven.util.function.Mapper;


public class UtilsTest {


	@Test
	public void testIntersect (){
		
		Collection<String> a = new ArrayList<String>(); 
		a.add("A");
		a.add("A");
		a.add("B");
		a.add("C");
		a.add("D");
		
		Collection<String> b = new ArrayList<String>(); 
		b.add("A");
		b.add("B");
		b.add("B");
		b.add("D");
		
		Collection<String> r = new ArrayList<String>(); 
		r.add("A");
		r.add("B");
		r.add("D");
		
		assertTrue(CollectionUtils.equalContents(r, CollectionUtils.intersect(a, b)));
	}
	
	@Test
	public void testTime (){
		
		Collection<String> a = new ArrayList<String>(); 
		a.add("A");
		a.add("B");
		a.add("C");
		a.add("D");
		
		Collection<String> b = new ArrayList<String>(); 
		b.add("A");
		b.add("B");
		b.add("D");
		
		long time= System.nanoTime();
		CollectionUtils.intersect(a, b);
		long del = System.nanoTime() - time;
		
		a = new HashSet<String>(); 
		a.add("A");
		a.add("B");
		a.add("C");
		a.add("D");
		
		time= System.nanoTime();
		CollectionUtils.intersect(a, b);
		long del2 = System.nanoTime() - time;
		
		assertTrue("Faster" , del2<del);
	
	}
	
	@Test
	public void testSpecialIterator(){
		
		List<List<Integer>> lists = new ArrayList<List<Integer>>(); 
		
		List<Integer> a = new ArrayList<Integer>();
		a.add(1);
		a.add(2);
		a.add(3);
		
		List<Integer> b = new ArrayList<Integer>();
		b.add(-1);
		b.add(-2);
		b.add(-3);
		b.add(-4);
		
		lists.add(a);
		lists.add(b);
		
		IteratorsIterator<Integer>  it =IteratorsIterator.aggregateIterables(lists);
		
		int count =0;
		for (;it.hasNext();){
			it.next();
			count++;
		}
		
		assertEquals(7, count);
		
		
		
	}
	
	@Test
	public void testMapAll(){
		
		List<Integer> a = new ArrayList<Integer>();
		a.add(1);
		a.add(2);
		a.add(3);
		
		List<Integer> result = new ArrayList<Integer>();
		
		final Mapper<Enumerable<Integer>, Integer> mapper = new Mapper<Enumerable<Integer>, Integer>(){

			@Override
			public Enumerable<Integer> apply(Integer i) {
				return CollectionUtils.asEnumerable(i , i * 10 , i *100);
			}
			
		};
		
		CollectionUtils.asEnumerable(a).mapAll(mapper).into(result);
		
		assertEquals(9, result.size());
		
		final BinaryOperator<Integer> operator = new BinaryOperator<Integer>(){

			@Override
			public Integer apply(Integer a, Integer b) {
				return a + b;
			}
			
		};
		
		Integer total = CollectionUtils.asEnumerable(a).mapAll(mapper).reduce(0, operator);
		
		assertEquals(666, total);
		
		total = CollectionUtils.asEnumerable(a).mapReduce(0, mapper, operator);
		
		assertEquals(666, total);
	}
}
