package org.middleheaven.sequence.service;

import org.middleheaven.events.EventListenersSet;
import org.middleheaven.sequence.SequenceState;
import org.middleheaven.sequence.SequenceStateListener;
import org.middleheaven.sequence.StateChangedEvent;
import org.middleheaven.sequence.StatePersistentSequence;

public abstract class AbstractStatePersistanteSequence<T> implements StatePersistentSequence<T> {


    private EventListenersSet<SequenceStateListener> listeners = EventListenersSet.newSet(SequenceStateListener.class);
    private String name;
    
	protected AbstractStatePersistanteSequence(String name) {
		this.name= name;
	}

    protected void fireStateChange(SequenceState state){
    	listeners.broadcastEvent().onStateChanged(new StateChangedEvent(state));
    }
    
	@Override
	public void addSequenceStateListener(SequenceStateListener listener) {
		listeners.addListener(listener);
	}

	@Override
	public String getName(){
		return name;
	}


}
