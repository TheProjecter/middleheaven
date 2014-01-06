package org.middleheaven.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Determines if {@code charsequence} is a correctly formatted email address. 
 *
 * @param <C>
 */
public class SimpleEmailAddressValidator<C extends CharSequence> implements Validator<C> {

	public SimpleEmailAddressValidator(){}
	
	@Override
	public ValidationResult validate(C email) {
		DefaultValidationResult result = new DefaultValidationResult();

		if(email==null){
			return result;
		}

		Pattern p = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");  
		Matcher m = p.matcher(email.toString()); 
		
		if (!m.matches()){
			result.add(MessageInvalidationReason.error(email,"invalid.email.address"));
		}		
		
		return result;
	}

}
