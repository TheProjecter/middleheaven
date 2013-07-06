package org.middleheaven.logging;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;


/**
 * Stores all {@code LoggingEvent}s written to it in a {@code List}.
 * The events can be recovered in the {@code List} of {@code LoggingEvent} format.
 */
public final class ListLogBookWriter extends LogBookWriter{

	private final Deque<LoggingEvent> events = new LinkedList<LoggingEvent>();
	
	public ListLogBookWriter(){}
	
	/**
	 * No configuration is needed
	 */
	@Override
	public void config(Map<String, String> params, LoggingConfiguration configuration) {
		//no-op
	}

	@Override
	public void write(LoggingEvent event) throws LogWritingException {
		events.add(event);
	}
	
	public Collection<LoggingEvent> getEvents(){
		return events;
	}
	
	public void writeTo(LogBookWriter other){
		for (LoggingEvent event: events){
			other.write(event);
		}
	}

	public void writeTo(Logger book){
		for (LoggingEvent event: events){
			book.log(event);
		}
	}
}
