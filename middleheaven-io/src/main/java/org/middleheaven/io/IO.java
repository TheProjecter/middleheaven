/**
 * 
 */
package org.middleheaven.io;

import java.io.Closeable;
import java.io.IOException;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.middleheaven.util.function.Block;
import org.middleheaven.util.function.Function;

/**
 * 
 */
public class IO {

	/**
	 * @param jarInputStream
	 * @param block
	 */
	public static  <S extends Closeable> void using(S in, Block<S> block) {
		ManagedIOException ex= null;
		try{
			if (in != null){
				block.apply(in);
			}
		} catch (ManagedIOException e ){
			ex = e;
		} finally {
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
					if (ex != null){
						throw ex;
					}
					throw ManagedIOException.manage(e);
				}
			}
		}
	}
	
	/**
	 * @param jarInputStream
	 * @param block
	 */
	public static  <T, S extends Closeable> T using(S in, Function<T, S> block) {
		ManagedIOException ex= null;
		T result= null;
		try{
			if (in != null){
				result = block.apply(in);
			}
		} catch (ManagedIOException e ){
			ex = e;
		} finally {
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
					if (ex != null){
						throw ex;
					}
					throw ManagedIOException.manage(e);
				}
			}
		}
		return result;
	}

}
