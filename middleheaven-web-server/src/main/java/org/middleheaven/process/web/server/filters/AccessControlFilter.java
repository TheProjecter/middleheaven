package org.middleheaven.process.web.server.filters;


import org.middleheaven.aas.AccessControlService;
import org.middleheaven.aas.AccessPermissionsManager;
import org.middleheaven.aas.AccessRequest;
import org.middleheaven.aas.AccessRequestBroker;
import org.middleheaven.aas.AccessRequestController;
import org.middleheaven.aas.Callback;
import org.middleheaven.aas.CallbackHandler;
import org.middleheaven.aas.IPAddressCallback;
import org.middleheaven.aas.NameCallback;
import org.middleheaven.aas.PasswordCallback;
import org.middleheaven.aas.Permission;
import org.middleheaven.process.ContextScope;
import org.middleheaven.process.web.HttpCookie;
import org.middleheaven.process.web.HttpMethod;
import org.middleheaven.process.web.HttpUrl;
import org.middleheaven.process.web.server.HttpServerContext;
import org.middleheaven.process.web.server.HttpServerSignatureStorePolicy;
import org.middleheaven.process.web.server.Outcome;
import org.middleheaven.process.web.server.RedirectAfterCookie;
import org.middleheaven.web.aas.HttpContextAccessRequest;

public class AccessControlFilter implements HttpFilter{

	private static final String CALLBACK_HANLDER = "authentication.callbackhandler";

	private AccessControlService accessControlService;

	private HttpServerSignatureStorePolicy storePolicy;
	private AccessPermissionsManager<HttpUrl> permissionsManager;
	private Outcome failureOutcome;
	private Outcome loginOutcome;
	private Outcome accessDeniedOutcome;
	private Outcome successOutcome;
	
	public AccessControlFilter(
			AccessPermissionsManager<HttpUrl> permissionsManager,
			AccessControlService accessControlService,
			HttpServerSignatureStorePolicy storePolicy, 
			Outcome loginOutcome,
			Outcome failureOutcome,
			Outcome accessDeniedOutcome,
			Outcome successOutcome
			){
		this.permissionsManager = permissionsManager;
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

	
	private class HttpAccessRequestController extends AccessRequestController {

		private HttpFilterChain chain;

		private HttpServerContext context;
		
		/**
		 * Constructor.
		 * @param accessRequestBroker
		 */
		public HttpAccessRequestController(AccessRequestBroker accessRequestBroker ,HttpServerContext context, HttpFilterChain chain) {
			super(accessRequestBroker);
			this.chain = chain;
			this.context = context;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void doFail(AccessRequest accessRequest) {
			chain.interruptWithOutcome(failureOutcome);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void doDenied(AccessRequest accessRequest) {
			chain.interruptWithOutcome(accessDeniedOutcome);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void doRedirectToCallbacksGather(AccessRequest accessRequest) {
			// if it is the login page
			if (loginOutcome.getParameterizedURL()
					.equalsIgnoreCase(context.getRequestUrl().getContexlesPathAndFileName())){

				letPass(context,chain, accessRequest);

			} else {
				chain.interruptWithOutcome(loginOutcome);

				// memorize target page
				if (context.getRequestUrl() != null){
					RedirectAfterCookie rc = new RedirectAfterCookie(
							"redirect_after_login", 
							context.getRequestUrl().toString()
							);
					context.getAttributes().setAttribute(
							ContextScope.REQUEST_COOKIES, 
							"redirect_after_login", 
							rc
					);
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void doCallbacks(AccessRequest accessRequest, CallbackHandler handler) {
			for (Callback callback : handler.getCallbacks()){
				if (callback instanceof IPAddressCallback){
					((IPAddressCallback) callback).setAddress(accessRequest.getCallerAddress());
				} else if (callback instanceof NameCallback){
					NameCallback nc = ((NameCallback) callback);
					nc.setName(context.getAttributes().getAttribute(nc.getPrompt(), String.class));
				} else if (callback instanceof PasswordCallback){
					PasswordCallback pc = ((PasswordCallback) callback);
					String attribute = context.getAttributes().getAttribute(pc.getPrompt(), String.class);
					if (attribute==null){
						attribute = "";
					}
					pc.setPassword(attribute.toCharArray());
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void doSuccess(AccessRequest accessRequest) {
			HttpCookie redirectCookie =	context.getAttributes().getAttribute(
					ContextScope.REQUEST_COOKIES,
					"redirect_after_login", 
					HttpCookie.class
			);
			
			if (redirectCookie != null){ // read redirect cookie
				RedirectAfterCookie rc = new RedirectAfterCookie(redirectCookie);
				chain.interruptWithOutcome(rc.asOutcome());

			} else if (successOutcome.getParameterizedURL().equals(context.getRequestUrl().getContexlesPathAndFileName())) {
				// already on correct url

				letPass(context,chain, accessRequest);
			} else {
				chain.interruptWithOutcome(successOutcome);

			}
		}

	}
	
	
	@Override
	public void doFilter(HttpServerContext context, HttpFilterChain chain) {

		Permission[] guardPermissions = this.permissionsManager.getGuardPermission(context.getRequestUrl());

		CallbackHandler handler = context.getAttributes().getAttribute(ContextScope.SESSION, CALLBACK_HANLDER, CallbackHandler.class);
		
		if (handler == null){
			handler = new CallbacksSetCallbackHandler();
			context.getAttributes().setAttribute(ContextScope.SESSION, CALLBACK_HANLDER, handler);
		}
		
		final HttpContextAccessRequest accessRequest = new HttpContextAccessRequest(context,handler,storePolicy.resolveSignatureStore(context));

		if (canPassWithoutCheck(guardPermissions) && !context.getRequestMethod().equals(HttpMethod.POST)){
			// free for all

			letPass(context,chain, accessRequest);
		} else {
			
			
			final AccessRequestBroker accessRequestBroker = accessControlService.accessRequestBroker();

			final AccessRequestController controller = new HttpAccessRequestController(accessRequestBroker, context, chain);
			
			controller.doAccess(accessRequest, guardPermissions, handler);
			
		}


	}

	/**
	 * Determines if the request can pass. It can if no permissions are required or all required permissions are lenient.
	 * 
	 * @param permissions all permissions required
	 * @return <code>true</code> if the request can continue, <code>false</code> otherwise
	 * 
	 */
	private boolean canPassWithoutCheck(Permission[] permissions) {
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



}
