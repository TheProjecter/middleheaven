package org.middleheaven.process.web.server;

import static org.middleheaven.util.SafeCastUtils.safeCast;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.process.web.server.filters.AbstractFilter;

public class FrontEndControlFilter extends AbstractFilter{


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		try {
			HttpServletRequest hRequest = safeCast(request, HttpServletRequest.class).get(); 
			HttpServletResponse hResponse = safeCast(response, HttpServletResponse.class).get();
			try{

				ServletHttpServerService serverService = (ServletHttpServerService) this.getFilterConfig().getServletContext().getAttribute("httpService");
			
				if (serverService == null){
					throw new ServletException("HTTPServerService not found in contexts");
				}
				
				serverService.processRequest(hRequest, hResponse);
				

			} catch (ClassCastException e){
				// this servlet can only work with this specific implementation of HTTPServerService
				throw new ServletException("HTTPServerService not compatible with generic Servlet Container");
			} catch (RuntimeException t){
				this.getFilterConfig().getServletContext().log("Unexpected Exception", t);
				t.printStackTrace();
			}
			
		} catch (ClassCastException e){
			this.getFilterConfig().getServletContext().log("Cannot process non HTTP request/response", e);
			chain.doFilter(request, response);
		}
		
		
		
	}



}
