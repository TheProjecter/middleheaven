/**
 * 
 */
package org.middleheaven.io;


/**
 * 
 */
public abstract class MediaStreamableContentAdapter extends StreamableContentAdapter {


	private String contentType;

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
	public String getContentType() {
		return contentType;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isContentTypeWritable() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setContentType(String contentType) throws ManagedIOException {
		this.contentType = contentType;
	}

}
