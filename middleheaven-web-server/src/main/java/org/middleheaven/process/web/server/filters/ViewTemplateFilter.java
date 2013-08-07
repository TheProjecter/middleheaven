package org.middleheaven.process.web.server.filters;

import static org.middleheaven.util.SafeCastUtils.safeCast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.middleheaven.logging.Logger;
import org.middleheaven.process.web.server.PageResponseWrapper;
import org.middleheaven.web.rendering.Decorator;
import org.middleheaven.web.rendering.Page;

public class ViewTemplateFilter extends AbstractFilter {

	private static final String FILTER_APPLIED = "_filterApplied";


	private static final String USING_STREAM = "_usingStream";


	private static final String PAGE = "_page";



	public void doFilter(ServletRequest  rq, ServletResponse  rs, FilterChain chain) throws IOException , ServletException {

		HttpServletRequest  request = (HttpServletRequest ) rq;

		if (rq.getAttribute(FILTER_APPLIED) != null /*|| factory.isPathExcluded(extractRequestPath(request))*/) {
			// ensure that filter is only applied once per request
			chain.doFilter(rq, rs);
		} else {
			request.setAttribute(FILTER_APPLIED, Boolean.TRUE);

			// force creation of the session now because Tomcat  had problems with
			// creating sessions after the response had been committed
			//			if (Container.get() == Container.TOMCAT) {
			request.getSession(true);
			//			}
			HttpServletResponse  response = safeCast(rs, HttpServletResponse.class).get();

			// parse data into Page object (or continue as normal if Page not parseable)
			Page page = parsePage(request, response, chain);

			if (page != null) {
				page.setRequest(request);

				Decorator decorator = getDecorator(request, page);
				if (decorator != null && decorator.getPage() != null) {
					applyDecorator(page, decorator, request, response);
					return;
				}

				// if we got here, an exception occured or the decorator was null,
				// what we don't want is an exception printed to the user, so
				// we write the original page
				writeOriginal(request, response, page);
			}
		}
	}

	private Decorator getDecorator(HttpServletRequest request, Page page2) {
		// TODO implement ViewTemplateFilter.getDecorator
		return null;
	}

	protected void applyDecorator(Page page, Decorator decorator, HttpServletRequest  request, HttpServletResponse  response) throws ServletException, IOException  {
		try {
			request.setAttribute(PAGE, page);
			ServletContext context = getFilterConfig().getServletContext();
			// see if the URI path (webapp) is set
			if (decorator.getURIPath() != null) {
				// in a security conscious environment, the servlet container
				// may return null for a given URL
				if (context.getContext(decorator.getURIPath()) != null) {
					context = context.getContext(decorator.getURIPath());
				}
			}
			// get the dispatcher for the decorator
			RequestDispatcher dispatcher = context.getRequestDispatcher(decorator.getPage());
			// create a wrapper around the response
			dispatcher.include(request, response);

			// set the headers specified as decorator init params
//			while (decorator.getInitParameterNames().hasNext()) {
//				String  initParam = (String ) decorator.getInitParameterNames().next();
//				if (initParam.startsWith("header.")) {
//					response.setHeader(initParam.substring(initParam.indexOf('.')), decorator.getInitParameter(initParam));
//				}
//			}

			request.removeAttribute(PAGE);
		}
		catch (RuntimeException  e) {
			// added a print message here because otherwise Tomcat swallows
			// the error and you never see it = bad!
			//if (Container.get() == Container.TOMCAT)
			Logger.onBookFor(this.getClass()).error("Unexpected exception", e);

			throw new ServletException(e);
		}
	}

	private void writeOriginal(HttpServletRequest  request, HttpServletResponse  response, Page page) throws IOException  {
		response.setContentLength(page.getContentLength());
		if (request.getAttribute(USING_STREAM).equals(Boolean.TRUE))
		{
			PrintWriter  writer = new PrintWriter (response.getOutputStream());
			page.writePage(writer);
			//flush writer to underlying outputStream
			writer.flush();
			response.getOutputStream().flush();
		}
		else
		{
			page.writePage(response.getWriter());
			response.getWriter().flush();
		}
	}

	protected Page parsePage(HttpServletRequest  request, HttpServletResponse  response, FilterChain chain) throws IOException , ServletException {
		try {
			PageResponseWrapper pageResponse = new PageResponseWrapper(response);
			chain.doFilter(request, pageResponse);
			// check if another servlet or filter put a page object to the request
			Page result = (Page)request.getAttribute(PAGE);
			if (result == null) {
				// parse the page
				result = pageResponse.getPage();
			}
			request.setAttribute(USING_STREAM, Boolean.valueOf (pageResponse.isUsingStream()));
			return result;
		}catch (IllegalStateException  e) {
			// weblogic throws an IllegalStateException when an error page is served.
			// it's ok to ignore this, however for all other containers it should be thrown
			// properly.
			//if (Container.get() != Container.WEBLOGIC){
			Logger.onBookFor(this.getClass()).error("Unexpected exception", e);
			throw new ServletException(e);
			//}
			//return null;
		}
	}



	//	@Override
	//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	//
	//		MYHttpServletResponseWrapper myresponde = new MYHttpServletResponseWrapper((HttpServletResponse) response );
	//		
	//		filterConfig.getServletContext().getRequestDispatcher("/decorator/common.jsp").include(request, myresponde);
	//		
	//		chain.doFilter(request, myresponde);
	//
	//		// pega decorador correspondente
	//		IOUtils.copy(myresponde.getInputStream(), response.getOutputStream());
	//
	//	}

	private static class MYHttpServletResponseWrapper extends HttpServletResponseWrapper {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ServletOutputStream sout = new ServletOutputStream(){

			@Override
			public void write(int b) throws IOException {
				out.write(b);
			}

		};
		PrintWriter printer = new PrintWriter(sout);

		public MYHttpServletResponseWrapper(HttpServletResponse response) {
			super(response);
		}

		public InputStream getInputStream(){
			return new ByteArrayInputStream(out.toByteArray());
		}


		public PrintWriter getWriter() throws IOException{
			return printer;
		}

		public ServletOutputStream getOutputStream() throws IOException{
			return sout;
		}

		public void flushBuffer() throws IOException {
			sout.flush();
		}
	}




}
