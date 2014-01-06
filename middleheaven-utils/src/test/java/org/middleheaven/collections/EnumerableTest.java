/**
 * 
 */
package org.middleheaven.collections;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.quantity.time.Period;
import org.middleheaven.util.StopWatch;
import org.middleheaven.util.function.Predicate;

/**
 * 
 */
public class EnumerableTest {

	
	@Test
	public void testEnumerable(){
		
		Enumerable<Integer> e = Enumerables.asEnumerable(1,2,3,4,5,7,8,9);
		
		assertFalse(e.isEmpty());
		
		for(Integer i : e){
			
		}
		
		assertFalse(e.isEmpty());
	}
	
	@Test
	public void testReverseEnumerable(){
		
		final int limit = 1000000;
		Enumerable<Integer> r = Enumerables.range(1,limit).reverse();
		
		assertFalse(r.isEmpty());
		assertEquals(limit, r.size());
		StopWatch w = StopWatch.start();
		for(Integer i : r){
			
		}
		Period faster = w.mark();
		
		assertFalse(r.isEmpty());
		Enumerable<Integer> e = Enumerables.emptyEnumerable();
		for (int i =1 ; i <= limit; i++){
			e = e.concat(i);
		}
	
		r = e.reverse();
		
		assertFalse(r.isEmpty());
		assertEquals(limit, r.size());
		w = StopWatch.start();
		for(Integer i : r){
			
		}
		Period slow = w.mark();

		assertTrue("Indexable Enumerable is not faster", faster.milliseconds() < slow.milliseconds() );
	}
	
	@Test
	public void testFilterEnumerable(){
		
		Enumerable<Integer> e = Enumerables.asEnumerable(1).concat(2).concat(4).concat(5).concat(5).concat(7).concat(8).concat(9);
		
		assertFalse(e.isEmpty());
		
		Enumerable<Integer> f = e.filter(new Predicate<Integer>() {
			
			@Override
			public Boolean apply(Integer object) {
				return object % 2 == 0;
			}
		});
		
		assertFalse(f.isEmpty());
		assertEquals(3,f.size());
		for(Integer i : f){
			
		}
		assertEquals(3,f.size());
		assertFalse(f.isEmpty());
		
		Enumerable<Integer> d = e.distinct();
		
		assertFalse(d.isEmpty());
		assertEquals(7,d.size());
		for(Integer i : d){
			
		}
		assertEquals(7,d.size());

	}
}
