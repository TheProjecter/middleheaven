package org.middleheaven.logging.writters;

import java.io.OutputStream;
import java.io.PrintWriter;

import org.middleheaven.logging.LogBookWriter;
import org.middleheaven.logging.LogWritingException;
import org.middleheaven.logging.LoggingEvent;


/**
 * @author  Sergio M. M. Taborda 
 */
public abstract class StreamLogBookWriter extends LogBookWriter implements FormatableLogWriter{

    private LogFormat format = new SimpleLogFormat();

	public StreamLogBookWriter(){
		
	}
	
	protected abstract OutputStream getStream();
	
    @Override public void write(LoggingEvent event) throws LogWritingException {
        OutputStream out = getStream();
        PrintWriter writer = new PrintWriter(out);
    	this.format.format(event,writer);
        writer.flush();
    }

    public void setLogFormat(LogFormat format) {
        this.format = format;
        this.format.setWriter(this);
    }

    public LogFormat getLogFormat() {
        return format;
    }

}
