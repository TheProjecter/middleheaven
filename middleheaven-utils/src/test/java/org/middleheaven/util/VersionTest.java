/**
 * 
 */
package org.middleheaven.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 */
public class VersionTest {

	@Test
	public void testVErsionParsing() {
		Version v = Version.valueOf("6.1.2.29.July 19 2010 1458");
		
		assertEquals(6 , v.getMajor());
		assertEquals(1 , v.getMinor());
		assertEquals(2 , v.getRevision());
		assertEquals(29 , v.getBuild());
		assertEquals("July 19 2010 1458" , v.getTag());
	}

}
