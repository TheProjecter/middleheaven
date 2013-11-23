package org.middleheaven.sequence.service;

import org.middleheaven.sequence.SequenceState;
import org.middleheaven.sequence.SequenceToken;
import org.middleheaven.sequence.StateEditableSequence;


public class NamedSequenceAdapter<T> extends AbstractStatePersistanteSequence<T> {


	private StateEditableSequence<T> baseSequence;
	
	public NamedSequenceAdapter(String name, StateEditableSequence<T> baseSequence) {
		super(name);
		this.baseSequence = baseSequence;
	}

	@Override
	public SequenceState getSequenceState() {
		return new SequenceState(this.getName(), baseSequence.getSequenceState().getLastUsedValue());
	}


	@Override
	public void setSequenceState(SequenceState state) {

		baseSequence.setSequenceState(state);
	}


	@Override
	public SequenceToken<T> next() {
		return baseSequence.next();
	}
}
