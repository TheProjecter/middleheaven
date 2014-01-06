package org.middleheaven.util.identity;

import org.middleheaven.sequence.DefaultToken;
import org.middleheaven.sequence.SequenceState;
import org.middleheaven.sequence.SequenceToken;
import org.middleheaven.sequence.StateEditableSequence;

/**
 * 
 */
public final class IntegerIdentitySequence implements IdentitySequence<IntegerIdentity> , StateEditableSequence<IntegerIdentity> {

	private IntegerIdentity current = IntegerIdentity.valueOf(0);

	public IntegerIdentitySequence(){}
	

	@Override
	public SequenceToken<IntegerIdentity> next() {
		current = current.next();
		return new DefaultToken<IntegerIdentity>(current);
	}


	@Override
	public SequenceState getSequenceState() {
		return new SequenceState(current);
	}


	@Override
	public void setSequenceState(SequenceState state) {
		current = (IntegerIdentity)state.getLastUsedValue();
	}

}
