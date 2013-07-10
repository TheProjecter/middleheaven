/**
 * 
 */
package org.middleheaven.process.web;

import java.io.Writer;

import org.middleheaven.io.StreamableContentSource;
import org.middleheaven.io.repository.MediaStreamableContent;

/**
 * 
 */
public interface HttpEntry extends StreamableContentSource {

	public String getContentEncoding();
	
	public boolean isRepeatable();
	
	public boolean isStreaming();
	
	public boolean isChunked();
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public MediaStreamableContent getContent();

	/**
	 * @return
	 */
	public Writer getContentWriter();

}
