package org.middleheaven.web.rendering;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.process.web.CommonHttpServerContainers;
import org.middleheaven.process.web.HttpProcessIOException;
import org.middleheaven.process.web.server.Outcome;
import org.middleheaven.process.web.server.PageResponseWrapper;
import org.middleheaven.process.web.server.action.HttpProcessServletException;
import org.middleheaven.process.web.server.action.RequestResponseWebContext;
import org.middleheaven.util.StringUtils;
import org.middleheaven.web.container.WebContainerInfo;

public class DecoratorRenderingProcessorResolver extends AbstractJspProcessorResolver  {

	private final DecoratorRendingProcessor processor = new DecoratorRendingProcessor();
	private final Map<String, Decorator> decorators = new LinkedHashMap<String,Decorator>();
	
	private String decoratorPath;
	private String contentPath;
	
	
	public DecoratorRenderingProcessorResolver(){
		this("/WEB-INF/decorators", "/WEB-INF/view");
	}

	public DecoratorRenderingProcessorResolver(String decoratorPath, String contentPath){
		this.decoratorPath = decoratorPath;
		this.contentPath = contentPath;
	}

	
	
	public void setDecoratorPath(String decoratorPath) {
		this.decoratorPath = decoratorPath;
	}

	public void setContentPath(String contentPath) {
		this.contentPath = contentPath;
	}

	public void addPattern(String urlPattern, Decorator decorator){
		decorators.put(urlPattern, decorator);
	}
	
	private Decorator getDecorator(Outcome outcome, Page page) {
		
		for (Map.Entry<String, Decorator> entry : decorators.entrySet()){
			if (StringUtils.simplePatternMatch(entry.getKey(), outcome.getUrl())){
				return entry.getValue();
			}
		}
		return null;
	}
	
	private String realDecoratorPath(String url){
		return decoratorPath + "/" + stripExtention(url) + ".jsp";
	}

	private String realContentPath(String url){

		return contentPath + "/" + stripExtention(url) + ".jsp";
	}
	
	
	private String stripExtention (String name){
		int pos = name.lastIndexOf('.');
		if (pos == 0) {
			return "";
		} else if (pos > 0){
			return name.substring(0, pos);
		} else {
			return name;
		}
		
	}
	@Override
	public RenderingProcessor resolve(String url, String contentType) {
		return processor;
	}

	@Override
	public boolean canProcess(String url, String contentType) {
		return url.endsWith("html") || url.endsWith("htm") || "text/html".equalsIgnoreCase(contentType);
	}


	
	private class DecoratorRendingProcessor implements RenderingProcessor{


		private static final String FILTER_APPLIED = "_filter";

		private static final String USING_STREAM = "_using_Stream";

		public DecoratorRendingProcessor(){

		}

		@Override
		public void process(RequestResponseWebContext context, Outcome outcome, String contentType) {

			injectBrowserClient(context);
			
			HttpServletRequest  request  = context.getServletRequest();
			
			WebContainerInfo container = (WebContainerInfo) request.getSession().getServletContext().getAttribute(WebContainerInfo.ATTRIBUTE_NAME);
			
			// force creation of the session now because Tomcat  had problems with
			// creating sessions after the response had been committed
			if (CommonHttpServerContainers.TOMCAT.equals(container.getName())){
				request.getSession(true);
			}
	
			HttpServletResponse  response = context.getServletResponse();
			try{
				if (request.getAttribute(FILTER_APPLIED) != null /*|| factory.isPathExcluded(extractRequestPath(request))*/) {
					// ensure that filter is only applied once per request
					request.getRequestDispatcher(realContentPath(outcome.getUrl())).include(request, response);

				} else {
					request.setAttribute(FILTER_APPLIED, Boolean.TRUE);


					// parse data into Page object (or continue as normal if Page not parseable)
					Page page = parsePage(outcome, request, response);

					if (page != null) {
						page.setRequest(request);

						Decorator decorator = getDecorator(outcome, page);
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

			} catch (IOException e){
				throw new HttpProcessIOException(e);
			} catch (ServletException e) {
				throw new HttpProcessServletException(e);
			}
		}
		
		protected Page parsePage(Outcome outcome, HttpServletRequest  request, HttpServletResponse  response) throws IOException , ServletException {
			try {
				PageResponseWrapper pageResponse = new PageResponseWrapper(response);
			
				request.getRequestDispatcher(realContentPath(outcome.getUrl())).include(request, pageResponse);
				// check if another servlet or filter put a page object to the request
				Page result = (Page)request.getAttribute(Page.PAGE);
				if (result == null) {
					// parse the page
					result = pageResponse.getPage();
				}
				request.setAttribute(USING_STREAM, Boolean.valueOf(pageResponse.isUsingStream()));
				return result;
			}catch (IllegalStateException  e) {
				WebContainerInfo container = (WebContainerInfo) request.getSession().getServletContext().getAttribute(WebContainerInfo.ATTRIBUTE_NAME);
				
				// weblogic throws an IllegalStateException when an error page is served.
				// it's ok to ignore this, however for all other containers it should be thrown
				// properly.
				if (CommonHttpServerContainers.WEBLOGIC.equals(container.getName())){
					throw e;
				}

				return null;
			} 
		}
		
		private void writeOriginal(HttpServletRequest  request, HttpServletResponse  response, Page page) throws IOException  {
			response.setContentLength(page.getContentLength());
			if (request.getAttribute(USING_STREAM).equals(Boolean.TRUE)){
				PrintWriter  writer = new PrintWriter (response.getOutputStream());
				page.writePage(writer);
				//flush writer to underlying outputStream
				writer.flush();
				response.getOutputStream().flush();
			} else {
				page.writePage(response.getWriter());
				response.getWriter().flush();
			}
		}
		
		protected void applyDecorator(Page page, Decorator decorator, HttpServletRequest  request, HttpServletResponse  response) 
			throws ServletException, IOException  {
			
			try {
				request.setAttribute(Page.PAGE, page);
				ServletContext context = request.getSession().getServletContext();
				
//				// see if the URI path (webapp) is set
//				if (decorator.getURIPath() != null) {
//					// in a security conscious environment, the servlet container
//					// may return null for a given URL
//					if (context.getContext(decorator.getURIPath()) != null) {
//						context = context.getContext(decorator.getURIPath());
//					}
//				}
				
				// get the dispatcher for the decorator
				RequestDispatcher dispatcher =  context.getRequestDispatcher(realDecoratorPath(decorator.getPage()));;
				
				// create a wrapper around the response
				dispatcher.include(request, response);

				// set the headers specified as decorator init params
//				while (decorator.getInitParameterNames().hasNext()) {
//					String  initParam = (String ) decorator.getInitParameterNames().next();
//					if (initParam.startsWith("header.")) {
//						response.setHeader(initParam.substring(initParam.indexOf('.')), decorator.getInitParameter(initParam));
//					}
//				}

				request.removeAttribute(Page.PAGE);
			} catch (RuntimeException  e) {
				WebContainerInfo container = (WebContainerInfo) request.getSession().getServletContext().getAttribute(WebContainerInfo.ATTRIBUTE_NAME);
				
				// added a print message here because otherwise Tomcat swallows
				// the error and you never see it 
				if (CommonHttpServerContainers.TOMCAT.equals(container.getName())){
					// TODO use logger
					e.printStackTrace();
				}
				
				throw e;
			}
		}

	}






}
