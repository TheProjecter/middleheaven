package org.middleheaven.validation;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.middleheaven.collections.iterators.IteratorsIterator;

public class DefaultValidationResult implements ValidationResult{


	private final Map<InvalidationSeverity, List<InvalidationReason>> reasons = new EnumMap<InvalidationSeverity, List<InvalidationReason>>(InvalidationSeverity.class);
	private boolean hasErrors = false;
	private boolean hasWarnings = false;
	
	public void clear(){
		hasErrors = false;
		hasWarnings = false;
		reasons.clear();
	}
	
	@Override
	public void add(InvalidationReason reason) {
		if (!hasErrors && InvalidationSeverity.ERROR.equals(reason.getSeverity())){
			hasErrors = true;
		}
		if (!hasWarnings && InvalidationSeverity.WARNING.equals(reason.getSeverity())){
			hasWarnings = true;
		}
		
		InvalidationSeverity severity = reason.getSeverity() == null ? InvalidationSeverity.ERROR : reason.getSeverity();
		
		List<InvalidationReason> list = reasons.get(severity);
		if (list==null){
			list = new LinkedList<InvalidationReason>();
			reasons.put(severity,list);
		}
		list.add(reason);
	}
	
	@Override
	public boolean hasErrors() {
		return hasErrors;
	}

	@Override
	public boolean hasWarnings() {
		return hasWarnings;
	}
	
	@Override
	public boolean isValid() {
		return !hasErrors;
	}

	@Override
	public Iterator<InvalidationReason> iterator() {
		// reason.values returns a list of list of reasons.
		return IteratorsIterator.aggregateIterables(reasons.values());
	}


	@Override
	public Iterator<InvalidationReason> iterator(InvalidationSeverity severity) {
		List<InvalidationReason> list = reasons.get(severity);
		if(list == null){
			list = Collections.emptyList();
		}
		return list.iterator();
	}
	
	@Override
	public boolean isStrictlyValid() {
		return reasons.isEmpty();
	}

	@Override
	public void merge(ValidationResult other) {
	
		for ( InvalidationReason reason : other){
			this.add(reason);
		}

	}


}
