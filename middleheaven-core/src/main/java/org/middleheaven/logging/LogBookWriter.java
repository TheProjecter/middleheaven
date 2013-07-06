
package org.middleheaven.logging;

import java.util.Map;


/**
 * Writes the LoggingEvent on to a book media. 
 */
public abstract class LogBookWriter {

    protected LoggingLevel level = LoggingLevel.ALL;


    public LoggingLevel getLevel(){
        return level;
    }

  
    public void setLevel(LoggingLevel level){
        this.level = level;
    }

    public final void log(LoggingEvent event){
        if (this.level.canLog(event.getLevel())){
            write(event);
        }
    }

    public abstract void config(Map<String,String> params, LoggingConfiguration configuration);
    public abstract void write(LoggingEvent event) throws LogWritingException;

}
