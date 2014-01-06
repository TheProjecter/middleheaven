/*
 * Created on 2006/09/23
 *
 */
package org.middleheaven.sequence;

/**
 * A <code>Sequence</code> who's state is preserved 
 * between uses. 
 * Althought implementations are free to implement the persistence logic
 * as they want this sequence is intended to a
 * strictly increasing sequence;
 * 
 * @author Sergio M. M. Taborda 
 *
 */
public interface StatePersistentSequence<T> extends StateEditableSequence<T> , NamedSequence<T> {


	/**
	 * Allows other objects to listener to this sequence state change.
	 * Sequence storage 
	 * @param listener
	 */
	public void addSequenceStateListener (SequenceStateListener listener);

    
    
    
}
