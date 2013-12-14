package org.middleheaven.core.wiring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.collections.ParamsMap;
import org.middleheaven.core.annotations.Service;
import org.middleheaven.core.annotations.Shared;
import org.middleheaven.core.annotations.Wire;
import org.middleheaven.core.bootstrap.ServiceRegistry;
import org.middleheaven.core.wiring.mock.C;
import org.middleheaven.core.wiring.mock.CyclicDisplayer;
import org.middleheaven.core.wiring.mock.DictionaryService;
import org.middleheaven.core.wiring.mock.DictionaryUser;
import org.middleheaven.core.wiring.mock.Displayer;
import org.middleheaven.core.wiring.mock.GermanDictionayService;
import org.middleheaven.core.wiring.mock.GreekDictionayService;
import org.middleheaven.core.wiring.mock.Greeter;
import org.middleheaven.core.wiring.mock.HashDictionaryService;
import org.middleheaven.core.wiring.mock.HelloMessage;
import org.middleheaven.core.wiring.mock.Message;
import org.middleheaven.core.wiring.mock.MockDisplay;
import org.middleheaven.core.wiring.mock.X;
import org.middleheaven.logging.Logger;
import org.middleheaven.tool.test.MiddleHeavenTestCase;

public class WiringTestCase extends MiddleHeavenTestCase {

	@Wire Logger log;
	@Wire WiringService wiringService;

	protected void setupWiringBundles(WiringService wiringService){

		wiringService.addItemBundle( new ClassSetWiringBundle().add(X.class.getPackage()));
		
		
		final HashDictionaryService en = new HashDictionaryService("en");
		final HashDictionaryService pt = new HashDictionaryService("pt");

		wiringService.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(DictionaryService.class).named("en").in(Service.class).toInstance(en);
				binder.bind(DictionaryService.class).named("pt").in(Service.class).toInstance(pt);
				binder.bind(DictionaryUser.class).in(Shared.class).to(DictionaryUser.class);
			}

		});
		
	
	}

	@Test
	public void testWiringModelReader(){
	
		
		DefaultWiringModelParser  parser = new DefaultWiringModelParser();

		BeanDependencyModel model = new BeanDependencyModel(C.class);
		parser.readBeanModel(C.class, model);

		assertNotNull(model.getProducingWiringPoint());
		assertNotNull(model.getAfterPoints());

		assertFalse("no after points", model.getAfterPoints().isEmpty());
	}


	@Test
	public void testInheritanceWiring(){

		C c = wiringService.getInstance(C.class);

		assertNotNull(c);
		assertNotNull(c.getX());
		
		assertNotNull(c.getX2());
	}


	@Test
	public void testPropertiesWiring(){
		final MockDisplay md = new MockDisplay();

		final WiringService wiringService = ServiceRegistry.getService(WiringService.class);

		wiringService.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(Displayer.class).in(Shared.class).toInstance(md);
				binder.bind(Message.class).in(Shared.class).to(HelloMessage.class);
				//binder.bind(Greeter.class).in(Shared.class).to(Greeter.class);
				binder.bindProperty(String.class).labeled("hello.message").toInstance("Hello");
				binder.bindProperty(String.class).labeled("hello.name").toInstance("World");
			}

		});

		wiringService.getInstance(Greeter.class)
		.sayHello();

		assertEquals("Hello, World", md.getSaing());
	}

	@Test
	public void testWiringServiceWithParams(){

		final WiringService wiringService = this.getWiringService();

		wiringService.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(DictionaryService.class).in(Service.class).to(GermanDictionayService.class); 
			}

		});
		
		DefaultWiringModelParser  parser = new DefaultWiringModelParser();

		BeanDependencyModel model = new BeanDependencyModel(GermanDictionayService.class);
		parser.readBeanModel(GermanDictionayService.class, model);


		assertFalse("object is not qualified" , model.getParams().isEmpty());

		GermanDictionayService ge = wiringService.getInstance(GermanDictionayService.class);
		assertNotNull(ge);


		ParamsMap params = new ParamsMap().setParam("name", "en");

		DictionaryService eDic = wiringService.getInstance(DictionaryService.class, params);

		assertNotNull(eDic);
		assertEquals("en", eDic.getLang());

		model = new BeanDependencyModel(DictionaryUser.class);
		parser.readBeanModel(DictionaryUser.class, model);


		assertNotNull(model.getProducingWiringPoint());
		assertNotNull(model.getProducingWiringPoint().getParamsSpecifications().getFirst());
		assertFalse("construtor injection point as no params", model.getProducingWiringPoint().getParamsSpecifications().getFirst().getParams().isEmpty());

		DictionaryUser user = wiringService.getInstance(DictionaryUser.class);

		assertNotNull(user);
		assertEquals("ge", user.getLang());

	}

	@Test
	public void testWiringServiceWithProfileProduction(){


		final WiringService wiringService = this.getWiringService();


		wiringService.getActiveProfiles().add("production");

		wiringService.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(DictionaryService.class).in(Service.class).to(GreekDictionayService.class); // production
				binder.bind(DictionaryService.class).in(Service.class).to(GermanDictionayService.class);
			}

		});


		DictionaryService eDic = wiringService.getInstance(DictionaryService.class);

		assertEquals("gr", eDic.getLang());

	}

	@Test
	public void testWiringServiceWithProfileBindDefinition(){


		final WiringService wiringService = this.getWiringService();

		wiringService.getActiveProfiles().add("dev");

		wiringService.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(DictionaryService.class).profiled("dev").in(Service.class).toInstance(new HashDictionaryService("un")); //dev
				binder.bind(DictionaryService.class).profiled("prod").in(Service.class).toInstance(new HashDictionaryService("kg"));
			}

		});

		DictionaryService eDic = wiringService.getInstance(DictionaryService.class);

		assertEquals("un", eDic.getLang());


	}


	@Test
	public void serviceTest(){

		// obtain the service it self from wiring

		WiringService wiringService = this.getWiringService();

		WiringService inj1 =  wiringService.getInstance(WiringService.class);

		assertNotNull(inj1);

		// the service is shared so its the same always
		WiringService inj2 =  wiringService.getInstance(WiringService.class);

		assertSame(inj1 , inj2);



	}

	@Test
	public void sharedInstanceTest(){
		final MockDisplay md = new MockDisplay();

		final WiringService wiringService = this.getWiringService();

		wiringService.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(Greeter.class).in(Shared.class).to(Greeter.class);
				binder.bind(Displayer.class).in(Shared.class).toInstance(md);
				binder.bind(Message.class).in(Shared.class).to(HelloMessage.class);
				binder.bindProperty(String.class).labeled("hello.message").toInstance("Hello");
				binder.bindProperty(String.class).labeled("hello.name").toInstance("World");
			}

		});

		Greeter g1 = wiringService.getInstance(Greeter.class);
		Greeter g2 = wiringService.getInstance(Greeter.class);

		assertTrue(g1==g2);
	}

	@Test
	public void cyclicTest(){

		final WiringService wiringService = this.getWiringService();

		wiringService.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(Greeter.class).in(Shared.class).to(Greeter.class);
				binder.bind(Displayer.class).in(Shared.class).to(CyclicDisplayer.class);
				binder.bind(Message.class).in(Shared.class).to(HelloMessage.class);
				binder.bindProperty(String.class).labeled("hello.message").toInstance("Hello");
				binder.bindProperty(String.class).labeled("hello.name").toInstance("World");
			}

		});

		Greeter g = wiringService.getInstance(Greeter.class);
		g.sayHello();
	}

	@Test
	public void cyclicSharedTest(){

		final WiringService wiringService = this.getWiringService();

		wiringService.addConfiguration(new BindConfiguration(){


			@Override
			public void configure(Binder binder) {
				binder.bind(Greeter.class).in(Shared.class).to(Greeter.class);
				binder.bind(Displayer.class).in(Shared.class).to(CyclicDisplayer.class);
				binder.bind(Message.class).in(Shared.class).to(HelloMessage.class);
				binder.bindProperty(String.class).labeled("hello.message").toInstance("Hello");
				binder.bindProperty(String.class).labeled("hello.name").toInstance("World");
			}

		});

		Greeter g = wiringService.getInstance(Greeter.class);
		g.sayHello();

	}

}
