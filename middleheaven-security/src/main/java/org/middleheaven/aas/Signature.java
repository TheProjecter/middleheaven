package org.middleheaven.aas;

import java.io.Serializable;
import java.util.Set;

/**
 * Represents a {@link Credential} set associated with a user having signed in on the system.
 */
public interface Signature extends Serializable {

	/**
	 * Determines if the signature is valid.
	 * @return <code>true</code> if it's valid, <code>false</code> otherwise.
	 */
	boolean isValid();
	
	/**
	 * Returns the associated credentials set.
	 * @return the associated credentials set.
	 */
	public Set<Credential> getCredentials();
	
	/**
	 * Refreches the signature. If the validity of the signature is time based , refreshing it will turn it valid for longer.
	 */
	void refresh();

}
