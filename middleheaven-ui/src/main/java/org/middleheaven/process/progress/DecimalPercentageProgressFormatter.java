package org.middleheaven.process.progress;

import java.text.NumberFormat;

public class DecimalPercentageProgressFormatter extends ProgressFormatter{

    NumberFormat df;
    public DecimalPercentageProgressFormatter(){
    	// TODO
     //  this.locale= LocalizationManager.getLocalizationModel().getNumberLocale();
     //  df =  NumberFormat.getPercentInstance(this.locale);
    }
    
    private DecimalPercentageProgressFormatter(DecimalPercentageProgressFormatter other){
        super(other);
        this.df = other.df;
    }
    
    public Object clone(){
        return new DecimalPercentageProgressFormatter(this);
    }
    
    public String format(Progress progress) {
        return df.format((progress.getCurrentStepIndex()*1D)/progress.stepsCount());
    }

  
}
