/**
 * 
 */
package org.middleheaven.core.wiring;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.core.FileContext;
import org.middleheaven.core.annotations.Wire;
import org.middleheaven.logging.LoggingService;
import org.middleheaven.tool.test.MiddleHeavenTestCase;


/**
 * 
 */
public class ScopeWiringTest extends MiddleHeavenTestCase{

	
	FileContext a;
	FileContext b;
	
	LoggingService c;
	LoggingService d;
	
	@Wire
	public void setA(FileContext fs){
		a = fs;
	}
	
	@Wire
	public void setB(FileContext fs){
		b = fs;
	}
	
	@Wire
	public void setC(LoggingService fs){
		c = fs;
	}
	
	@Wire
	public void setD(LoggingService fs){
		d = fs;
	}
	
	@Test
	public void testSameObject(){
		
		assertNotNull(a);
		assertTrue (a == b);
	}
	
	
	@Test
	public void testSameFromActivator(){
		
		assertNotNull(c);
		assertTrue (c == d);
	}
}
