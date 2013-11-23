package org.middleheaven.process.progress;

public abstract class ProgressFormatter {

    public ProgressFormatter(){
        
    }
    
    protected ProgressFormatter(ProgressFormatter other){

    }
    
    public abstract String format(Progress progress);
    
    public String format(Object object) {
        return format((Progress)object);
    }



  


}
