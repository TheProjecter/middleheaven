/**
 * 
 */
package org.middleheaven.process.web.server;

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
import org.middleheaven.process.web.HttpProcessException;
import org.middleheaven.process.web.HttpUrl;
import org.middleheaven.process.web.server.filters.CallbacksSetCallbackHandler;
import org.middleheaven.web.aas.HttpContextAccessRequest;

/**
 * 
 */
public abstract class AbstractSecureHttpProcessor implements HttpProcessor {

	private static final String CALLBACK_HANLDER = "authentication.callbackhandler";

	private AccessPermissionsManager<HttpUrl> permissionsManager;
	private HttpServerSignatureStorePolicy storePolicy;
	private AccessControlService accessControlService;

	public AbstractSecureHttpProcessor(
			AccessPermissionsManager<HttpUrl> permissionsManager,
			AccessControlService accessControlService,
			HttpServerSignatureStorePolicy storePolicy
			){
		this.permissionsManager = permissionsManager;
		this.storePolicy = storePolicy;
		this.accessControlService = accessControlService;
	}
	
	/**
	 * Process the request 
	 * @param context the invocation context.
	 * 
	 * @throws HttpProcessException if something goes wrong.
	 * @return the process outcome.
	 */
	public final Outcome process(HttpServerContext context) throws HttpProcessException{
		Permission[] guardPermissions = this.permissionsManager.getGuardPermission(context.getRequestUrl());

		CallbackHandler handler = context.getAttributes().getAttribute(ContextScope.SESSION, CALLBACK_HANLDER, CallbackHandler.class);
		
		if (handler == null){
			handler = new CallbacksSetCallbackHandler();
			context.getAttributes().setAttribute(ContextScope.SESSION, CALLBACK_HANLDER, handler);
		}
		
		final HttpContextAccessRequest accessRequest = new HttpContextAccessRequest(context,handler,storePolicy.resolveSignatureStore(context));

		if (canPassWithoutCheck(guardPermissions) /* && !context.getRequestMethod().equals(HttpMethod.POST)*/){
			// free for all
			context.getAttributes().setAttribute(ContextScope.REQUEST, AccessRequest.class.getName(), 	accessRequest);
		
			return doSuccessProcess(context);
		} else {
			
			
			final AccessRequestBroker accessRequestBroker = accessControlService.accessRequestBroker();

			final SecureAccessRequestController controller = new SecureAccessRequestController(accessRequestBroker, context);
			
			controller.doAccess(accessRequest, guardPermissions, handler);
			
			return controller.getOutcome();
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

	private class SecureAccessRequestController extends AccessRequestController {

		private HttpServerContext context;
		private Outcome outcome;
		
		/**
		 * Constructor.
		 * @param accessRequestBroker
		 * @param context 
		 */
		public SecureAccessRequestController(AccessRequestBroker accessRequestBroker, HttpServerContext context) {
			super(accessRequestBroker);
			this.context = context;
		}

		public Outcome getOutcome(){
			return outcome;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void doSuccess(AccessRequest accessRequest) {
			outcome = doSuccessProcess(context);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void doRedirectToCallbacksGather(AccessRequest accessRequest) {
			outcome = doSecureLogin(context);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void doCallbacks(AccessRequest accessRequest,CallbackHandler handler) {
			//TODO vaddin as no attributes, must be read from components
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
		protected void doDenied(AccessRequest accessRequest) {
			outcome = doSecureDenied(context);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void doFail(AccessRequest accessRequest) {
			outcome = doSecureFail(context);
		}
		
	}
	
	protected abstract Outcome doSuccessProcess(HttpServerContext context) throws HttpProcessException;
	protected abstract Outcome doSecureFail(HttpServerContext context) throws HttpProcessException;
	protected abstract Outcome doSecureDenied(HttpServerContext context) throws HttpProcessException;
	protected abstract Outcome doSecureLogin(HttpServerContext context) throws HttpProcessException;
	
}
