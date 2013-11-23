package org.middleheaven.process.web.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import org.middleheaven.aas.Subject;
import org.middleheaven.culture.Culture;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.empty.EmptyFileRepository;
import org.middleheaven.process.AttributeContext;
import org.middleheaven.process.MapContext;
import org.middleheaven.process.web.HttpChannel;
import org.middleheaven.process.web.HttpMethod;
import org.middleheaven.process.web.HttpUrl;
import org.middleheaven.process.web.HttpUserAgent;
import org.middleheaven.process.web.server.global.HttpCultureResolver;
import org.middleheaven.process.web.server.global.RequestAgentHttpCultureResolver;


public final class MapWebContext  extends WebContext{

	private HttpUserAgent agent;
	private HttpMethod service;
	private MapContext mapContext;
	private Culture culture= Culture.defaultValue();
	private InetAddress address;
	private String url;
	private String contextPath;
	private ManagedFileRepository uploadRepository = EmptyFileRepository.repository();
	
	public MapWebContext(String url , String contextPath, HttpMethod service) {
		this(url,contextPath, service, new RequestAgentHttpCultureResolver());
	}
	
	public MapWebContext(String url , String contextPath, HttpMethod service, HttpCultureResolver httpCultureResolveService) {
		this.service = service;
		this.url = url;
		this.contextPath = contextPath;
		this.mapContext = new MapContext();
		try {
			this.address = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			this.address = null;
		}

	}

	@Override
	public InetAddress getRemoteAddress() {
		return address;
	}
	
	public void setRemote(InetAddress address){
		this.address = address;
	}
	
	public void setAgent(HttpUserAgent agent){
		this.agent = agent;
	}
	
	
	public void setCulture(Culture culture) {
		this.culture = culture;
	}

	@Override
	public String getContextPath() {
		return this.contextPath;
	}


	
	/**
	 * Obtains {@link HttpMethod}.
	 * @return the service
	 */
	public HttpMethod getService() {
		return service;
	}

	/**
	 * Obtains {@link MapContext}.
	 * @return the mapContext
	 */
	public MapContext getMapContext() {
		return mapContext;
	}

	/**
	 * Obtains {@link InetAddress}.
	 * @return the address
	 */
	public InetAddress getAddress() {
		return address;
	}

	/**
	 * Obtains {@link String}.
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpServerResponse getResponse() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileRepository getUploadRepository() {
		return uploadRepository;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributeContext getAttributes() {
		return mapContext;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Culture> getCultures() {
		return Collections.singletonList(Culture.defaultValue());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Culture getCulture() {
		return Culture.defaultValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpChannel getHttpChannel() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpUrl getRequestUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpUrl getRefererUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpMethod getRequestMethod() {
		// TODO Auto-generated method stub
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
	public HttpUserAgent getAgent() {
		// TODO Auto-generated method stub
		return null;
	}






}
