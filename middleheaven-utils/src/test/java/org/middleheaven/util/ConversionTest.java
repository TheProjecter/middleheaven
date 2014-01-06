package org.middleheaven.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.middleheaven.util.coersion.TypeCoercing;


public class ConversionTest {

	@Test
	public void testIdentity() {
		String s = "test";
		assertEquals(s, TypeCoercing.coerce(s, String.class));
		assertSame(s, TypeCoercing.coerce(s, String.class));
		CharSequence cs = "test";
		assertEquals(cs, TypeCoercing.coerce(s, String.class));
		assertSame(cs, TypeCoercing.coerce(s, String.class));
		assertEquals(cs, TypeCoercing.coerce(s, CharSequence.class));
		assertSame(cs, TypeCoercing.coerce(s, CharSequence.class));
	}
	
	@Test
	public void testCharSequenceNumber() {
		
		String a = "1";
		StringBuilder ba = new StringBuilder("1");
		Long one = 1L;
		
		assertEquals(one, TypeCoercing.coerce(a, Long.class));
		assertEquals(one, TypeCoercing.coerce(ba, Long.class));
		assertEquals(a, TypeCoercing.coerce(one, String.class));
		StringBuilder bs = TypeCoercing.coerce(one, StringBuilder.class);
		assertEquals(ba.toString(), bs.toString());
		
		String b = "1.23";
		Double d = 1.23;
		
		assertEquals(d, TypeCoercing.coerce(b, Double.class));
		assertEquals(b, TypeCoercing.coerce(d, String.class));

	}
}
