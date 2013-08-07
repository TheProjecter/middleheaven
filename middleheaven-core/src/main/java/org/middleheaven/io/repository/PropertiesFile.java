/**
 * 
 */
package org.middleheaven.io.repository;

import java.io.IOException;
import java.util.Properties;

import org.middleheaven.io.ManagedIOException;

/**
 * 
 */
public final class PropertiesFile extends ManagedFileDecorator {
	
	/**
	 * @param file
	 * @return
	 */
	public static PropertiesFile from(ManagedFile file) {
		return new PropertiesFile(file);
	}
	
	/**
	 * Constructor.
	 * @param original
	 */
	private PropertiesFile(ManagedFile original) {
		super(original);
	}

	
	public Properties toProperties() throws ManagedIOException{

		try {
			Properties p = new Properties();
			p.load(this.getContent().getInputStream());
			
			return p;
		}  catch (IOException e) {
			throw ManagedIOException.manage(e);
		}
	}



}
