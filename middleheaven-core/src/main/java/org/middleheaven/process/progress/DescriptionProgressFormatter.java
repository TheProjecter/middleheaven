package org.middleheaven.process.progress;

public class DescriptionProgressFormatter extends ProgressFormatter {

    public String format(Progress progress) {
        CharSequence s = progress.getCurrentStepDescription();
        if (s==null) {
        	return " ";
        }
        return s.toString();
    }
    
    public Object clone(){
        return this; // statelss
    }
}
