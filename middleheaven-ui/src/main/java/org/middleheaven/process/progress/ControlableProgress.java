package org.middleheaven.process.progress;

public interface ControlableProgress extends Progress {

    
    /**
     * Terminates the process. 
     * TODO explain diference from interrupt 
     */
    public void terminate();
    
    /**
     * Changes the current step description.
     * @param description
     */
    public void setCurrentStepDescription(CharSequence description);
    
}
