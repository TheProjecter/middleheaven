package org.middleheaven.validation;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Result of validating an object
 *
 */
public interface ValidationResult extends Serializable, Iterable<InvalidationReason>{

	/**
	 * 
	 * @return <code>true</code> if no error invalidation messages are present; <code>false</code> otherwise.
	 */
	public boolean isValid();
	
	/**
	 * 
	 * @return <code>true</code> if no invalidation messages, of any level, are present; <code>false</code> otherwise.
	 */
	public boolean isStrictlyValid();

	/**
	 * 
	 * @return <code>true</code> if warning invalidation messages are present; <code>false</code> otherwise.
	 */
	public boolean hasWarnings();
	
	/**
	 * 
	 * @return <code>true</code> if error invalidation messages are present; <code>false</code> otherwise.
	 */
	public boolean hasErrors();
	
	/**
	 * 
	 * @param reason invalidation reason
	 */
	public void add(InvalidationReason reason);
	
	/**
	 * Adds the invalidation reasons on <code>other</code> to <code>this</code> 
	 */
	public void merge(ValidationResult other);

	public Iterator<InvalidationReason> iterator(InvalidationSeverity severity);
}
