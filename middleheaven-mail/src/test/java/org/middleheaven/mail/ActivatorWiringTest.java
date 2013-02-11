/**
 * 
 */
package org.middleheaven.mail;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.middleheaven.core.annotations.Shared;
import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;
import org.middleheaven.mail.service.JavaMailActivator;
import org.middleheaven.namedirectory.NameDirectoryService;
import org.middleheaven.namedirectory.jndi.JNDINameDirectoryService;
import org.middleheaven.tool.test.MiddleHeavenTestCase;


/**
 * 
 */
public class ActivatorWiringTest extends MiddleHeavenTestCase{

	@Test
	public void testInit(){
		
		this.getWiringService().addConfiguration(new BindConfiguration (){

			@Override
			public void configure(Binder binder) {
				binder.bind(NameDirectoryService.class).in(Shared.class).toInstance(new JNDINameDirectoryService());
				binder.bind(JavaMailActivator.class).in(Shared.class).to(JavaMailActivator.class);
			}

		});
		
		JavaMailActivator instance = this.getWiringService().getInstance(JavaMailActivator.class);
		
		assertNotNull(instance);
		
	}
}
