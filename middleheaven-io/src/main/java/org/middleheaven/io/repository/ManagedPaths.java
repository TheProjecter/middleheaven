/**
 * 
 */
package org.middleheaven.io.repository;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.util.Splitter;

/**
 * 
 */
public class ManagedPaths {

	
	public static ManagedFilePath path(ManagedFileRepository repository , CharSequence path){
		Enumerable<String> result = Splitter.on('/').split(path);
		
		return new ArrayManagedFilePath(repository, result.getFirst(), result.skip(1).asArray());
	}
}
