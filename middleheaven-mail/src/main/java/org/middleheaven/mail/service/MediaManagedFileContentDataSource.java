/**
 * 
 */
package org.middleheaven.mail.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

import org.middleheaven.io.StreamableContent;

/**
 * 
 */
class MediaManagedFileContentDataSource implements DataSource {

	
	private StreamableContent content;
	private String name;

	public MediaManagedFileContentDataSource (String name, StreamableContent content){
		this.content = content;
		this.name = name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getContentType() {
		return content.getContentType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		return content.getInputStream();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OutputStream getOutputStream() throws IOException {
		return content.getOutputStream();
	}

}
