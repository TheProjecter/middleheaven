/**
 * 
 */
package org.middleheaven.quantity.math.structure;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.Test;

/**
 * 
 */
public class BigDecimalRealTest {

	@Test
	public void test() {
		
		BigDecimal d = new BigDecimal("0.003");
		BigDecimal u = new BigDecimal("3000");
		u = u.setScale(-3);
		
		Assert.assertEquals(BigDecimalReal.fraction(3, 1000),new BigDecimalReal(d));
		Assert.assertEquals(BigDecimalReal.fraction(3000, 1), new BigDecimalReal(u));
	}

}
