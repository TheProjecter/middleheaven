package org.middleheaven.process.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.middleheaven.culture.Culture;


public class TestHttpProcessingUtils {

	
	@Test
	public void testProcesingUtils(){
		
		
		List<Culture> cultures = HttpProcessingUtils.acceptableLanguages("da , pt, en ; q=0.7, en-gb ; q=0.8");
		
		assertEquals(4, cultures.size());
		
		assertEquals(0,cultures.indexOf(Culture.valueOf("da")));
		assertEquals(1,cultures.indexOf(Culture.valueOf("pt")));
		assertEquals(2,cultures.indexOf(Culture.valueOf("en", "GB")));
		assertEquals(3,cultures.indexOf(Culture.valueOf("en")));
		
	}
	
	
	@Test
	public void testGlobPattern(){
		
		GlobUrlPattern a = new GlobUrlPattern("*.html");
		GlobUrlPattern b = new GlobUrlPattern("/distribution/*.html");
		
		final String url = "/distribution/login.html";

		assertTrue(a.match(url) > 0);
		assertTrue(b.match(url) > 0);
	
		assertTrue(a.match(url) < b.match(url));
	}
}
