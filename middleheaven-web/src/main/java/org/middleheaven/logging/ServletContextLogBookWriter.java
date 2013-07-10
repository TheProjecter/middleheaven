
package org.middleheaven.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.ServletContext;

import org.middleheaven.logging.writters.FormatableLogWriter;
import org.middleheaven.logging.writters.LogFormat;
import org.middleheaven.logging.writters.SimpleLogFormat;


public class ServletContextLogBookWriter extends LogBookWriter implements FormatableLogWriter{

	private ServletContext context;
    protected LogFormat format = new SimpleLogFormat();

    
	public ServletContextLogBookWriter(ServletContext context) {
		this.context = context;
	}

	@Override
	public void config(Map<String, String> params, LoggingConfiguration configuration) {
	}

	@Override
	public void write(LoggingEvent event) throws LogWritingException {

		final StringWriter writer = new StringWriter();
		final PrintWriter pWriter = new PrintWriter(writer);
		format.format(event, pWriter);
		
		if (event.hasThrowable()){
			context.log(writer.getBuffer().toString());
		} else {
			context.log(writer.getBuffer().toString(),event.getThrowable());	
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLogFormat(LogFormat format) {
		this.format = format;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LogFormat getLogFormat() {
		return this.format;
	}

}
