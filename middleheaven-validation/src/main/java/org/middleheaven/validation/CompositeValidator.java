package org.middleheaven.validation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CompositeValidator<T> implements Validator<T> {

	Set<Validator<? extends T>> validators = new HashSet<Validator<? extends T>>();
	
	public static <E> CompositeValidator<E> compose(Validator<E> a , Validator<E> b){
		return new CompositeValidator<E>().add(a).add(b);
	}
	
	public CompositeValidator(){}
	
	public CompositeValidator<T> add(Validator<? extends T> validator){
		validators.add(validator);
		return this;
	}
	
	public CompositeValidator<T> remove(Validator<? extends T> validator){
		validators.add(validator);
		return this;
	}
	
	
	@Override
	public ValidationResult validate(T object) {
		DefaultValidationResult result = new DefaultValidationResult();
		for (Iterator<Validator<? extends T>> it = validators.iterator(); result.isStrictlyValid() && it.hasNext();  ){
			@SuppressWarnings("unchecked") Validator<T> v = (Validator<T>) it.next();
			result.merge(v.validate(object));
		}
		
		return result;
	}

}
