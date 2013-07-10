/**
 * 
 */
package org.middleheaven.process.web;

import java.io.PrintWriter;
import java.io.Writer;

import org.middleheaven.io.StreamableContentSource;
import org.middleheaven.io.IOTransport;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.MediaStreamableContent;

/**
 * 
 */
public class MediaManagedFileContentHttpEntry implements HttpEntry {

	
	private MediaStreamableContent content;

	public MediaManagedFileContentHttpEntry(MediaStreamableContent content){
		this.content = content;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyTo(StreamableContentSource other) throws ManagedIOException {
		IOTransport.copy(content).to(other);	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getContentEncoding() {
		return content.getContentType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRepeatable() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isStreaming() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChunked() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MediaStreamableContent getContent() {
		return content;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Writer getContentWriter() {
		return new PrintWriter(content.getOutputStream());
	}

}
