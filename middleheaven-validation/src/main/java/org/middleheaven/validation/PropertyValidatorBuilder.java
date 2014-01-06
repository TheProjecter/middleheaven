package org.middleheaven.validation;

public class PropertyValidatorBuilder<T> {

	BeanValidator<T> beanValidator;
	private String propertyName;
	private ValidatorBuilder<T> validatorBuilder;
	
	public PropertyValidatorBuilder(	ValidatorBuilder<T> validatorBuilder,String propertyName) {
		this.validatorBuilder = validatorBuilder;
		this.beanValidator = (BeanValidator<T>) validatorBuilder.validator();
		this.propertyName = propertyName;
	}
	

	public PropertyValidatorBuilder<T> isNotEmpty(){
		beanValidator.addPropertyValidator(propertyName, new NotEmptyValidator<String>());
		return this;
	}
	
	public PropertyValidatorBuilder<T> isAValidEmail(){
		beanValidator.addPropertyValidator(propertyName, new SimpleEmailAddressValidator<String>());
		return this;
	}
	
	public PropertyValidatorBuilder<T> withinRange(Integer min, Integer max){
		IntervalValidator interval = new IntervalValidator();
		interval.setInterval(min,max);
		
		beanValidator.addPropertyValidator(propertyName, interval);
		return this;
	}
	
	public PropertyValidatorBuilder<T> property(String name){
		return validatorBuilder.property(name);
	}

	public ValidatorBuilder<T> instanceWith(Validator<T> validator){
		return validatorBuilder.instanceWith(validator);
	}
	
	public Validator<T> validator(){
		return validatorBuilder.validator();
	}
}
