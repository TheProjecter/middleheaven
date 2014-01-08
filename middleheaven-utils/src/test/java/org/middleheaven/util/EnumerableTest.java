/**
 * 
 */
package org.middleheaven.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;
import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.Pair;
import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.util.function.Predicate;

/**
 * 
 */
public class EnumerableTest {

	@Test
	public void testConcat() {
	
		Enumerable<Integer> a = Enumerables.asEnumerable(1, 2, 3, 4);
		Enumerable<Integer> b = Enumerables.asEnumerable(5, 6, 7, 8);
		Enumerable<Integer> c = Enumerables.asEnumerable(1, 2, 3, 4, 5, 6, 7, 8);
		
		Enumerable<Integer> r = a.concat(b);

		assertTrue(CollectionUtils.equalContents(c.into(new ArrayList<Integer>()), r.into(new ArrayList<Integer>())));
		
		r = a.concat(Enumerables.<Integer>emptyEnumerable());
		assertTrue(CollectionUtils.equalContents(a.into(new ArrayList<Integer>()), r.into(new ArrayList<Integer>())));
		
		r = Enumerables.<Integer>emptyEnumerable().concat(a);
		assertTrue(CollectionUtils.equalContents(a.into(new ArrayList<Integer>()), r.into(new ArrayList<Integer>())));
		
		
		Enumerable<Number> x = Enumerables.<Number>asEnumerable(1, 2.0d, 3f, 4L);
		Enumerable<Number> u = Enumerables.<Number>asEnumerable(1, 2.0d, 3f, 4L,5, 6, 7, 8);
		
		Enumerable<Number> d = x.concat(b);
		assertTrue(CollectionUtils.equalContents(u.into(new ArrayList<Number>()),d.into(new ArrayList<Number>())));
		
		Enumerable<Number> z = Enumerables.<Number, Integer>safeCast(b);
		d = x.concat(z);
		assertTrue(CollectionUtils.equalContents(u.into(new ArrayList<Number>()),d.into(new ArrayList<Number>())));
		
		
		r = a.concat(5).concat(6).concat(7).concat(8);
		assertTrue(CollectionUtils.equalContents(c.into(new ArrayList<Integer>()), r.into(new ArrayList<Integer>())));
		
	}

	@Test
	public void testDistinct() {
		
		Enumerable<Integer> a = Enumerables.asEnumerable(1, 2, 3, 2, 4, 5, 3, 2 ,1);
		
		assertEquals(5, a.distinct().size());
	}
	
	@Test
	public void testToArray() {
		Enumerable<Integer> a = Enumerables.asEnumerable(1, 2, 3, 4);
		
		Integer[] arr = a.asArray();
		
		assertNotNull(arr);
		assertEquals(4, arr.length);
		
		arr = a.filter(new Predicate<Integer>(){

			@Override
			public Boolean apply(Integer obj) {
				return true;
			}}).asArray();
		
		assertNotNull(arr);
		assertEquals(4, arr.length);
		
		arr = a.concat(Enumerables.<Integer>emptyEnumerable()).asArray();
		
		assertNotNull(arr);
		assertEquals(4, arr.length);
		
		arr = a.concat(5).asArray();
		
		assertNotNull(arr);
		assertEquals(5, arr.length);
	}
	
	@Test
	public void testJoin() {
		Enumerable<Integer> a = Enumerables.asEnumerable(1, 2, 3, 4);
		
		Enumerable<String> b = Enumerables.asEnumerable("1", "2", "3", "4", "%");
		
		for(Pair<Integer, String> p : a.join(b)){
			assertEquals(p.left().toString(), p.right().toString());
		}
		
	}
}
