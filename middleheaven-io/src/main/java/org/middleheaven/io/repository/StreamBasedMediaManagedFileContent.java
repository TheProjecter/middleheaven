package org.middleheaven.io.repository;

import org.middleheaven.io.StreamableContent;


public abstract class StreamBasedMediaManagedFileContent  implements StreamableContent {

	private String contentType = "octet-stream";

	public StreamBasedMediaManagedFileContent() {

	}

	@Override
	public String getContentType() {
		return this.contentType;
	}

	@Override
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReadable() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isContentTypeReadable() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWritable() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isContentTypeWritable() {
		return true;
	}





}
