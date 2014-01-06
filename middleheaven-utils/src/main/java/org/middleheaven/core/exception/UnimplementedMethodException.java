package org.middleheaven.core.exception;

/**
 * Marks a method as not implemented. This is functionally equivalent to 
 * <code>UnsupportedOperationException</code> but as UnsupportedOperationException will never be implemented
 * <code>UnimplementedMethodException</code> will be.
 */
public class UnimplementedMethodException extends UnsupportedOperationException {


	private static final long serialVersionUID = 1L;

	public UnimplementedMethodException(String msg) {
		super(msg);
	}
	
	public UnimplementedMethodException() {
		super();
	}


}
