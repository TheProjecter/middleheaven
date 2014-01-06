/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.sequence;

/**
 * A token of the sequence. This intermediary object 
 * is necessary for further control of sequence values in 
 * a managed environment. 
 * 
 * @param <T> The sequence value type
 */
public interface SequenceToken <T> {

    public T value();
}
