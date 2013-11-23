package org.middleheaven.process.progress;

import java.io.Serializable;


/**
 * Tracks a process progress.
 * 
 */
public interface Progress extends Serializable{

    /**
     * 
     * @return <code>true</code> if the process is terminated, <code>false</code> otherwise
     */
    public boolean isTerminated();
    
    
    /**
     * Interrupts the process, if possible.
     * @throws IllegalStateException if was not possible to interrupt the process.
     */
    public void interrupt();
    
    /**
     * 
     * @return <code>true</code> if the underlying process can be interrupted,  <code>false</code> otherwise
     */
    public boolean isInterruptable();
    
    
    /**
     * @return the process steps count. Returns a negative number if  the quantity of steps
     * is unkown. 
     */
    public int stepsCount();
    
    /**
     *  
     * @return current step number. Zero means the process is yet to start.
     */
    public int getCurrentStepIndex();

    /**
     * 
     * @return current step description
     */
    public CharSequence getCurrentStepDescription();
    
    /**
     * Increment the current step by one.
     */
    public void increment();
  
    
    public void addProgressListener(ProgressListener observer);
    public void removeProgressListener(ProgressListener observer);
}
