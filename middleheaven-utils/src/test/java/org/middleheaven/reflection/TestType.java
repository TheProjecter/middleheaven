/**
 * 
 */
package org.middleheaven.reflection;

/**
 * 
 */
public class TestType {
	
	private String a;
	private Integer i;
	
	public TestType (String a, Integer i){
		this.a = a;
		this.i = i;
	}
	
	public String test(){
		return a + i;
	}
}
