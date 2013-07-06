package org.middleheaven.io.repository;

import org.middleheaven.io.StreamableContent;

/**
 * A {@link ManagedFileContent} that can inform the content type of the containing information.
 * 
 */
public interface MediaStreamableContent extends StreamableContent {

	/**
	 * 
	 * @return the MIME type for the stream content
	 */
	public String getContentType();
	
	/**
	 * Set the MIME type for this file contents.
	 * This operation is optional. Classes that do not support it must fail silently
	 * 
	 * @param contentType the MIME type for this file contents
	 */
	public void setContentType(String contentType);

}
