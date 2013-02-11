package org.middleheaven.process.web.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.aas.AccessDeniedException;
import org.middleheaven.logging.Logger;
import org.middleheaven.process.web.HttpProcessException;
import org.middleheaven.process.web.HttpProcessIOException;
import org.middleheaven.process.web.HttpStatusCode;
import org.middleheaven.process.web.UrlPattern;
import org.middleheaven.process.web.server.action.HttpProcessServletException;
import org.middleheaven.process.web.server.action.RequestResponseWebContext;
import org.middleheaven.process.web.server.filters.HttpFilter;
import org.middleheaven.process.web.server.filters.HttpFilterChain;
import org.middleheaven.process.web.server.global.HttpCultureResolver;
import org.middleheaven.process.web.server.global.RequestAgentHttpCultureResolver;
import org.middleheaven.util.function.Maybe;
import org.middleheaven.web.rendering.RenderingProcessor;
import org.middleheaven.web.rendering.RenderingProcessorResolver;

public abstract class AbstractHttpServerService implements HttpServerService {

	private final List<HttpFilter> filters = new CopyOnWriteArrayList<HttpFilter>();
	private final Map<String, HttpMapping> processorsMappings = new HashMap<String, HttpMapping>();
	private final Map<String, HttpRenderingMapping> renderingMappings = new HashMap<String, HttpRenderingMapping>();

	private boolean available = false;
	private boolean stopped = false;

	private Map<String,HttpRenderingMapping> urlTrasientRenderingMatch = new WeakHashMap<String, AbstractHttpServerService.HttpRenderingMapping>();

	private HttpCultureResolver httpCultureResolveService = new RequestAgentHttpCultureResolver();
	private Logger logger;

	private static class HttpMapping{

		public HttpProcessor processor;
		public UrlPattern mapping;

		public HttpMapping(HttpProcessor processor, UrlPattern mapping) {
			this.processor = processor;
			this.mapping = mapping;
		}

	}

	/**
	 * 
	 * Constructor.
	 */
	public AbstractHttpServerService (){
		this.logger = Logger.onBookFor(this.getClass());
	}

	@Override
	public HttpCultureResolver getHttpCultureResolver() {
		return httpCultureResolveService;
	}

	@Override
	public void setHttpCultureResolver(HttpCultureResolver httpCultureResolveService) {
		this.httpCultureResolveService = httpCultureResolveService;

	}

	private static class HttpRenderingMapping{

		public RenderingProcessorResolver processor;
		public UrlPattern mapping;

		public HttpRenderingMapping(RenderingProcessorResolver resolver, UrlPattern mapping) {
			this.processor = resolver;
			this.mapping = mapping;
		}

	}

	@Override
	public void addFilter(HttpFilter filter) {
		filters.add(filter);
	}

	@Override
	public void removeFilter(HttpFilter filter) {
		filters.remove(filter);
	}

	@Override
	public void removeRenderingProcessorResolver(String resolverID) {
		renderingMappings.remove(resolverID);
	}

	@Override
	public void addRenderingProcessorResolver(String resolverID, RenderingProcessorResolver resolver, UrlPattern mapping) {
		renderingMappings.put(resolverID, new HttpRenderingMapping(resolver,mapping));
	}

	@Override
	public void removeAllRenderingProcessors() {
		renderingMappings.clear();
	} 


	@Override
	public RenderingProcessor resolverRenderingProcessor(String incomingUrl, String viewName , String contentType) {

		HttpRenderingMapping mapping = urlTrasientRenderingMatch.get(incomingUrl);

		if (mapping == null){
			Maybe<HttpRenderingMapping> maybeMapping = findHttpRenderingMapping(incomingUrl, viewName , contentType);
					
			if (maybeMapping.isPresent()){
				mapping = maybeMapping.get();
				urlTrasientRenderingMatch.put(incomingUrl, maybeMapping.get());
			} else {
				throw new IllegalStateException("Cannot find a mapping for url " + incomingUrl + " and type " + contentType);
			}
		}

		return mapping.processor.resolve(viewName, contentType);
	}

	private Maybe<HttpRenderingMapping> findHttpRenderingMapping(String incomingUrl, String viewName, String contentType){
		HttpRenderingMapping mapping = null;
		double max = 0;
		for (HttpRenderingMapping m : renderingMappings.values()){

			double match = m.processor.canProcess(viewName, contentType) ? m.mapping.match(incomingUrl) : 0;

			if (Double.compare(match, max) >0){
				mapping = m;
			}

		}
		
		return Maybe.of(mapping);

	}
	
	@Override
	public HttpProcessor resolveControlProcessor(String url) {

		double max = 0;
		HttpMapping mapping = null;

		for (HttpMapping m : processorsMappings.values()){

			double match =  m.mapping.match(url);

			if (Double.compare(match, max) >0){
				mapping = m;
				max = match;
			}

		}

		return mapping != null ? mapping.processor : null;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public synchronized boolean isAvailable() {
		return available;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void setAvailable(boolean available) {
		this.available = available;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void stop() {
		this.available = false;
		this.stopped = true;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void start() {
		this.available = true;
	}

	/**
	 * Determines if hte server is stopped.
	 * @return <code>true</code> if the server is stopped, <code>false</code> otherwise.
	 */
	protected synchronized boolean isStopped(){
		return this.stopped;
	}



	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void registerHttpProcessor(final String processorID, final HttpProcessor processor, final UrlPattern mapping) {

		processorsMappings.put(processorID, new HttpMapping(processor,mapping));

		processor.init(new HttpProcessorConfig() {

			@Override
			public UrlPattern getUrlPattern() {
				return mapping;
			}

			@Override
			public HttpServerService getRegisteredService() {
				return AbstractHttpServerService.this;
			}

			@Override
			public String getProcessorId() {
				return processorID;
			}
		});
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void unRegisterHttpProcessor(String processorID) {
		processorsMappings.remove(processorID);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRegistered(String processorID) {
		return processorsMappings.containsKey(processorID);
	}

	/**
	 * Entry point for the FrontEndServlet to call.
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

		if (isStopped()){
			logger.warn("HttpServerService is stopped.");
			response.sendError(HttpStatusCode.NOT_FOUND.intValue()); 
			return;
		}

		if (!isAvailable()){
			logger.warn("HttpServerService is not available.");
			response.sendError(HttpStatusCode.SERVICE_UNAVAILABLE.intValue()); 
			return;
		}

		// determine processor
		RequestResponseWebContext context = new  RequestResponseWebContext(request,response,this.getHttpCultureResolver());

		request.setAttribute("mhRequestResponseWebContext", context);

		HttpProcessor processor = resolveControlProcessor(context.getRequestUrl().getContexlesPathAndFileName());

		if (processor == null){
			logger.warn("{0} has not found for {1}", HttpProcessor.class.getSimpleName() , request.getRequestURI());
			response.sendError(HttpStatusCode.NOT_IMPLEMENTED.intValue()); 
			return;
		}

		try{

			// execute processing
			Outcome outcome = this.doService(context, processor);

			if(outcome == null){
				logger.warn("Outcome is null for {0}", request.getRequestURI());
				response.sendError(HttpStatusCode.NOT_FOUND.intValue());
			} else if (outcome.isTerminal()){
				logger.debug("Outcome is terminal for {0} ", request.getRequestURI());
				return; // do not process view. The response is already written.
			} else if (outcome.isError){
				response.sendError(outcome.getHttpCode().intValue());
			}else if (outcome.isDoRedirect()){
				if (HttpStatusCode.MOVED_PERMANENTLY.equals(outcome.getHttpCode())){
					response.setStatus(HttpStatusCode.MOVED_PERMANENTLY.intValue());
					response.setHeader( "Location", addContextPath(request.getContextPath(), outcome.getParameterizedURL()));
					response.setHeader( "Connection", "close" );
				} else {
					response.sendRedirect(addContextPath(request.getContextPath(), outcome.getParameterizedURL()));
				}

			} else {

				String contentType = request.getHeader("Content-Type");

				if(outcome.getContentType()!=null){
					contentType = outcome.getContentType();
				} 

				RenderingProcessor render = this.resolverRenderingProcessor(
						context.getRequestUrl().getFilename(), 
						outcome.getUrl(), 
						contentType
						);

				if (render == null){
					logger.error("Render could not be found for {0}" , outcome);
					response.sendError(HttpStatusCode.NOT_FOUND.intValue());
				} else {
					render.process(context, outcome,contentType);
				}
			}

		}catch (AccessDeniedException e){
			logger.warn("Access denied to {0}", request.getRequestURI());
			response.sendError(HttpStatusCode.FORBIDDEN.intValue());
		}catch (HttpProcessIOException e){
			throw e.getIOException();
		}catch (HttpProcessServletException e){
			throw e.getServletException();
		}catch (Exception e){
			throw new ServletException(e);
		} 
	}

	/**
	 * Realizes the service.
	 * 
	 * @param context the current context.
	 * @param processor the selected processor
	 * @return the  service {@link Outcome}.
	 * @throws HttpProcessException if something goes wrong
	 */
	protected Outcome doService(HttpServerContext context ,HttpProcessor processor) throws HttpProcessException{

		final FilterChain filterChain = new FilterChain(filters,processor);
		filterChain.doChain(context);

		return filterChain.getOutcome();
	}


	private  class FilterChain extends AbstractInterruptableChain<HttpFilter> implements HttpFilterChain {

		HttpProcessor processor;

		public FilterChain(List<HttpFilter> filters, HttpProcessor processor ){
			super (filters, null);
			this.processor = processor; 
		}


		@Override
		protected void call(HttpFilter element, HttpServerContext context, AbstractInterruptableChain chain) {
			element.doFilter(context, (FilterChain)chain);
		}

		@Override
		protected Outcome doFinal(HttpServerContext context) {
			return doOnChainEnd( context, processor ,  this.getOutcome());
		}


	}

	/**
	 * Execute at the end of the chain.
	 * @param context the curent context.
	 * @param processor the current processor
	 * @param outcome the previous {@link Outcome}
	 * @return
	 * @throws HttpProcessException
	 */
	protected Outcome doOnChainEnd(HttpServerContext context,HttpProcessor processor , Outcome outcome) throws HttpProcessException{

		if (outcome == null){
			outcome = processor.process(context);
		}

		return outcome;
	}

	private String addContextPath(String ctx, String url){
		if (url.startsWith("/")){
			return ctx.concat(url);
		} else {
			return url;
		}
	}



}
