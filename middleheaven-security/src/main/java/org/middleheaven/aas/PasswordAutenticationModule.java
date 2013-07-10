package org.middleheaven.aas;

import java.util.Set;


/**
 * An {@link AuthenticationModule} that use a simple name-password pair callback.
 * 
 */
public class PasswordAutenticationModule implements AuthenticationModule {

	private PasswordVerifier verifier;

	
	private String nameCallBackIdentifier = "authentication.name";
	private String passwordCallBackIdentifier = "authentication.password";
	
	/**
	 * 
	 * Constructor.
	 * @param verifier
	 */
	public PasswordAutenticationModule(PasswordVerifier verifier){
		this.verifier = verifier;
	}
	
	/**
	 * 
	 * Constructor.
	 * @param verifier
	 * @param nameCallBackIdentifier the string identifier to use for the "name" callback
	 * @param passwordCallBackIdentifier the string identifier to use for the "password" callback
	 */
	public PasswordAutenticationModule(PasswordVerifier verifier, String nameCallBackIdentifier, String passwordCallBackIdentifier){
		this.verifier = verifier;
		this.nameCallBackIdentifier = nameCallBackIdentifier;
		this.passwordCallBackIdentifier = passwordCallBackIdentifier;
	}

	@Override
	public void registerCallbacks(CallbacksSet callbackSet) {
		callbackSet.add(new NameCallback(nameCallBackIdentifier,""));
		callbackSet.add(new PasswordCallback(passwordCallBackIdentifier));
	}
	
	@Override
	public void autenticate(Set<Credential> credentials,
			CallbacksSet callbackSet) throws AuthenticationException {
		

		
		String name = callbackSet.getCallback(NameCallback.class).getName();
		char[] password = callbackSet.getCallback(PasswordCallback.class).getPassword();
		
		try{
			verifier.verify (name, password);
			
			credentials.add(new NamedCredential(name));
		} catch (InvalidPasswordException e){
			// do not add credential
			throw new FailureAutenticationException();
		}
	}





}
