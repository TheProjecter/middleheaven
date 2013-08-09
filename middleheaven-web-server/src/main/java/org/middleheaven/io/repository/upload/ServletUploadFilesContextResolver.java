/**
 * 
 */
package org.middleheaven.io.repository.upload;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.empty.EmptyFileRepository;

/**
 * 
 */
public class ServletUploadFilesContextResolver implements
		UploadFilesContextResolver {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UploadFilesContext resolve(HttpServletRequest request) {
		try {
			if (request.getParts().size() > 0){
				return new UploadFilesContext (ServletRequestPartFileRepository.newInstance(request), request.getParameterMap()); 
			} else {
				return new UploadFilesContext (EmptyFileRepository.repository(), request.getParameterMap());
			}		
		} catch (IOException e) {
			throw ManagedIOException.manage(e);
		} catch (ServletException e) {
			throw new ManagedIOException(e);
		}
	}

}
