/*
 * Created on 16/08/2005
 */
package org.middleheaven.logging;

import java.io.OutputStream;
import java.util.Map;

import org.middleheaven.logging.writters.StreamLogBookWriter;

/**
 *  A writer that outputs to the console.
 */
class ConsoleLogWriter extends StreamLogBookWriter {

	/**
	 * 
	 * Constructor.
	 */
    ConsoleLogWriter(){ }


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected OutputStream getStream() {
		return System.out;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void config(Map<String, String> params,LoggingConfiguration configuration) {}

}
