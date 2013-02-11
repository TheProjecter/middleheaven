package org.middleheaven.aas;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;


public final class Authenticator {

	private final Collection<QualifiedAuthenticationModule> agents = new LinkedHashSet<QualifiedAuthenticationModule>();

	private static class QualifiedAuthenticationModule {

		AuthenticationModule agent;
		SuccessLevel level;
		public QualifiedAuthenticationModule(AuthenticationModule agent,SuccessLevel level) {
			super();
			this.agent = agent;
			this.level = level;
		}

	}

	public Authenticator addAuthenticationModule (AuthenticationModule module, SuccessLevel level){
		this.agents.add(new QualifiedAuthenticationModule(module,level));
		return this;
	}

	public void registerCallbacks(CallbacksSet callbackSet) {
		for(QualifiedAuthenticationModule agentLevel : agents){
			agentLevel.agent.registerCallbacks(callbackSet);
		}
	}

	public void autenticate(Set<Credential> credentials, CallbacksSet callbackSet) throws AuthenticationException{

		boolean globalSucess = true;

		/*
		 * The overall authentication succeeds only if all 
		 * Required and Requisite LoginModules succeed. 
		 * If a Sufficient  LoginModule is configured and succeeds, 
		 * then only the Required and Requisite LoginModules 
		 * prior to that Sufficient LoginModule need to have
		 *  succeeded for the overall authentication to succeed.
		 *   If no Required or Requisite LoginModules
		 *  are configured for an application, then at 
		 *  least one Sufficient or Optional  LoginModule must succeed. 
		 */

		/**
		 * see http://java.sun.com/j2se/1.4.2/docs/guide/security/jaas/JAASRefGuide.html#LoginContext
		 */
		for(QualifiedAuthenticationModule agentLevel : agents){
			try {
				agentLevel.agent.autenticate(credentials, callbackSet);
				if (SuccessLevel.SUFFICIENT.equals(agentLevel.level)){
					// interrupt with success. no further authentication is required
					globalSucess = true;
					break;
				}
			} catch (AuthenticationException e) {
				if (SuccessLevel.REQUISITE.equals(agentLevel.level)){
					// interrupt with fail. no further authentication is required
					globalSucess = false;
					throw e;
				} else if (SuccessLevel.REQUIRED.equals(agentLevel.level)){
					// do not interrupt. continue
					globalSucess = false;
				}  else {
					continue; // explicitly continue
				}
			}
		}

		if(!globalSucess){
			/// do not sign in
			throw new FailureAutenticationException();
		}

	}

}
