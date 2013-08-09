package org.middleheaven.io.repository.upload;

import javax.servlet.http.HttpServletRequest;

import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.logging.Logger;


public final class UploadFilesRequestFactory {

	private static final UploadFilesRequestFactory me = new UploadFilesRequestFactory();
	
	private UploadFilesContextResolver resolver;
	private UploadFilesRequestFactory (){}

	public static UploadFilesRequestFactory getInstance(){
		return me;
	}
	
	public  UploadFilesContext getContext(HttpServletRequest request) {
		if (resolver == null){
			if (request.getSession().getServletContext().getMajorVersion() >= 3){
				resolver = new ServletUploadFilesContextResolver();
			} else if (ClassIntrospector.isInClasspath("org.middleheaven.io.repository.upload.CommonsUploadFilesContextResolver")){
				resolver = (UploadFilesContextResolver)ClassIntrospector.loadFrom("org.middleheaven.io.repository.upload.CommonsUploadFilesContextResolver").newInstance();
			} else {
				Logger.onBookFor(this.getClass()).error("No UploadFilesContextResolver found. Consider using middleheaven-web-commonsupload for servlet api previous to 3.0");
				resolver = new EmptyUploadFilesContextResolver();
			}
		}
		
		return resolver.resolve(request);
		
	}

}
