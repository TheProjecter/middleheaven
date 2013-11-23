package org.middleheaven.process.progress;


public class ExternalControlledProgress extends AbstractProgress implements ControlableProgress {

    int count=100;
    int current=0;
    boolean stopable = false;
    
    CharSequence status="Processing...";
    
    public ExternalControlledProgress(){}
    
    public ExternalControlledProgress(int count){
        this.count= count;
    }
    
    public void setActionCount(int count){
        this.count= count;
    }
    
    public int stepsCount() {
        return count;
    }
    
    public int getCurrentStepIndex() {
        return current;
    }

    public void increment() {
        current++;
        this.fireProgressChange();
    }

    public void increment(String status) {
        current++;
        this.fireProgressChange();
        this.status = status;
    }
    
    public boolean isInterruptable() {
        return stopable;
    }

    public void setStopable(boolean value) {
        stopable = value;
    }
    
    public boolean isTerminated() {
        return current>=count;
    }

    public void interrupt() {
        current = count;
        this.fireProgressChange();
    }

    public void setStepDescription(CharSequence sequence) {
        status = sequence.toString();
    }

    public void terminate() {
        current = count;
        this.fireProgressChange();
    }

	@Override
	public void setCurrentStepDescription(CharSequence description) {
		this.status = description;
	}

	@Override
	public CharSequence getCurrentStepDescription() {
		return status;
	}

}
