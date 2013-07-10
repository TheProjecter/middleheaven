package org.middleheaven.process.web.server.filters;

import static org.middleheaven.util.SafeCastUtils.safeCast;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class ResourceCacheFilter extends AbstractFilter {

	private static final int ONE_MONTH_IN_MILISECOUNDS = 30*24*3600*1000;
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		chain.doFilter(request, response);
		
		HttpServletResponse httpResponse = safeCast(response, HttpServletResponse.class).get();
		
		final long currentTimeMillis = System.currentTimeMillis();
		httpResponse.setDateHeader("Last-Modified", currentTimeMillis);
		httpResponse.setDateHeader("Expires", currentTimeMillis + ONE_MONTH_IN_MILISECOUNDS);
			
		
		
	}

}
