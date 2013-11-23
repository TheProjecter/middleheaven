package org.middleheaven.process.progress;


public class WaitProgress extends AbstractProgress implements ControlableProgress {

    private boolean terminated = false;
    private CharSequence caption;
    
    public WaitProgress(CharSequence caption){
        this.caption = caption;
    }
    

    public int getCurrentStepIndex() {
        return 0;
    }

    public void setStepDescription(CharSequence sequence){
        caption = sequence.toString();
        this.fireProgressChange();
    }
    
    public CharSequence getCurrentStepDescription() {
        return caption;
    }

    public int stepsCount() {
        return -1; // indeterminated duration 
    }

    public void increment() {}


    public boolean isTerminated() {
        return terminated;
    }
    
    public void terminate(){
        terminated = true;
        this.fireProgressChange();
    }

    public boolean isInterruptable() {
        return false;
    }
    
    public void interrupt(){
        throw new IllegalStateException("WaitProgress is not stopable.");
    }


	@Override
	public void setCurrentStepDescription(CharSequence description) {
		this.caption = description;
	}
    

}
