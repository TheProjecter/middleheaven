package org.middleheaven.validation;

public final class ValidatorBuilder<T> {

	
	public static <B> ValidatorBuilder<B> newValidatorFor(Class<B> type){
		return new ValidatorBuilder<B>();
	}
	
	private BeanValidator<T> validator = new BeanValidator<T>();
	
	private ValidatorBuilder(){
		
	}
	
	public ValidatorBuilder<T> instanceWith(Validator<T> validator){
		this.validator.add(validator);
		return this;
	}
	
	public PropertyValidatorBuilder<T> property(String propertyName){
		
		return new PropertyValidatorBuilder<T>(this,propertyName);
	}
	
	public Validator<T> validator(){
		return validator;
	}
}
