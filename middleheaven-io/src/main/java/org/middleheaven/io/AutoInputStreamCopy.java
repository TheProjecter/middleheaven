/**
 * 
 */
package org.middleheaven.io;

import java.io.InputStream;

/**
 * Marker interface that means the implementer can copy it selft from the given stream
 */
public interface AutoInputStreamCopy {

	
	public void autoCopyFrom(InputStream stream);
}
