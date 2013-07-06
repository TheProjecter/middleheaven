package org.middleheaven.logging.writters;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.middleheaven.logging.LogBookWriter;
import org.middleheaven.logging.LogWritingException;
import org.middleheaven.logging.LogWritingIOExcepiton;
import org.middleheaven.logging.LoggingEvent;


/**
 * @author  Sergio M. M. Taborda 
 */
public abstract class StreamLogBookWriter extends LogBookWriter implements FormatableLogWriter{

    protected LogFormat format = new SimpleLogFormat();

	public StreamLogBookWriter(){
		
	}
	
	protected abstract OutputStream getStream();
	
    @Override public void write(LoggingEvent event) throws LogWritingException {
        OutputStream out = getStream();
    	this.format.format(event,new PrintWriter(out));
        try {
        	out.flush();
        } catch (IOException e) {
            throw new LogWritingIOExcepiton(e);
        }
    }

    public void setLogFormat(LogFormat format) {
        this.format = format;
        this.format.setWriter(this);
    }

    public LogFormat getLogFormat() {
        return format;
    }

}
