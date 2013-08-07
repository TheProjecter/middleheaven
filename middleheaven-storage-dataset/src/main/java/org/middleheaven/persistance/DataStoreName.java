/**
 * 
 */
package org.middleheaven.persistance;

import org.middleheaven.util.Hash;

/**
 * 
 */
public final class DataStoreName {

	
	private String name;
	
	/**
	 * store:/provider/name
	 * 
	 * @param provider
	 * @param name
	 * @return
	 */
	public static DataStoreName name(String name) {
		return new DataStoreName(name);
	}
	
	private DataStoreName(String name) {
		super();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int hashCode(){
		return Hash.hash(name).hashCode();
	}
	
	public boolean equals(Object other){
		return (other instanceof DataStoreName) && ((DataStoreName)other).name.equals(this.name);
	}
	
}
