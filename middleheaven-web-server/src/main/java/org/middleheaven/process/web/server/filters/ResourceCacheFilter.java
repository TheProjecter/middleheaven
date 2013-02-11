package org.middleheaven.process.web.server.filters;

import static org.middleheaven.util.SafeCastUtils.safeCast;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class ResourceCacheFilter extends AbstractFilter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		chain.doFilter(request, response);
		HttpServletResponse httpResponse = safeCast(response, HttpServletResponse.class).get();
		
		httpResponse.addHeader("Expires", "Thu, 1 Jan 2099 23:59:59 GMT");
		httpResponse.addHeader("Cache-Control", "max-age=2592000"); // one month
		httpResponse.addHeader("Last-Modified", "Thu, 1 Jan 1970 00:00:00 GMT");

	}

}
