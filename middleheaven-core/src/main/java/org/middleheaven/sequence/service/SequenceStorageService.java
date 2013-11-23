/*
 * Created on 2006/09/23
 *
 */
package org.middleheaven.sequence.service;

import org.middleheaven.sequence.SequenceState;
import org.middleheaven.sequence.SequenceStateListener;
import org.middleheaven.sequence.StatePersistentSequence;



public interface SequenceStorageService extends SequenceStateListener{

    public void store(SequenceState state);
    public void restore(StatePersistentSequence<?> sequence);

}
