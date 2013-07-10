/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.middleheaven.aas.AccessControlService;
import org.middleheaven.aas.RolePermission;
import org.middleheaven.process.MapContext;
import org.middleheaven.process.web.HttpProcessException;
import org.middleheaven.process.web.UrlPattern;
import org.middleheaven.process.web.server.AbstractSecureHttpProcessor;
import org.middleheaven.process.web.server.HttpProcessorConfig;
import org.middleheaven.process.web.server.HttpServerContext;
import org.middleheaven.process.web.server.HttpServerSignatureStorePolicy;
import org.middleheaven.process.web.server.Outcome;
import org.middleheaven.process.web.server.ServletWebContext;
import org.middleheaven.process.web.server.action.TerminalOutcome;
import org.middleheaven.process.web.server.action.URLOutcome;
import org.middleheaven.ui.UIEnvironment;
import org.middleheaven.ui.UIService;
import org.middleheaven.ui.binding.UIBinder;
import org.middleheaven.ui.layout.UIClientLayoutConstraint;
import org.middleheaven.ui.layout.UIClientLayoutManager;
import org.middleheaven.web.aas.UrlAccessPermissionsManager;

/**
 * 
 */
public class VaadinClientRenderingProcessor extends AbstractSecureHttpProcessor{

	private VaadinApplicationServletAdapter adapter;
	private String baseProcessorID;
	private final URLOutcome loginOutcome;
	private URLOutcome deniedOutcome;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(HttpProcessorConfig config) {
		
		// add adicional processor por VAADIN
		
		if (baseProcessorID == null){
			baseProcessorID = config.getProcessorId();
			
			config.getRegisteredService().registerHttpProcessor(baseProcessorID + "_VAADIN", this, UrlPattern.matchSimplePattern("/VAADIN/*"));
			config.getRegisteredService().registerHttpProcessor(baseProcessorID + "_UIDL", this, UrlPattern.matchSimplePattern("/UIDL"));
		}
		
		
	}
	
	public VaadinClientRenderingProcessor(	
			String root, 
			UrlAccessPermissionsManager permissionsManager,
			AccessControlService accessControlService,
			HttpServerSignatureStorePolicy storePolicy, 
			UIService uiService, UIEnvironment env, UIBinder binder){
		super(permissionsManager, accessControlService, storePolicy);
	
		permissionsManager.restrict(UrlPattern.matchSimplePattern("/VAADIN/*"), RolePermission.any());
		permissionsManager.restrict(UrlPattern.matchSimplePattern("/UIDL/*"), RolePermission.any());
		
		uiService.registerEnvironment(env, new VaadinRenderKit() , new MapContext());
		
		
		adapter = new VaadinApplicationServletAdapter("app" /*env.getName() TODO make sure is not blank*/, uiService , binder);
		
		
		ServletConfig configProxy = (ServletConfig) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{ServletConfig.class}, new DummyServletConfigInvocationHandler());

		try {
			adapter.init(configProxy);
		} catch (ServletException e) {
			throw new HttpProcessException(e);
		}
		
		
		UIClientLayoutManager manager = (UIClientLayoutManager) env.getClient().getUIContainerLayout().getLayoutManager();
		
		loginOutcome = URLOutcome.forUrl(root +  "/" + manager.getComponentId(UIClientLayoutConstraint.LOGIN) + ".html").byRedirecting();
		
		if (manager.getComponentId(UIClientLayoutConstraint.ACCESS_DENIED) != null){
			deniedOutcome = URLOutcome.forUrl(root +  "/" + manager.getComponentId(UIClientLayoutConstraint.ACCESS_DENIED) + ".html").byRedirecting();
		} else {
			deniedOutcome = loginOutcome;
		}
		 
		
		manager.getComponentId(UIClientLayoutConstraint.MAIN);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Outcome doSuccessProcess(HttpServerContext context) throws HttpProcessException {
		
		try {
			ServletWebContext servletContext = (ServletWebContext) context;
			
			adapter.service(servletContext.getServletRequest(), servletContext.getServletResponse());
			
			return new TerminalOutcome(); 
		} catch (Exception e) {
			throw new HttpProcessException(e);
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Outcome doSecureDenied(HttpServerContext context) throws HttpProcessException {
		return deniedOutcome;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Outcome doSecureLogin(HttpServerContext context) throws HttpProcessException {
		return loginOutcome;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Outcome doSecureFail(HttpServerContext context) throws HttpProcessException {
		return doSecureDenied(context);
	}

	
	private static class DummyServletConfigInvocationHandler implements InvocationHandler {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object invoke(Object arg0, Method method, Object[] args)
				throws Throwable {
			
			if ("getInitParameterNames".equals(method.getName())){
				return Collections.enumeration(Collections.emptySet());
			} else if ("getServletContext".equals(method.getName())){
				
				return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{ServletContext.class}, new DummyServletContextInvocationHandler());
			}
			
			return null;
		}
		
	}
	
	private static class DummyServletContextInvocationHandler implements InvocationHandler {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object invoke(Object arg0, Method method, Object[] args)
				throws Throwable {
			
			if ("getInitParameterNames".equals(method.getName())){
				return Collections.enumeration(Collections.emptySet());
			} 
			
			return null;
		}
		
	}
}
