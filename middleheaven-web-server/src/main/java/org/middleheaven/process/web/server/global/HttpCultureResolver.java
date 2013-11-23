package org.middleheaven.process.web.server.global;

import org.middleheaven.culture.Culture;
import org.middleheaven.process.web.server.HttpServerContext;

/**
 * Determines what is the culture associated with the HTTP request
 * 
 */
public interface HttpCultureResolver {

	public Culture resolveFrom(HttpServerContext request);
	
}
