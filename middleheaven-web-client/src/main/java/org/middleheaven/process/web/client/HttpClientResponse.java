/**
 * 
 */
package org.middleheaven.process.web.client;

import org.middleheaven.culture.Culture;
import org.middleheaven.process.AttributeContext;
import org.middleheaven.process.web.HttpCookieReader;
import org.middleheaven.process.web.HttpEntry;
import org.middleheaven.process.web.HttpStatusCode;


/**
 * 
 */
public interface HttpClientResponse extends org.middleheaven.process.web.HttpResponse {

	/**
	 * Context limited to Cookies and Headers scopes.
	 * @return
	 */
	public AttributeContext getAttributeContext();


	public HttpEntry getEntry();

	public Culture getCulture();

	public HttpStatusCode getStatusCode();

	// TODO CookieAttribute vs name-value Attribute
	public HttpCookieReader getHttpCookieReader();


}
