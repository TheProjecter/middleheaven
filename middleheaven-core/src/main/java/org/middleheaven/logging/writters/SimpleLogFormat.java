/**
 * 
 */
package org.middleheaven.logging.writters;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.middleheaven.logging.LogWritingIOExcepiton;
import org.middleheaven.logging.LoggingEvent;

/**
 * 
 */
public class SimpleLogFormat extends AbstractLogFormat {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void format(LoggingEvent event, PrintWriter writer) throws LogWritingIOExcepiton {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		writer.append("[").append(event.getLevel().toString()).append(" ").append(format.format(new Date(event.getTime()))).append("]").append(fillParamterHolders(event));
		writer.println();
		if (event.hasThrowable()){
        	writer.println("----");
            event.getThrowable().printStackTrace(writer);
            writer.println("----");
        }
	}


}
