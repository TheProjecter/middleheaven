/**
 * 
 */
package org.middleheaven.persistance;

/**
 * 
 */
public class DataStoreNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1624645336240860893L;

	public DataStoreNotFoundException(String message){
		super(message);
	}
}
