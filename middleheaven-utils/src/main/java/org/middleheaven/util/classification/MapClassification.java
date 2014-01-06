package org.middleheaven.util.classification;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class  MapClassification<C,T> implements Classification<C,T> {

	private final Map <C, Collection<T>> classification = new HashMap<C, Collection<T>>();
	
	@Override
	public Collection<T> elements(C group) {
		Collection<T> c = classification.get(group);
		if (c==null){
			return Collections.emptySet();
		}
		return c;
	}

	public void addElement (C group , T element){
		Collection<T> c = classification.get(group);
		if (c==null){
			c = new LinkedList<T>();
		}
		c.add(element);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public C[] groups() {
		return (C[])classification.keySet().toArray();
	}

}
