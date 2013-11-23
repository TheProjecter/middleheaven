/**
 * 
 */
package org.middleheaven.process.web.server;

import org.middleheaven.culture.Culture;
import org.middleheaven.process.web.HttpCookieWriter;
import org.middleheaven.process.web.HttpEntry;
import org.middleheaven.process.web.HttpProcessIOException;
import org.middleheaven.process.web.HttpResponse;
import org.middleheaven.process.web.HttpStatusCode;

/**
 * 
 */
public interface HttpServerResponse extends HttpResponse {

	public void setEntry(HttpEntry entry);
	
	public HttpEntry getEntry();

	public void setCulture(Culture culture);

	public boolean isCommitted();
	
	public void reset();

	public void sendError(HttpStatusCode sc, String msg) throws HttpProcessIOException ;


	public void sendError(HttpStatusCode sc) throws HttpProcessIOException;
	

	public void setStatus(HttpStatusCode sc);

	public HttpCookieWriter getHttpCookieWriter();

	

}
