package org.middleheaven.util.identity;

import org.middleheaven.sequence.DefaultToken;
import org.middleheaven.sequence.RandomSequence;
import org.middleheaven.sequence.SequenceToken;

public class UUIDIdentitySequence implements IdentitySequence<UUIDIdentity>, RandomSequence<UUIDIdentity>{

	public UUIDIdentitySequence(){}
	
	@Override
	public SequenceToken<UUIDIdentity> next() {
		return new DefaultToken<UUIDIdentity>(UUIDIdentity.next());
	}

}
