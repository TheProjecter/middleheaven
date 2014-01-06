package org.middleheaven.sequence;

import java.io.Serializable;

/**
 * Memento pattern implementation to store and recover sequence state 
 * 
 *
 */
public final class SequenceState implements Serializable{


	private static final long serialVersionUID = 5400730752507991533L;
	
	private Object lastUsedValue;
	private String name;
	
	public SequenceState(Object lastUsedValue) {
		this(null,lastUsedValue);
	}
	
	public SequenceState(String name, Object lastUsedValue) {
		super();
		this.name = name;
		this.lastUsedValue = lastUsedValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SequenceState(){}
	
    /**
     * The last used value for the sequence
     * 
     * @return the last used value for the sequence
     */ 
	public Object getLastUsedValue() {
		return lastUsedValue;
	}
	public void setLastUsedValue(Object lastUsedValue) {
		this.lastUsedValue = lastUsedValue;
	}

}
