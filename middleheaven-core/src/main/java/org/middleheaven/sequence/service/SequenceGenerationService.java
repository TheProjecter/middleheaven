/*
 * Created on 2006/09/23
 *
 */
package org.middleheaven.sequence.service;

import org.middleheaven.sequence.Sequence;
import org.middleheaven.sequence.SequenceScope;


public interface SequenceGenerationService{

    /**
     * Returns a sequence that correspondes to the name
     * @param name the sequence's name
     * @return a <code>Sequence</code>
     */
    public Sequence<?> getSequence(String name);
   
    
    /**
     * Returns a sequence that correspondes to the name
     * exist in the scope and may be shared.
     * @param name sequence's name
     * @param shared means that several clients use the values from the sequence.Shared sequences are trasnactional
     * @param scope the scope for the sequence
     * @return
     */
    public Sequence<?> getSequence(String name , boolean shared , SequenceScope scope);
}
