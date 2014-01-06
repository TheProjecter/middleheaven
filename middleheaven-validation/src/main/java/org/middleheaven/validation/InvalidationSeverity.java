package org.middleheaven.validation;

public enum InvalidationSeverity {

	/**
	 * Causes the validation to fail
	 */
	ERROR,
	
	/**
	 * does not causes the validation to fail but can inform 
	 * about problems during the validation.
	 */
	WARNING
}
