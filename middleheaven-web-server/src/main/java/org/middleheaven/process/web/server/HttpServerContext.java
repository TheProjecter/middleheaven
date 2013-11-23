package org.middleheaven.process.web.server;

import java.net.InetAddress;
import java.util.List;

import org.middleheaven.aas.Subject;
import org.middleheaven.culture.Culture;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.process.AttributeContext;
import org.middleheaven.process.web.HttpChannel;
import org.middleheaven.process.web.HttpMethod;
import org.middleheaven.process.web.HttpUrl;
import org.middleheaven.process.web.HttpUserAgent;

/**
 * A context that is scoped inside an HTTP request.
 */
public interface HttpServerContext  {

	/**
	 * allows access to the current state attributes context.
	 * 
	 * @return the {@link AttributeContext}.
	 */
	public AttributeContext getAttributes();
	
	/**
	 * The list of {@link Culture}s accepted by the user agent. 
	 * The list is ordered according to rules of the Accept-Language HTTP header.
	 * @return a list of accepted {@link Culture}s.
	 */
	public List<Culture> getCultures();
	
	/**
	 * The most likely {@link Culture} accepted by the user agent. 
	 * Selects the most preferable {@link Culture} according to the rules of the Accept-Language HTTP header.
	 * 
	 * @return The most likely {@link Culture} accepted by the user agent.
	 */
	public Culture getCulture();
	
	/**
	 * the current {@link HttpChannel}.
	 * @return the current communication channel.
	 */
	public HttpChannel getHttpChannel();

	/**
	 * 
	 * @return an {@link HttpUrl} object with the current request url. 
	 */
	public HttpUrl getRequestUrl();
	
	/**
	 * The URL of the Referer. The Referer is a page that linked to this one.
	 * 
	 * @return the origin of the request.
	 */
	public HttpUrl getRefererUrl();

	
	/**
	 * The {@link HttpMethod} called from the user agent.
	 * @return the {@link HttpMethod} that being called from the user agent.
	 */
	public  HttpMethod getRequestMethod();

	/**
	 * 
	 * @return The current authenticated {@link Subject} or <code>null</code> if none exists.
	 */
	public Subject getAuthenticatedSubject();
	
	/**
	 * 
	 * @return The server response.
	 */
	public HttpServerResponse getResponse();
	
	/**
	 * Information on the user agent that made the request.
	 * @return a {@link HttpUserAgent} object with the current user agent. 
	 */
	public  HttpUserAgent getAgent();
	
	/**
	 * Access the upload repository for files uploaded in this request.
	 * 
	 * @return the upload repository.
	 */
	public ManagedFileRepository getUploadRepository();
	
	/**
	 * 
	 * @return the web container deploy context.
	 */
	public String getContextPath();

	/**
	 * 
	 * @return the remote caller's origin address or <code>null</code> if its not available.
	 */
	public InetAddress getRemoteAddress();
}
