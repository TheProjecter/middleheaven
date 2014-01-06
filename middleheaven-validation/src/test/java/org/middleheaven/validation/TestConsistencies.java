package org.middleheaven.validation;

import java.util.Collections;

import org.junit.Test;
import org.middleheaven.reflection.NoSuchClassReflectionException;
import org.middleheaven.validation.Consistencies;


public class TestConsistencies {


	@Test(expected=IllegalArgumentException.class)
	public void testDinamicCompleteExceptionInvocation(){

		Consistencies.consistNotNull(null, "Illegal",IllegalArgumentException.class);
		
	}
	
	@Test(expected=NoSuchClassReflectionException.class)
	public void testDinamicExceptionOnlyInvocation(){
		Consistencies.consistNotNull(null,NoSuchClassReflectionException.class);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDinamicSimplerExceptionInvocation(){

		Consistencies.consistNotNull(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNotEmptyInvocationNull(){

		Consistencies.consistNotEmpty(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNotEmptyInvocationString(){
		Consistencies.consistNotEmpty("");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNotEmptyInvocationCollection(){
		Consistencies.consistNotEmpty(Collections.emptyList());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNotEmptyInvocationMap(){
		Consistencies.consistNotEmpty(Collections.emptyMap());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNotEmptyInvocationArray(){
		Consistencies.consistNotEmpty(new Object[0]);
	}
	
	@Test
	public void testNotEmptyInvocationArrayFail(){
		Consistencies.consistNotEmpty(new Object[]{"Teste"});
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBetwenInvocationArray(){
		Consistencies.consistIsBetween(0, 5, 10);
	}
}
