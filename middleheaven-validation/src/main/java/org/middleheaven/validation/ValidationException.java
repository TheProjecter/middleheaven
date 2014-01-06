package org.middleheaven.validation;

public class ValidationException extends RuntimeException {

	
	private ValidationResult context;

	public ValidationException(ValidationResult context) {
		super();
		this.context = context;
	}

	public ValidationResult getResult() {
		return context;
	}
	
}
