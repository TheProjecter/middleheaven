/**
 * 
 */
package org.middleheaven.io.repository;

import java.io.IOException;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.middleheaven.io.ManagedIOException;

/**
 * 
 */
public class JarFile extends ManagedFileDecorator {

	/**
	 * @param file
	 * @return
	 */
	public static JarFile from(ManagedFile file) {
		return new JarFile(file);
	}
	
	/**
	 * Constructor.
	 * @param file
	 */
	private JarFile(ManagedFile original) {
		super(original);
	}
	
	public Manifest getManifest(){
		JarInputStream jis = null;
		ManagedIOException ex= null;
		Manifest manifest = null;
		try {
			jis = new JarInputStream(this.getContent().getInputStream());
			manifest = jis.getManifest();
		} catch (IOException e) {
			ex = ManagedIOException.manage(e);
		} finally{
			if (jis != null){
				try {
					jis.close();
				} catch (IOException e) {
					if (ex != null){
						throw ex;
					}
					throw ManagedIOException.manage(e);
				}
			}
		}
		return manifest;
	}
}
