/**
 * 
 */
package org.middleheaven.process.web;

import java.io.Writer;

import org.middleheaven.io.StreamableContentSource;


/**
 * 
 */
public interface HttpEntry extends StreamableContentSource {

	public String getContentEncoding();
	public boolean isRepeatable();
	public boolean isStreaming();
	public boolean isChunked();
	
	/**
	 * @return
	 */
	public Writer getContentWriter();

}
