/**
 * 
 */
package org.middleheaven.mail.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

import org.middleheaven.io.repository.MediaStreamableContent;

/**
 * 
 */
class MediaManagedFileContentDataSource implements DataSource {

	
	private MediaStreamableContent content;
	private String name;

	public MediaManagedFileContentDataSource (String name, MediaStreamableContent content){
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
