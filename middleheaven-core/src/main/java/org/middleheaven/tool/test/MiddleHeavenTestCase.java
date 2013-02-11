package org.middleheaven.tool.test;

import org.junit.After;
import org.junit.Before;
import org.middleheaven.core.annotations.Component;
import org.middleheaven.core.bootstrap.BootstrapContext;
import org.middleheaven.core.services.ServiceBuilder;
import org.middleheaven.core.wiring.TypeWiringItem;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.io.repository.FileRepositoryActivator;
import org.middleheaven.io.repository.FileRepositoryService;

@Component
public abstract class MiddleHeavenTestCase extends TestBootstrap {


	
	public void preConfig(BootstrapContext context){
		context.registerService(ServiceBuilder
				.forContract(FileRepositoryService.class)
				.activatedBy(new FileRepositoryActivator())
				.newInstance()
		);
	}
	
	@Before
	public final void setUp(){
	
		//getWiringService().addItem(new TypeWiringItem(this.getClass()));
		
		this.start();
		
		setupWiringBundles(getWiringService());

		getWiringService().wireMembers(this);
		
		configurateTest();
	}

	
	@After
	public void tearDown(){
		this.stop();
	}
	
	protected void configurateTest() {

	}
	
	/**
	 * Override to configure activators needed for the test.
	 * @param classSet a SetActivatorScanner to add the necessary activators.
	 */
	protected void setupWiringBundles(WiringService service){
		
	}

	
}
