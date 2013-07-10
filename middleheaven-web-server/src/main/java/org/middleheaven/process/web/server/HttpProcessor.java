package org.middleheaven.process.web.server;

import org.middleheaven.process.web.HttpProcessException;
import org.middleheaven.process.web.UrlPattern;

/**
 * Process an HTTP (HiperText Transport Protocol) request.
 * 
 * One or more {@link HttpProcessor} are registered with the {@link HttpServerService} to process
 * requests directed at specific URL address in the server. 
 * 
 * @see  HttpServerService#registerHttpProcessor(String, HttpProcessor, UrlPattern)
 */
public interface HttpProcessor  {

	/**
	 * Called once after the objet is registered with the {@link HttpServerService}.
	 * @param config
	 */
	public void init(HttpProcessorConfig config);
	
	/**
	 * Process the request 
	 * @param context the invocation context.
	 * 
	 * @throws HttpProcessException if something goes wrong.
	 * @return the process outcome.
	 */
	public Outcome process(HttpServerContext context) throws HttpProcessException;
}
