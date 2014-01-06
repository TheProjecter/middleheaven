package org.middleheaven.validation;

import org.middleheaven.util.classification.LogicOperator;

/**
 * 
 * Validates an object by logic operation on the result of two other validators.
 *
 * @param <T>
 */
public class LogicValidator<T> implements Validator<T> {

	LogicOperator operator;
	Validator<T> a;
	Validator<T> b;
	
	public static <E> LogicValidator<E> and (Validator<E> a , Validator<E> b){
		return new LogicValidator<E>(LogicOperator.and(), a, b);
	}
	
	public static <E> LogicValidator<E> or (Validator<E> a , Validator<E> b){
		return new LogicValidator<E>(LogicOperator.or(), a, b);
	}
	
	public LogicValidator(LogicOperator operator,Validator<T> a , Validator<T> b) {
		super();
		this.operator = operator;
		this.a = a;
		this.b = b;
	}

	@Override
	public ValidationResult validate(T object) {
		DefaultValidationResult result = new DefaultValidationResult();
		
		ValidationResult aContext=  a.validate(object);
		 
		ValidationResult bContext=  b.validate(object);
		
		if (!operator.operate(aContext.isValid(), bContext.isValid())){
			result.merge(aContext);
			result.merge(bContext);
		}
		
		return result;
	}

}
