package org.middleheaven.aas;

import org.middleheaven.collections.CollectionUtils;

/**
 * The password {@link Callback}.
 */
public class PasswordCallback implements Callback {


	private static final long serialVersionUID = 1769695239559899080L;
	
	private String prompt;
	private char[] password; 
	             
	/**
	 * 
	 * Constructor.
	 * @param prompt the password prompt. This string will somewhat utilized to interrogate the subject for a password.
	 */
	public PasswordCallback(String prompt){
		this.prompt = prompt;
	}

	/**
	 * 
	 * @return the password as a char array.
	 */
	public char[] getPassword() {
		return CollectionUtils.duplicateArray(password);
	}


	public void setPassword(char[] password) {
		this.password = CollectionUtils.duplicateArray(password);
	}


	public String getPrompt() {
		return prompt;
	}


	@Override
	public boolean isBlank() {
		return password==null || password.length==0;
	}

}
