/**
 * 
 */
package org.middleheaven.io.repository.upload;

import javax.servlet.http.HttpServletRequest;

import org.middleheaven.io.repository.empty.EmptyFileRepository;

/**
 * 
 */
public class EmptyUploadFilesContextResolver implements UploadFilesContextResolver{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UploadFilesContext resolve(HttpServletRequest request) {
		return new UploadFilesContext (EmptyFileRepository.repository(), request.getParameterMap());
	}

}
