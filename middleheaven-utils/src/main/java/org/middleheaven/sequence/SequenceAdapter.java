package org.middleheaven.sequence;

public abstract class SequenceAdapter<T> implements Sequence<T>{

	private Sequence<T> baseSequence;
	
	public SequenceAdapter(StatePersistentSequence<T> baseSequence) {
		super();
		this.baseSequence = baseSequence;
	}
	
	protected Sequence<T> baseSequence(){
		return this.baseSequence;
	}
	
	@Override
	public SequenceToken<T> next() {
		return baseSequence.next();
	}
	




}
