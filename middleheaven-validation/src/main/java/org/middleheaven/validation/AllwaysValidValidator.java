package org.middleheaven.validation;

public final class AllwaysValidValidator implements Validator<Object>{

	
	public static AllwaysValidValidator getInstance(){
		return new AllwaysValidValidator();
	}
	
	private AllwaysValidValidator(){}
	
	@Override
	public ValidationResult validate(Object object) {
		return new DefaultValidationResult();
	}

}
