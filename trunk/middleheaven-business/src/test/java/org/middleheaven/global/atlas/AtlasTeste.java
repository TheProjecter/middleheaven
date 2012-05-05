package org.middleheaven.global.atlas;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.Locale;

import org.junit.Test;
import org.middleheaven.global.atlas.modules.DefaultAtlasModule;
import org.middleheaven.global.atlas.modules.ISOFileAtlasModule;


public class AtlasTeste {

	@Test
	public void testDefaultAtlasModule(){
		
		ChronologicalCountryBuilder context = new ChronologicalCountryBuilder();

		DefaultAtlasModule m = new DefaultAtlasModule();
		m.loadAtlas(context);
		
		context.build();
		
		assertEquals(Locale.getISOCountries().length, context.countries().size());
	}
	
	@Test
	public void testISOFileAtlasModule(){
		
		ChronologicalCountryBuilder context = new ChronologicalCountryBuilder();

		ISOFileAtlasModule m = new ISOFileAtlasModule();
		m.loadAtlas(context);
		
		context.build();
		
		assertEquals(246,  context.countries().size());
		
		Country brazil = context.get("BR");
		assertEquals(27, brazil.getChildren().size());
		
		assertNotNull(brazil.getChild("MG"));
		
		assertEquals(263,brazil.getChild("MG").getChildren().size());
		
		Country portugal = context.get("PT");
		assertEquals(20, portugal.getChildren().size());
		
		assertEquals(39,portugal.getChild("01").getChildren().size());
		
		assertTrue(portugal.getCurrentCurrency().toString().equals("EUR"));
		assertTrue(brazil.getCurrentCurrency().toString().equals("BRL"));
		
		assertTrue(portugal.getLanguage().equals("pt"));
		assertTrue(brazil.getLanguage().equals("pt"));
	}
	
	@Test
	public void testISOFileAtlasModlueLoadFromPath(){
		URL url = this.getClass().getResource(".");
		System.setProperty("middleheaven.atlas.data.path" , new File("./src/test/java/org/middleheaven/test/atlas").getAbsolutePath());
		
		ChronologicalCountryBuilder context = new ChronologicalCountryBuilder();

		
		ISOFileAtlasModule m = new ISOFileAtlasModule();
		m.loadAtlas(context);
		
		context.build();
		
		assertEquals(246,  context.countries().size());
		
		// No file present but loads from embedded
		Country portugal = context.get("PT");
		assertEquals(20, portugal.getChildren().size());
		
		Country brazil = context.get("BR");
		assertEquals(27, brazil.getChildren().size());
		
		// file present at filsystem location with a dummy state to make 58
		// 57 means the data from the embeded version is being red witch is an error
		Country usa = context.get("US");
		assertEquals(59, usa.getChildren().size());
		
	
		
		System.setProperty("middleheaven.atlas.data.path", "");
	}
}
