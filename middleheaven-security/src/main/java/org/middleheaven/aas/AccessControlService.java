package org.middleheaven.aas;

import org.middleheaven.core.annotations.Service;

@Service
public interface AccessControlService {


	public void addAuthenticationModule(AuthenticationModule module, SuccessLevel level);
	public void addAutorizationModule(AutorizationModule module);
	
	public AccessRequestBroker accessRequestBroker();
	
}
