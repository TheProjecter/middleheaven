package org.middleheaven.core.wiring;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.middleheaven.core.annotations.Service;
import org.middleheaven.core.annotations.Wire;
import org.middleheaven.core.wiring.mock.DictionaryService;
import org.middleheaven.core.wiring.mock.GermanDictionayService;
import org.middleheaven.core.wiring.mock.GreekDictionayService;
import org.middleheaven.logging.Logger;
import org.middleheaven.tool.test.MiddleHeavenTestCase;

public class WiringProfilesTestCase extends MiddleHeavenTestCase {

	@Wire Logger log;
	@Wire WiringService wiringService;

	protected void setupWiringBundles(WiringService wiringService){

		wiringService.getActiveProfiles().add("dev");

		wiringService.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(DictionaryService.class).in(Service.class).to(GreekDictionayService.class);
				binder.bind(DictionaryService.class).in(Service.class).to(GermanDictionayService.class);
			}

		});

		
	
	}

	@Test
	public void testWiringServiceWithProfileDev(){


		final WiringService wiringService = this.getWiringService();

		DictionaryService eDic = wiringService.getInstance(DictionaryService.class);

		assertEquals("ge", eDic.getLang());


	}
	


}
