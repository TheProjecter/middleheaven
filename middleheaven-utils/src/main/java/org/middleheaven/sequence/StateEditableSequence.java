package org.middleheaven.sequence;

public interface StateEditableSequence<T> extends Sequence<T> {

    /**
     * 
     * @return SequenceState for this sequence. 
     */
	public SequenceState getSequenceState();
	
	public void setSequenceState(SequenceState state);
	
}
