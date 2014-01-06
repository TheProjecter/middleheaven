package org.middleheaven.sequence;

public class LongSequence implements StateEditableSequence<Long>{

	long current;
	public LongSequence(){
		this(0);
	}
	
	public LongSequence(long first){
		this.current = first;
	}
	
	
	@Override
	public synchronized SequenceToken<Long> next() {
		return new DefaultToken<Long>(current++);
	}

	@Override
	public synchronized void setSequenceState(SequenceState state) {
		this.current = ((Long)state.getLastUsedValue()).longValue();
	}

	@Override
	public synchronized SequenceState getSequenceState() {
		return new SequenceState(this.current);
	}


}
