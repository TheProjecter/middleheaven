package org.middleheaven.validation;

import org.middleheaven.collections.interval.Interval;

public class IntervalValidator<T extends Comparable<T>> implements Validator<T> {

	private Interval<T> interval;
	private boolean includeStart = true;
	private boolean includeEnd = true;
	
	public void setInterval(Interval<T> interval){
		this.interval = interval;
	}
	
	public void setInterval(T start, T end){
		this.interval = Interval.between(start, end);
	}
	
	public void setIncludeStart(boolean includeStart){
		this.includeStart = includeStart;	
	}
	
	public void setIncludeEnd(boolean includeEnd){
		this.includeEnd = includeEnd;	
	}

	public Interval<T> getInterval() {
		return interval;
	}

	public boolean isStartIncluded() {
		return includeStart;
	}

	public boolean isEndInclued() {
		return includeEnd;
	}

	@Override
	public ValidationResult validate(T object) {
		DefaultValidationResult result = new DefaultValidationResult();
		if (!interval.contains(object,includeStart,includeEnd)){
			result.add(MessageInvalidationReason.invalid(object));
		}
		return result;
	}
}
