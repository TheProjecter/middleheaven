/**
 * 
 */
package org.middleheaven.io.repository.upload;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.middleheaven.io.repository.empty.EmptyFileRepository;

/**
 * 
 */
public class CommonsUploadFilesContextResolver implements
		UploadFilesContextResolver {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UploadFilesContext resolve(HttpServletRequest request) {
		if (ServletFileUpload.isMultipartContent(request)){
			Map<String,String[]> parameters = new HashMap<String,String[]>();
			
			return new  UploadFilesContext(UploadManagedFileRepository.newInstance(request, parameters), parameters);
		} else {
			@SuppressWarnings("unchecked") // Servlet API  Specifies Map<String, String[]>
			final Map<String, String[]> parameterMap = request.getParameterMap();
			return new UploadFilesContext (EmptyFileRepository.repository(), parameterMap);
		}
		
	}

}
