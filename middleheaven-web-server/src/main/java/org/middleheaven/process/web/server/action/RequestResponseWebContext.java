package org.middleheaven.process.web.server.action;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.upload.UploadFilesContext;
import org.middleheaven.io.repository.upload.UploadFilesRequestFactory;
import org.middleheaven.process.ContextScope;
import org.middleheaven.process.web.HttpProcessingUtils;
import org.middleheaven.process.web.HttpUserAgent;
import org.middleheaven.process.web.server.ServletWebContext;
import org.middleheaven.process.web.server.global.HttpCultureResolver;

public class RequestResponseWebContext extends ServletWebContext {

	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private final Map<String,String[]> parameters;
	private ManagedFileRepository uploadRepository;

	public RequestResponseWebContext(HttpServletRequest request,HttpServletResponse response, HttpCultureResolver httpCultureResolveService) {
		super(httpCultureResolveService);
		
		this.request = request;
		this.response = response;
		
		this.request.setAttribute("__" + HttpCultureResolver.class.getName(), httpCultureResolveService);
		
		UploadFilesContext context = UploadFilesRequestFactory.getInstance().getContext(request);
		
		this.parameters = context.getParametersMap();
		this.uploadRepository = context.getManagedFileRepository();
		
	}

	@Override
	public ManagedFileRepository getUploadRepository() {
		return uploadRepository;
	}

	public HttpUserAgent getAgent(){
		return HttpProcessingUtils.parse(request);
	}

	public HttpServletRequest getServletRequest() {
		return request;
	}

	public HttpServletResponse getServletResponse() {
		return response;
	}

	@Override
	public Map<String, String[]> getParameters() {
		return this.parameters;
	}

	protected javax.servlet.ServletContext getServletContext() {
		return request.getSession().getServletContext();
	}

	@Override
	protected HttpSession getSession() {
		return request.getSession();
	}

	@Override
	protected void setHeaderAttribute(ContextScope scope, String name,Object value) {
		if (!( value instanceof String)){
			throw new ClassCastException(value + " cannot be used as a header value");
		}

		response.setHeader(name, value.toString());
	}

	@Override
	public InetAddress getRemoteAddress(){
		try{
			return InetAddress.getByName(request.getRemoteAddr());
		}catch (UnknownHostException e){
			return null;
		}
	}




}
