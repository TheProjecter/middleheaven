package org.middleheaven.sequence;

public class StateChangedEvent {

	private SequenceState state;

	public StateChangedEvent(SequenceState state) {
		super();
		this.state = state;
	}
	
	public SequenceState getSequenceState(){
		return state;
	}
}
