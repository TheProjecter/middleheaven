package org.middleheaven.process.web.server.filters;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.aas.AccessControlService;
import org.middleheaven.aas.AccessRequest;
import org.middleheaven.aas.AccessRequestBroker;
import org.middleheaven.aas.Callback;
import org.middleheaven.aas.CallbackHandler;
import org.middleheaven.aas.IPAddressCallback;
import org.middleheaven.aas.LoginStep;
import org.middleheaven.aas.NameCallback;
import org.middleheaven.aas.PasswordCallback;
import org.middleheaven.aas.Permission;
import org.middleheaven.aas.Signature;
import org.middleheaven.aas.SignatureStore;
import org.middleheaven.aas.Subject;
import org.middleheaven.process.AttributeContext;
import org.middleheaven.process.ContextScope;
import org.middleheaven.process.web.HttpCookie;
import org.middleheaven.process.web.HttpMethod;
import org.middleheaven.process.web.HttpUrl;
import org.middleheaven.process.web.UrlPattern;
import org.middleheaven.process.web.server.HttpServerContext;
import org.middleheaven.process.web.server.HttpServerSignatureStorePolicy;
import org.middleheaven.process.web.server.Outcome;
import org.middleheaven.process.web.server.RedirectAfterCookie;

public class AccessControlFilter implements HttpFilter{

	private static final String CALLBACK_HANLDER = "authentication.callbackhandler";

	private AccessControlService accessControlService;
	private List<URLPermission> permissions = new LinkedList<URLPermission>();
	private Outcome failureOutcome;
	private Outcome loginOutcome;
	private Outcome accessDeniedOutcome;
	private Outcome successOutcome;
	private HttpServerSignatureStorePolicy storePolicy;

	public AccessControlFilter(
			AccessControlService accessControlService,
			HttpServerSignatureStorePolicy storePolicy, 
			Outcome loginOutcome,
			Outcome failureOutcome,
			Outcome accessDeniedOutcome,
			Outcome successOutcome
			){

		this.storePolicy = storePolicy;
		this.accessControlService = accessControlService;
		this.failureOutcome = failureOutcome;
		this.loginOutcome = loginOutcome;
		this.accessDeniedOutcome = accessDeniedOutcome;
		this.successOutcome = successOutcome;
	}

	private void letPass(HttpServerContext context, HttpFilterChain chain, AccessRequest request){
		context.getAttributes().setAttribute(ContextScope.REQUEST, AccessRequest.class.getName(), 	request);
		chain.doChain(context);
	}

	private void fillCallbacks(CallbackHandler handler , AttributeContext attributes, AccessRequest request){

		for (Callback callback : handler.getCallbacks()){
			if (callback instanceof IPAddressCallback){
				((IPAddressCallback) callback).setAddress(request.getCallerAddress());
			} else if (callback instanceof NameCallback){
				NameCallback nc = ((NameCallback) callback);
				nc.setName(attributes.getAttribute(nc.getPrompt(), String.class));
			} else if (callback instanceof PasswordCallback){
				PasswordCallback pc = ((PasswordCallback) callback);
				String attribute = attributes.getAttribute(pc.getPrompt(), String.class);
				if (attribute==null){
					attribute = "";
				}
				pc.setPassword(attribute.toCharArray());
			}
		}
	}

	@Override
	public void doFilter(HttpServerContext context, HttpFilterChain chain) {

		Permission[] guardPermissions = getGuardPermission(context.getRequestUrl());

		CallbackHandler handler = context.getAttributes().getAttribute(ContextScope.SESSION, CALLBACK_HANLDER, CallbackHandler.class);


		if (handler ==null && canPass(guardPermissions) && !context.getRequestMethod().equals(HttpMethod.POST)){
			// free for all
			HttpContextAccessRequest request = new HttpContextAccessRequest(context,handler,storePolicy.resolveSignatureStore(context));

			letPass(context,chain, request);
		} else {
			if (handler == null){
				handler = new AccessControlCallbackHandler();
				context.getAttributes().setAttribute(ContextScope.SESSION, CALLBACK_HANLDER, handler);
			}

			final HttpContextAccessRequest request = new HttpContextAccessRequest(context,handler,storePolicy.resolveSignatureStore(context));

			final AccessRequestBroker accessRequestBroker = accessControlService.accessRequestBroker();

			boolean repeat=true;
			do{
				LoginStep step = accessRequestBroker.broke(request);

				AttributeContext attributeContext = context.getAttributes();

				switch (step){
				case FAIL:
					chain.interruptWithOutcome(failureOutcome);
					repeat=false;
					break;
				case SUCCESS:
					// is authenticated
					context.getAttributes().removeAttribute(ContextScope.SESSION, CALLBACK_HANLDER);

					Subject subject = request.getSubject();

					// assert permissions
					for (Permission p : guardPermissions){
						if (!accessRequestBroker.hasPermission(subject, p)){
							chain.interruptWithOutcome(accessDeniedOutcome);
							return;
						}
					}

					// all fine. go on
					repeat=false;
					HttpCookie redirectCookie =	attributeContext.getAttribute(
							ContextScope.REQUEST_COOKIES,
							"redirect_after_login", 
							HttpCookie.class
					);
					
					if (redirectCookie != null){ // read redirect cookie
						RedirectAfterCookie rc = new RedirectAfterCookie(redirectCookie);
						chain.interruptWithOutcome(rc.asOutcome());

					} else if (successOutcome.getParameterizedURL().equals(context.getRequestUrl().getContexlesPathAndFileName())) {
						// already on correct url

						letPass(context,chain, request);
					} else {
						chain.interruptWithOutcome(successOutcome);

					}
					break;
				case HANDLE_CALLBACK:

					// try to fullfill the callbacks
					fillCallbacks(handler,attributeContext , request);

					// if was not possible, invoke login page
					if (handler.getCallbacks().isBlank()){
						repeat = false;

						// if it is the login page
						if (this.loginOutcome.getParameterizedURL()
								.equalsIgnoreCase(context.getRequestUrl().getContexlesPathAndFileName())){

							letPass(context,chain, request);

						} else {
							chain.interruptWithOutcome(loginOutcome);

							// memorize target page
							if (context.getRequestUrl() != null){
								RedirectAfterCookie rc = new RedirectAfterCookie(
										"redirect_after_login", 
										context.getRequestUrl().toString()
										);
								attributeContext.setAttribute(
										ContextScope.REQUEST_COOKIES, 
										"redirect_after_login", 
										rc
								);
							}
						}

						break;
					} 
				}
			} while (repeat);
		}


	}

	/**
	 * Determines if the request can pass. It can if no permissions are required or all required permissions are lenient.
	 * 
	 * @param permissions all permissions required
	 * @return <code>true</code> if the request can continue, <code>false</code> otherwise
	 * 
	 */
	private boolean canPass(Permission[] permissions) {
		if (permissions.length == 0){
			return true;
		}

		for (Permission p : permissions){
			if (!p.isLenient()){
				return false;
			}
		}

		return true;
	}

	public void restrict(UrlPattern urlPattern, Permission ... permissions){
		this.permissions.add(new URLPermission(urlPattern,permissions));
	}

	private Permission[] getGuardPermission(HttpUrl url) {

		URLPermission selected = null;
		double max = 0;
		String path = url.getContexlesPathAndFileName(true);

		for (URLPermission up : this.permissions){

			double m = up.match(path);
			if (Double.compare(m, max) > 0){
				max = m;
				selected = up;
			}
		}

		if (selected != null){
			return selected.permissions;
		} else {
			return new Permission[0];
		}
	}

	private static class URLPermission {

		private Permission[] permissions;
		private UrlPattern urlPattern;

		public URLPermission(UrlPattern urlPattern, Permission ... permissions) {
			this.urlPattern = urlPattern;
			this.permissions = permissions;
		}

		public double match (String url){
			return urlPattern.match(url);
		}

	}

	private static class HttpContextAccessRequest extends AccessRequest{


		private HttpServerContext context;
		private CallbackHandler handler;
		private SignatureStore store;

		public HttpContextAccessRequest(HttpServerContext context,CallbackHandler handler, SignatureStore store){
			this.context = context;
			this.store = store;
			this.handler = handler;
		}

		@Override
		public CallbackHandler getCallbackHandler() {
			return handler;
		}

		@Override
		public InetAddress getCallerAddress() {
			return context.getHttpChannel().getRemoteAddress();
		}

		Subject subject;

		@Override
		public Subject getSubject() {
			return subject;
		}

		@Override
		protected void setSubject(Subject subject) {
			this.subject = subject;
		}

		@Override
		public Signature getSignature() {
			return store.getSignature();
		}

		@Override
		public void setSignature(Signature signature) {
			store.setSignature(signature);
		}

	}



}
