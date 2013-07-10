/**
 * 
 */
package org.middleheaven.aas;


/**
 * 
 */
public abstract class AccessRequestController {

	final AccessRequestBroker accessRequestBroker;
	
	public AccessRequestController (AccessRequestBroker accessRequestBroker){
		this.accessRequestBroker = accessRequestBroker;
	}
	
	public final void doAccess(AccessRequest accessRequest, Permission[] guardPermissions , CallbackHandler handler){

		boolean repeat=true;
		do{
			LoginStep step = accessRequestBroker.broke(accessRequest);

			switch (step){
			case FAIL:
				doFail(accessRequest);
				repeat=false;
				break;
			case SUCCESS:
				// is authenticated
				
				Subject subject = accessRequest.getSubject();

				// assert permissions
				for (Permission p : guardPermissions){
					if (!accessRequestBroker.hasPermission(subject, p)){
						doDenied(accessRequest);
						return;
					}
				}

				// all fine. go on
				repeat=false;
				
				doSuccess(accessRequest);

				break;
			case HANDLE_CALLBACK:

				// try to fullfill the callbacks
				doCallbacks(accessRequest, handler);

				// if was not possible, invoke login page
				if (handler.getCallbacks().isBlank()){
					repeat = false;

					doRedirectToCallbacksGather(accessRequest);

					break;
				} 
			}
		} while (repeat);
	}

	/**
	 * 
	 */
	protected abstract void doRedirectToCallbacksGather(AccessRequest accessRequest);

	/**
	 * @param accessRequest
	 * @param handler
	 */
	protected abstract void doCallbacks(AccessRequest accessRequest, CallbackHandler handler);

	/**
	 * @param subject
	 */
	protected abstract void doSuccess(AccessRequest accessRequest);

	/**
	 * 
	 */
	protected abstract void doDenied(AccessRequest accessRequest);

	/**
	 * 
	 */
	protected abstract void doFail(AccessRequest accessRequest);
}
