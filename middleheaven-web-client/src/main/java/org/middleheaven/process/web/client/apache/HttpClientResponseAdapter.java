/**
 * 
 */
package org.middleheaven.process.web.client.apache;

import org.apache.http.HttpResponse;
import org.middleheaven.culture.Culture;
import org.middleheaven.process.AttributeContext;
import org.middleheaven.process.web.HttpCookieReader;
import org.middleheaven.process.web.HttpEntry;
import org.middleheaven.process.web.HttpStatusCode;
import org.middleheaven.process.web.client.HttpClientResponse;

/**
 * 
 */
public class HttpClientResponseAdapter implements HttpClientResponse  {

	private HttpResponse apacheResponse;

	/**
	 * Constructor.
	 * @param apacheResponse
	 */
	public HttpClientResponseAdapter(HttpResponse apacheResponse) {
		this.apacheResponse = apacheResponse;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpEntry getEntry() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Culture getCulture() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributeContext getAttributeContext() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpCookieReader getHttpCookieReader() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpStatusCode getStatusCode() {
		return HttpStatusCode.valueOf(apacheResponse.getStatusLine().getStatusCode());
	}



}
