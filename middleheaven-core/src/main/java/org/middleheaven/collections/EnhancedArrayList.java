package org.middleheaven.collections;

import java.util.ArrayList;
import java.util.Collection;

public class EnhancedArrayList<T> extends EncancedListAdapter<T>{

	public EnhancedArrayList() {
		super(new ArrayList<T>());
	}
	
	public EnhancedArrayList(int size) {
		super(new ArrayList<T>(size));
	}

	public EnhancedArrayList(Enumerable<? extends T> other) {
		this(new ArrayList<T>());
		
		for (T t : other){
			this.add(t);
		}
	}
	
	public EnhancedArrayList(Collection<? extends T> other) {
		super(new ArrayList<T>(other));
	}
}
