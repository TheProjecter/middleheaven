package org.middleheaven.process.progress;

public class BoundProgress extends AbstractProgress{

    int stepsCount;
    int current;
    
    public BoundProgress (int stepsCount){
        this.stepsCount = stepsCount;
    }
    
    public void setMaxSteps(int stepsCount){
        this.stepsCount = stepsCount;
        fireProgressChange();
    }
    
    public int getCurrentStepIndex() {
        return current;
    }

    public String getCurrentStepDescription() {
        return null;
    }

    public int stepsCount() {
        return stepsCount;
    }

    public void increment() {
        current++;
        fireProgressChange();
    }

    public boolean isInterruptable() {
        return false;
    }

    public boolean isTerminated() {
        return current>=stepsCount;
    }

    public void interrupt() {}




}
