package org.middleheaven.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.middleheaven.collections.CollectionUtils;

public class StringUtilsTest {

	@Test
	public void testRepeat(){
		assertEquals("mmm", StringUtils.repeat("m", 3));
		assertEquals("acacac", StringUtils.repeat("ac", 3));
		
		assertEquals("ac:ac:ac", StringUtils.repeat("ac", 3, ":"));
	}
	
	
	@Test
	public void testIffenDelimiter(){
		assertEquals("abBcCd", StringUtils.iffenDelimitedToCamelCase("ab-bc-cd"));
	}
	
	@Test
	public void testFirstLetterToUppercase(){
		assertEquals("middleHeaven", StringUtils.firstLetterToLower("MiddleHeaven"));
		assertEquals("MiddleHeaven", StringUtils.firstLetterToUpper("middleHeaven"));
	}
	
	@Test
	public void testSimplePattern(){
		
		String phrase = "MiddleHeaven is the best framework";
		
		assertTrue(StringUtils.simplePatternMatch("*framework", phrase));
		assertTrue(StringUtils.simplePatternMatch("MiddleHeaven*", phrase));
		assertTrue(StringUtils.simplePatternMatch("*best*", phrase));
	}
	
	@Test
	public void testSplitter(){
		
		String phrase = "a=2, b = 4,, f = 67";
		
		List<String> res = Splitter.on(",").split(phrase).into(new ArrayList<String>());
		
		assertTrue("Not the same : "  + res , CollectionUtils.equalContents(res, Arrays.asList("a=2", " b = 4" ,"", " f = 67")));
		
		res = Splitter.on(",").trim().split(phrase).into(new ArrayList<String>());
		
		assertTrue("Not the same : "  + res , CollectionUtils.equalContents(res, Arrays.asList("a=2", "b = 4" , "f = 67")));
		
		Map<String, String> map = Splitter.on(",").trim().withKeyValueSeparator("=").split(phrase).into(new HashMap<String,String>());
		
		Map<String, String> mapAssert = new HashMap<String, String>();
		
		mapAssert.put("a", "2");
		mapAssert.put("b ", " 4");
		mapAssert.put("f ", " 67");
		
		assertTrue("Not the same : "  + map , CollectionUtils.equalContents(map, mapAssert));
		
		map = Splitter.on(",").trim().withKeyValueSeparator("=").trim().split(phrase).into(new HashMap<String,String>());
		
		mapAssert = new HashMap<String, String>();
		
		mapAssert.put("a", "2");
		mapAssert.put("b", "4");
		mapAssert.put("f", "67");
		
		assertTrue("Not the same : "  + map , CollectionUtils.equalContents(map, mapAssert));
		
	}

}
