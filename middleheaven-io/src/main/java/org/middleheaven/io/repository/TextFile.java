/**
 * 
 */
package org.middleheaven.io.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.util.function.Block;

/**
 * {@link ManagedFile} decorator that provides text specific methods. Useful to handle text files.
 */
public class TextFile extends ManagedFileDecorator {

	/**
	 * @param file
	 * @return
	 */
	public static TextFile from(ManagedFile file) {
		return new TextFile(file);
	}
	
	/**
	 * Constructor.
	 * @param file
	 */
	private TextFile(ManagedFile original) {
		super(original);
	}

	public StringBuilder getText(){
		BufferedReader reader = null;
		try{

			reader = new BufferedReader(new InputStreamReader(this.getContent().getInputStream()));

			StringBuilder builder = new StringBuilder();

			String line;
			while ((line = reader.readLine()) !=null){
				builder.append(line).append("\n");
			}

			return builder;


		} catch (IOException e){
			throw ManagedIOException.manage(e);
		} finally {
			if (reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					throw ManagedIOException.manage(e);
				}
			}
		}
	}
	
	public void eachLine (Block<String> walker){
		BufferedReader reader = null;
		try{

			reader = new BufferedReader(new InputStreamReader(this.getContent().getInputStream()));

		
			String line;
			while ((line = reader.readLine()) !=null){
				walker.apply(line);
			}

		} catch (IOException e){
			throw ManagedIOException.manage(e);
		} finally {
			if (reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					throw ManagedIOException.manage(e);
				}
			}
		}
	}

	
}
