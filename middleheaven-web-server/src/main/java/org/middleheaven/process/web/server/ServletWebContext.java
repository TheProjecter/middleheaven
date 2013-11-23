package org.middleheaven.process.web.server;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.middleheaven.aas.Subject;
import org.middleheaven.culture.Culture;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.empty.EmptyFileRepository;
import org.middleheaven.process.AbstractAttributeContext;
import org.middleheaven.process.AttributeContext;
import org.middleheaven.process.ContextScope;
import org.middleheaven.process.ScopedAttributesResolutionStrategy;
import org.middleheaven.process.web.BrowserInfo;
import org.middleheaven.process.web.HttpChannel;
import org.middleheaven.process.web.HttpMethod;
import org.middleheaven.process.web.HttpProcessingUtils;
import org.middleheaven.process.web.HttpServletRequestHttpChannel;
import org.middleheaven.process.web.HttpUrl;
import org.middleheaven.process.web.HttpUserAgent;
import org.middleheaven.process.web.server.global.HttpCultureResolver;
import org.middleheaven.util.OperatingSystemInfo;

/**
 * A {@link WebContext} that maps to {@link ServletRequest} and {@link ServletResponse} objects. 
 * This {@link WebContext} is used when the application runs in a web container.
 * 
 */
public abstract class ServletWebContext extends WebContext {


	private HttpCultureResolver httpCultureResolver;
	private List<Culture> cultures;
	private AttributeContext attributeContext = new ServletRequestAttributeContext();

	public ServletWebContext(HttpCultureResolver httpCultureResolver) {
		this.httpCultureResolver = httpCultureResolver;
	}

	/**
	 * 
	 * @return the underliing {@link ServletResponse}
	 */
	public abstract ServletResponse getServletResponse();
	
	/**
	 * 
	 * @return @return the underliing {@link ServletRequest}
	 */
	public abstract ServletRequest getServletRequest();
	
	protected abstract HttpSession getSession();

	protected abstract ServletContext getServletContext();

	protected abstract void setHeaderAttribute(ContextScope scope, String name, Object value);


	/**
	 * @return
	 */
	protected abstract Map<String, String[]> getParameters();


	public ServletWebContext (){
		this.attributeContext = new ServletRequestAttributeContext();
	}

	@Override
	public ManagedFileRepository getUploadRepository() {
		return EmptyFileRepository.repository();
	}

	@Override
	public final String getContextPath() {
		if (getServletRequest() instanceof HttpServletRequest){
			return ((HttpServletRequest) getServletRequest()).getContextPath();
		} else {
			return "";
		}
	}





	//	@Override
	//	public <T> T getAttribute(ContextScope scope, String name, Class<T> type) {
	//		Object value = null;
	//		switch (scope){
	//		case CONFIGURATION:
	//			value = getServletContext().getInitParameter(name);
	//			break;
	//		case APPLICATION:
	//			value = getServletContext().getAttribute(name);
	//			break;
	//		case SESSION:
	//			value = getSession().getAttribute(name);
	//			break;
	//		case REQUEST:
	//			value = getRequest().getAttribute(name);
	//			
	//			if (value == null){
	//				// search special
	//				if (WebContext.REQUEST_REMOTE_ADDRESS.equals(name)){
	//					value = getRequest().getRemoteAddr();
	//				} if (WebContext.REQUEST_REMOTE_HOST.equals(name)){
	//					value = getRequest().getRemoteHost();
	//				} if (WebContext.REQUEST_SERVER_PORT.equals(name)){
	//					value = getRequest().getServerPort();
	//				} if (WebContext.REQUEST_SERVER_NAME.equals(name)){
	//					value = getRequest().getServerName();
	//				} else if (this.getRequest() instanceof HttpServletRequest){
	//					// HTTP Only
	//					if (WebContext.REQUEST_URL.equals(name)){
	//						value = ((HttpServletRequest)this.getRequest()).getRequestURL();
	//					} 
	//				} 
	//			}
	//			
	//			break;
	//		case REQUEST_HEADERS:
	//			if (getRequest() instanceof HttpServletRequest){
	//				value = ((HttpServletRequest)getRequest()).getHeader(name);
	//			} 
	//			break;
	//		case PARAMETERS:
	//			value = getParameters().get(name);
	//			
	//			if (value!=null){
	//				// the result is an array and the expected type is array
	//				if (type.isArray() && value.getClass().isArray()){
	//					try{
	//						return type.cast(value);
	//					} catch (ClassCastException e ){
	//						throw new ClassCastException("Cannot cast " + value.getClass() + " to " + type);
	//					}
	//				} else if (value.getClass().isArray()){
	//					value = ((String[])value)[0];
	//				}
	//			} else if (type.isArray()){ // value is null and is expected array
	//				value = new String[0]; // parameters can only be Strings, so an array can only of string
	//			}
	//			break;
	//		case REQUEST_COOKIES:
	//			ServletCookieBagTranslator t = new ServletCookieBagTranslator(((HttpServletRequest)getRequest()));
	//		
	//			if (type.isArray() && type.getComponentType().isAssignableFrom(HttpCookie.class)){
	//				return type.cast(t.readAll().toArray());
	//			} else if (type.isAssignableFrom(HttpCookie.class)){
	//				return type.cast(t.readAll().getCookie(name));
	//			} else {
	//				throw new IllegalArgumentException("Illegal type for scope " + scope);
	//			}
	//		default:
	//			throw new IllegalArgumentException("Unavailable scope " + scope);
	//		}
	//
	//		if (type==null){
	//			// do not convert or cast
	//			@SuppressWarnings("unchecked") T t = (T)value;
	//			return t;
	//		}
	//		return TypeCoercing.coerce(value, type);
	//
	//	}


	//	/**
	//	 * {@inheritDoc}
	//	 */
	//	@Override
	//	public void removeAttribute(ContextScope scope, String name) {
	//		switch (scope){
	//		case APPLICATION:
	//			getServletContext().removeAttribute(name);
	//			break;
	//		case SESSION:
	//			getSession().removeAttribute(name);
	//			break;
	//		case REQUEST:
	//			getServletRequest().removeAttribute(name);
	//			break;
	//		case REQUEST_COOKIES:
	//		case PARAMETERS:
	//		case REQUEST_HEADERS:
	//		case CONFIGURATION:
	//			throw new IllegalArgumentException(scope + " is read-only");
	//		default:
	//			throw new IllegalArgumentException("Unavailable scope " + scope);
	//		}
	//	}
	//	
	//	@Override
	//	public void setAttribute(ContextScope scope, String name, Object value) {
	//		
	//		switch (scope){
	//		case APPLICATION:
	//			getServletContext().setAttribute(name,value);
	//			break;
	//		case SESSION:
	//			getSession().setAttribute(name,value);
	//			break;
	//		case REQUEST:
	//			getServletRequest().setAttribute(name,value);
	//			break;
	//		case REQUEST_HEADERS:
	//			setHeaderAttribute( scope,  name,  value);
	//			break;
	//		case REQUEST_COOKIES:
	//			if (!(value instanceof HttpCookie)){
	//				throw new IllegalArgumentException(HttpCookie.class + " type expected");
	//			}
	//			
	//			ServletCookieBagTranslator t = new ServletCookieBagTranslator(((HttpServletResponse)getResponse()));
	//			
	//			t.write((HttpCookie)value);
	//			break;
	//		case PARAMETERS:
	//		case CONFIGURATION:
	//			throw new IllegalArgumentException(scope + " is read-only");
	//		default:
	//			throw new IllegalArgumentException("Unavailable scope " + scope);
	//		}
	//	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpChannel getHttpChannel() {
		return new HttpServletRequestHttpChannel(this.getServletRequest());
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public HttpUrl getRequestUrl() {
		if (this.getServletRequest() instanceof HttpServletRequest){
			HttpServletRequest hrequest = (HttpServletRequest) this.getServletRequest();
			return new HttpUrl(hrequest.getRequestURL(), hrequest.getContextPath());
		} else {
			return new HttpUrl(null, "/");
		}

	}

	@Override
	public HttpUrl getRefererUrl() {
		if (this.getServletRequest() instanceof HttpServletRequest){
			HttpServletRequest hrequest = (HttpServletRequest) this.getServletRequest();
			String referer = ((HttpServletRequest) hrequest).getHeader("Referer");
			if (referer != null){
				return new HttpUrl( referer,  hrequest.getContextPath());
			}
		} 
		return null;


	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Subject getAuthenticatedSubject() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpServerResponse getResponse() {
		return new ServletBasedHttpServerResponse((HttpServletResponse) this.getServletResponse());
	}

	/**
	 * @param scope
	 * @return
	 */
	protected ScopedAttributesResolutionStrategy resolveScopeAttributeContext(ContextScope scope) {
		if (this.getServletRequest() instanceof HttpServletRequest){
			HttpServletRequest httpRequest = (HttpServletRequest) this.getServletRequest();

			switch (scope){
			case CONFIGURATION:
				return new ServletInitConfigurationScopedAttributesResolutionStrategy(httpRequest.getSession().getServletContext());
			case APPLICATION:
				return new ServletApplicationScopedAttributesResolutionStrategy(httpRequest.getSession().getServletContext());
			case SESSION:
				return new ServletSessionContextScopeStrategy(httpRequest.getSession());
			case REQUEST:
				return new ServletRequestScopedAttributesResolutionStrategy(httpRequest);
				//				if (value == null){
				//					// search special
				//					if (WebContext.REQUEST_REMOTE_ADDRESS.equals(name)){
				//						value = getRequest().getRemoteAddr();
				//					} if (WebContext.REQUEST_REMOTE_HOST.equals(name)){
				//						value = getRequest().getRemoteHost();
				//					} if (WebContext.REQUEST_SERVER_PORT.equals(name)){
				//						value = getRequest().getServerPort();
				//					} if (WebContext.REQUEST_SERVER_NAME.equals(name)){
				//						value = getRequest().getServerName();
				//					} else if (this.getRequest() instanceof HttpServletRequest){
				//						// HTTP Only
				//						if (WebContext.REQUEST_URL.equals(name)){
				//							value = ((HttpServletRequest)this.getRequest()).getRequestURL();
				//						} 
				//					} 
				//				}
			case REQUEST_HEADERS:
				return new ServletRequestHeadersScopedAttributesResolutionStrategy(httpRequest);
			case REQUEST_PARAMETERS:
				return new ParametersMapScopedAttributesResolutionStrategy(this.getParameters());
			case REQUEST_COOKIES:
				HttpServletResponse httpResponse = (HttpServletResponse) this.getServletResponse();

				return new CookiesScopedAttributesResolutionStrategy(httpRequest, httpResponse);

			default:
				throw new IllegalArgumentException("Unavailable scope " + scope);
			}
		} else {
			throw new IllegalArgumentException("Unavailable scope " + scope);
		}


	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributeContext getAttributes() {
		return attributeContext;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpMethod getRequestMethod() {
		if (this.getServletRequest() instanceof HttpServletRequest){
			return HttpMethod.valueOf(( (HttpServletRequest) this.getServletRequest()).getMethod().toUpperCase());
		} else {
			return HttpMethod.UNKOWN;
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public HttpUserAgent getAgent(){
		if (this.getServletRequest()instanceof HttpServletRequest){
			return HttpProcessingUtils.parse( (HttpServletRequest) this.getServletContext());
		} else {
			// it is not a HttpRequest, create unkown info
			return new HttpUserAgent(BrowserInfo.unkownBrowser(), OperatingSystemInfo.unkown()
					);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Culture getCulture() {
		return this.httpCultureResolver.resolveFrom(this);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Culture> getCultures() {

		if (cultures == null) {
			if (this.getServletRequest()instanceof HttpServletRequest){
				// read the Accept-Language header manually because request.getLocale()
				// defaults to Locale.getDefaultLocale() when the header is not present
				// this will deceive the globalization api

				this.cultures = HttpProcessingUtils.acceptableLanguages( ((HttpServletRequest) this.getServletRequest()).getHeader("Accept-Language"));
			} else {

				this.cultures = Collections.emptyList();

			}
		}
		return cultures;

	}


	private class ServletRequestAttributeContext extends AbstractAttributeContext {

		private final Map<ContextScope , ScopedAttributesResolutionStrategy> contexts = new EnumMap<ContextScope , ScopedAttributesResolutionStrategy>(ContextScope.class);

		/**
		 * {@inheritDoc}
		 */
		@Override
		public <T> T getAttribute(ContextScope scope, String name, Class<T> type) {
			return getScopeAttributeContext(scope).getAttribute(name, type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setAttribute(ContextScope scope, String name, Object value) {
			getScopeAttributeContext(scope).setAttribute(name, value);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void removeAttribute(ContextScope scope, String name) {
			getScopeAttributeContext(scope).removeAttribute(name);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ScopedAttributesResolutionStrategy getScopeAttributeContext(ContextScope scope) {

			ScopedAttributesResolutionStrategy context = (ScopedAttributesResolutionStrategy) contexts.get(scope);
			if (context == null) {
				context = resolveScopeAttributeContext(scope);
				contexts.put(scope,context);
			}
			return context;


		}

	}



}
