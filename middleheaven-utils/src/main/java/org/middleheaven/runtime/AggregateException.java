/**
 * 
 */
package org.middleheaven.runtime;

import java.util.List;

/**
 * 
 */
public class AggregateException extends RuntimeException {

	private static final long serialVersionUID = -5286218876903113390L;
	
	private List<RuntimeException> exceptions;

	/**
	 * Constructor.
	 * @param exceptions
	 */
	public AggregateException(List<RuntimeException> exceptions) {
		super("Aggregate exception");
		this.exceptions = exceptions;
	}
	
	public List<RuntimeException> getExceptions(){
		return exceptions;
	}

}
