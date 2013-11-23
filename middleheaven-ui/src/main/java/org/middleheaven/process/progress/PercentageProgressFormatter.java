package org.middleheaven.process.progress;


public class PercentageProgressFormatter extends ProgressFormatter {

 
    public String format(Progress progress) {
        return progress.getCurrentStepIndex() + "/" + progress.stepsCount();
    }
    
    public Object clone(){
        return this; // statelss
    }

}
