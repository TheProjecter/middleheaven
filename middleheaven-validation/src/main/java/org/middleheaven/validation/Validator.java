package org.middleheaven.validation;

/**
 * Encapsulates the logic to validate an object 
 *
 * @param <T> the type of object this validator can validate.
 */
public interface Validator<T> {

	/**
	 * Validates the given object. Returns the result of that validation.
	 * @param object
	 * @return
	 */
	public ValidationResult validate ( T object ); 
}
