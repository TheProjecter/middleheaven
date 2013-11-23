package org.middleheaven.core.wiring;

import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.middleheaven.core.annotations.Service;
import org.middleheaven.core.annotations.Wire;
import org.middleheaven.core.wiring.mock.DictionaryService;
import org.middleheaven.core.wiring.mock.GermanDictionayService;
import org.middleheaven.core.wiring.mock.GreekDictionayService;
import org.middleheaven.logging.Logger;
import org.middleheaven.tool.test.MiddleHeavenTestCase;

public class WiringProfilesNotExistantTestCase extends MiddleHeavenTestCase {

	@Wire Logger log;
	@Wire WiringService wiringService;

	protected void setupWiringBundles(WiringService wiringService){

		wiringService.getActiveProfiles().add("test"); // none of the services below have this profile

		wiringService.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(DictionaryService.class).in(Service.class).to(GreekDictionayService.class);
				binder.bind(DictionaryService.class).in(Service.class).to(GermanDictionayService.class);
			}

		});
	}

	
	@Test
	public void testWiringServiceWithProfileTestDefault(){

		// the two services are negated by profiles as the active profile does not match neither.
		final WiringService wiringService = this.getWiringService();

		wiringService.getActiveProfiles().add("test");

		wiringService.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(DictionaryService.class).in(Service.class).to(GreekDictionayService.class);
				binder.bind(DictionaryService.class).in(Service.class).to(GermanDictionayService.class);
			}

		});


		DictionaryService eDic = wiringService.getInstance(DictionaryService.class);

		assertNull("dictionary service not found", eDic);



	}



}
