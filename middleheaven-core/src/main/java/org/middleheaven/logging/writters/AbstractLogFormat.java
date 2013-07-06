/**
 * 
 */
package org.middleheaven.logging.writters;

import java.io.PrintWriter;
import java.text.MessageFormat;

import org.middleheaven.logging.LogBookWriter;
import org.middleheaven.logging.LogWritingIOExcepiton;
import org.middleheaven.logging.LoggingEvent;

/**
 * 
 */
public abstract class AbstractLogFormat implements LogFormat {

	
	public AbstractLogFormat (){}
	
    protected String fillParamterHolders(LoggingEvent event){
    	CharSequence message = event.getMessage();
    	Object [] params = event.getMessageParameters();
    	if (params.length ==0){
    		return message.toString();
    	}
    	return MessageFormat.format(message.toString(), params);
    }

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setWriter(LogBookWriter writer) {}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writerHeader(PrintWriter writer) throws LogWritingIOExcepiton {}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writerFooter(PrintWriter writer) throws LogWritingIOExcepiton {}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getContentType() {
        return "text/plain";
    }

}
