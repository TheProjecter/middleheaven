package org.middleheaven.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.middleheaven.domain.query.ListQuery;
import org.middleheaven.domain.query.Query;
import org.middleheaven.util.collections.CollectionUtils;

public class TestQuery {

	
	@Test
	public void testQuery(){
		
		Query<String> q = new ListQuery<String>(){

			@Override
			protected List<String> list() {
				return Arrays.asList("A","B","C","D","E");
			}
			
		};
		
		assertFalse(q.isEmpty());
		assertEquals(5L, q.count());
		assertEquals("A", q.fetchFirst());
	
		Query<String> r = q.limit(1, 2);
		
		assertFalse(r.isEmpty());
		assertEquals(2L, r.count());
		assertEquals("A", r.fetchFirst());
		assertTrue(CollectionUtils.equalContents(Arrays.asList("A","B"), r.fetchAll()));
		
		r = q.limit(3, 3);
		
		assertFalse(r.isEmpty());
		assertEquals(3L, r.count());
		assertEquals("C", r.fetchFirst());
		assertTrue(CollectionUtils.equalContents(Arrays.asList("C","D","E"), r.fetchAll()));
		
		r = q.limit(4, 3);
		
		assertFalse(r.isEmpty());
		assertEquals(2L, r.count());
		assertEquals("D", r.fetchFirst());
		assertTrue(CollectionUtils.equalContents(Arrays.asList("D","E"), r.fetchAll()));
		
		r = q.limit(6, 3);
		
		assertTrue(r.isEmpty());
		assertEquals(0L, r.count());
		assertEquals(null, r.fetchFirst());
		assertTrue(r.fetchAll().isEmpty());
	}
}
