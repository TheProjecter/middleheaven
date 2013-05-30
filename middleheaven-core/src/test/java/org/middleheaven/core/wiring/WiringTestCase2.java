package org.middleheaven.core.wiring;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.middleheaven.core.bootstrap.ServiceRegistry;
import org.middleheaven.core.wiring.mock.C;
import org.middleheaven.core.wiring.mock.X;
import org.middleheaven.tool.test.MiddleHeavenTestCase;

public class WiringTestCase2 extends MiddleHeavenTestCase {


	protected void setupWiringBundles(WiringService service){
		service.addItemBundle( new ClassSetWiringBundle().add(C.class).add(X.class));
	}
	

	@Test
	public void testInheritanceWiring(){

		C c = ServiceRegistry.getService(WiringService.class).getInstance(C.class);
		
		assertNotNull(c);
		assertNotNull(c.getX());
		
		assertNotNull(c.getX2());
	}
	
	
	
}
