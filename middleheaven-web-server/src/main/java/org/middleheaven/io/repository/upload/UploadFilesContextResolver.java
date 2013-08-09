/**
 * 
 */
package org.middleheaven.io.repository.upload;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 */
public interface UploadFilesContextResolver {

	
	public UploadFilesContext resolve(HttpServletRequest request);
}
