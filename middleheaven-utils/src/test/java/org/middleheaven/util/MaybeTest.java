/**
 * 
 */
package org.middleheaven.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.util.function.BinaryOperator;
import org.middleheaven.util.function.Function;

/**
 * 
 */
public class MaybeTest {

	@Test
	public void testMap() {
		Maybe<String> s = Maybe.of("Middleheaven");
		
		Maybe<Integer> r = s.map(new Function<Integer, String>(){

			@Override
			public Integer apply(String object) {
				return object.length();
			}
			
		});
		
		assertTrue(r.isPresent());
		assertEquals(Maybe.of(12) ,r );
		
		Maybe<Integer> rr = s.flatMap(new Function<Maybe<Integer>, String>(){

			@Override
			public Maybe<Integer> apply(String object) {
				return Maybe.of(object.length());
			}
			
		});
		
		assertTrue(rr.isPresent());
		assertEquals(Maybe.of(12) ,rr );
	}
	
	@Test
	public void testApply() {
		
		BinaryOperator<Integer> op = new BinaryOperator<Integer>(){

			@Override
			public Integer apply(Integer a , Integer b) {
				return  a + b;
			}
			
		};
		
		Maybe<Integer> a = Maybe.of(1);
		Maybe<Integer> b = Maybe.of(2);

		Maybe<Integer> r = a.apply(b, op );
		
		assertTrue(r.isPresent());
		assertEquals(Maybe.of(3) ,r );
		
		Maybe<Integer> n = Maybe.absent();
		
		r = a.apply(n, op);
		
		assertTrue(r.isAbsent());
	}

}
