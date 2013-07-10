package org.middleheaven.process.web.server.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A simple filter that compresses the contente writen out in GZip format.
 */
public class GZipFilter extends AbstractFilter {

	/**
	 * 
	 * {@inheritDoc}
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
			
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			
			String acceptEncoding = httpRequest.getHeader("accept-encoding");
			if (acceptEncoding != null && acceptEncoding.contains("gzip")) {

				httpResponse.addHeader("Accept-Encoding", "gzip");
				httpResponse.addHeader("Vary", "Accept-Encoding");
				
				GZipResponseWrapper wrappedResponse = new GZipResponseWrapper(httpResponse);

				chain.doFilter(request, wrappedResponse);
				wrappedResponse.finishResponse();

				return;
			}
		}
		chain.doFilter(request, response);

	}

}
