/**
 * 
 */
package org.middleheaven.io;

import java.io.Closeable;
import java.io.IOException;

import org.middleheaven.util.function.Block;
import org.middleheaven.util.function.Function;

/**
 * 
 */
public final class IO {

	
	public static <S extends Closeable> void using(S in , Block<S> block){
		try {
			
			if (in != null){
				block.apply(in);
			}
		} finally{
			try {
				if (in != null){
					in.close();
				}
			} catch (IOException e) {
				//no-op
			}
		}
	}
	
	public static <T,S extends Closeable> T using(S in , Function<T, S> block){
		try {
			
			if (in != null){
				return block.apply(in);
			}
			return null;
		} finally{
			try {
				if (in != null){
					in.close();
				}
			} catch (IOException e) {
				throw  ManagedIOException.manage(e);
			}
		}
	}
	
	private IO(){}
}
