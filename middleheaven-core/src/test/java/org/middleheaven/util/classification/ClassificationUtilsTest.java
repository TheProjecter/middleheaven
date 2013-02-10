package org.middleheaven.util.classification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.Enumerable;
import org.middleheaven.util.collections.FilteredIterator;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.function.Mapper;
import org.middleheaven.util.function.Predicate;

public class ClassificationUtilsTest {

	final Predicate<Integer> predicate = new Predicate<Integer>(){

		@Override
		public Boolean apply(Integer obj) {
			return obj.intValue() % 2 ==0;
		}
		
	};
	
	@Test 
	public void testArrayIterator(){
		
		Iterator<Integer> it = CollectionUtils.arrayIterator(1 ,2 ,3 , 4);
		
		Integer r = it.hasNext() ? it.next() : null;
		
		assertEquals(1 ,r);
		
	}
	
	@Test 
	public void testFilteredIterator(){
		
		Iterator<Integer> it = new FilteredIterator<Integer>( CollectionUtils.arrayIterator(1 ,2 ,3 , 4), predicate);
		
		Integer r = it.hasNext() ? it.next() : null;
		
		assertEquals(2 ,r);
		
	}
	
	@Test
	public void testClassificationUtils(){

		Enumerable<Integer> c = CollectionUtils.asEnumerable(3,4,1,2);
		
		assertEquals(4,c.size());
		
		ArrayList<Integer> result = c.filter(predicate).into(new ArrayList<Integer>());
		
		assertEquals(2,result.size());
		
		assertTrue(CollectionUtils.equalContents(Arrays.asList(4,2), result));
		
		Integer r = c.find(predicate);
		
		assertEquals(Integer.valueOf(4),r);
	
		
		final List<Integer> rest = new ArrayList<Integer>();
		c.forEach(new Block<Integer>(){
			public void apply(Integer o){
				rest.add(o);
			}
		});
		
		assertEquals(4,rest.size());
		
		assertTrue(CollectionUtils.equalContents(Arrays.asList(1,2,3,4), c.sort().into(new ArrayList<Integer>())));
	}
	
	@Test
	public void testWalterFilter(){
		Enumerable<Integer> all = CollectionUtils.asEnumerable(3,4,1,2,-2, 5,6);
		List<Integer> proof = Arrays.asList(16,4,4,36);
		
		
		List<Integer> result = all.filter(new Predicate<Integer>(){

			@Override
			public Boolean apply(Integer i) {
				return i.intValue() % 2 == 0; // only the even ones
			}
			
		}).map(new Mapper<Integer, Integer>(){

			@Override
			public Integer apply(Integer i) {
				return i.intValue() * i.intValue(); // square
			}
			
		}).into(new ArrayList<Integer>());
		
		assertTrue(CollectionUtils.equalContents(proof, result));
		
	}
	
}
