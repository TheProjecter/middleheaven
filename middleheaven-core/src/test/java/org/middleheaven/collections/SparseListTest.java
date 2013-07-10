/**
 * 
 */
package org.middleheaven.collections;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * 
 */
public class SparseListTest {

	@Test
	public void test() {
		
		List<Integer> list = new LinkedSparseList<Integer>(null);
		
		list.add(1);
		list.add(2);
		list.add(3);
		
		assertEquals(3, list.size());
		assertEquals(1, list.get(0));
		assertEquals(2, list.get(1));
		assertEquals(3, list.get(2));
		
		
		List<Integer> copy = new ArrayList<Integer>();
		for (Integer a : list){
			copy.add(a);
		}
		
		assertEquals(3, copy.size());
		
		for (int i = 0; i < copy.size(); i++){
			assertEquals(copy.get(i), list.get(i));
		}
		
	
		list.set(8, 8);
		
		assertEquals(9, list.size());
		assertEquals(1, list.get(0));
		assertEquals(2, list.get(1));
		assertEquals(3, list.get(2));
		assertEquals(8, list.get(8));
		assertNull(list.get(7));
		
		list.set(8, 88);
		
		assertEquals(9, list.size());
		assertEquals(1, list.get(0));
		assertEquals(2, list.get(1));
		assertEquals(3, list.get(2));
		assertEquals(88, list.get(8));
		assertNull(list.get(7));
		
		list.set(8, 8);
		
		list.add(9);
		
		assertEquals(10, list.size());
		assertEquals(1, list.get(0));
		assertEquals(2, list.get(1));
		assertEquals(3, list.get(2));
		assertEquals(8, list.get(8));
		assertEquals(9, list.get(9));
		assertNull(list.get(7));
		
		list.add(2, 4);
		
		assertEquals(11, list.size());
		assertEquals(1, list.get(0));
		assertEquals(2, list.get(1));
		assertEquals(4, list.get(2));
		assertEquals(3, list.get(3));
		assertEquals(8, list.get(9));
		assertEquals(9, list.get(10));
		assertNull(list.get(7));
	}
	
	@Test
	public void testSize() {
		List<Integer> list = new LinkedSparseList<Integer>(null, 8);
		assertEquals(8, list.size());
		for ( int i =0; i < 8; i++){
			assertNull(list.get(i));
		}
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testSizeOutofBounds() {
		List<Integer> list = new LinkedSparseList<Integer>(null, 8);
		assertNull(list.get(8));
	}
}
