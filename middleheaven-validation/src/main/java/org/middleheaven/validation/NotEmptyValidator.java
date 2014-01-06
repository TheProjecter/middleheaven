package org.middleheaven.validation;

import java.util.Collection;
import java.util.Map;

import org.middleheaven.collections.enumerable.Enumerable;

public class NotEmptyValidator<T> implements Validator<T> {

	@Override
	public ValidationResult validate(T obj) {
		DefaultValidationResult result = new DefaultValidationResult();	
		
		if (obj == null) {
			result.add(MessageInvalidationReason.error(null,"invalid.is.empty"));
		} else if (obj instanceof Collection) {
			if (((Collection< ? >) obj).isEmpty()) {
				result.add(MessageInvalidationReason.error(obj,"invalid.is.empty"));
			}
		} else if (obj instanceof CharSequence) {
			if (((CharSequence) obj.toString().trim()).length() == 0) {
				result.add(MessageInvalidationReason.error(obj,"invalid.is.empty"));
			}
		} else if (obj instanceof Map) {
			if (((Map< ? , ? >) obj).isEmpty()) {
				result.add(MessageInvalidationReason.error(obj,"invalid.is.empty"));
			}
		} else if (obj instanceof Enumerable) {
			if (((Enumerable<?>) obj).isEmpty()) {
				result.add(MessageInvalidationReason.error(obj,"invalid.is.empty"));
			}
		}	
		
		return result;
	}

	
}
