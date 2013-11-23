
package org.middleheaven.logging;

import org.middleheaven.core.bootstrap.ServiceRegistry;





/**
 * Provides a convinent set of method to create and send {@link LoggingEvent}s to a {@link LoggingService}.
 * 
 */
public abstract class Logger {


	/**
	 * Acquires a {@link Logger} for a specific log book. 
	 * 
	 * @param bookName the book's name
	 * 
	 * @return a {@link Logger} that will log events on the given book.
	 */
	public static Logger onBook(String bookName){
		
		return new LogServiceDelegatorLogger(bookName, ServiceRegistry.getService(LoggingService.class));
		
	}
	
	public static Logger onBookFor(Class<?> type){
		return onBook(type.getName());
	}
	
	private String name;

	protected Logger(String name){
		this.name = name;
	}

	public final void fatal(CharSequence msg, Object ... params) {
		fatal(null,msg,params);
	}
	
	public final void fatal(CharSequence msg, LoggingEventParametersCollector collector) {
		fatal(null,msg,collector);
	}

	public void fatal(Throwable throwable, CharSequence message,Object ... params) {
		fatal(throwable, message, new ObjectArrayLoggingEventParametersCollector(params));
	}
	
	public void fatal(Throwable throwable, CharSequence message,LoggingEventParametersCollector collector) {
		log(new LoggingEvent(name, LoggingLevel.FATAL,message,throwable,collector));
	}

	public final void error(CharSequence msg, Object ... params) {
		error(null,msg,params);
	}
	
	public final void error(CharSequence msg, LoggingEventParametersCollector collector) {
		error(null,msg,collector);
	}

	public void error(Throwable throwable, CharSequence message,Object ... params) {
		error(throwable, message, new ObjectArrayLoggingEventParametersCollector(params));
	}
	
	public void error(Throwable throwable, CharSequence message,LoggingEventParametersCollector collector) {
		log(new LoggingEvent(name, LoggingLevel.ERROR,message,throwable,collector));
	}

	public final void warn(CharSequence msg, Object ... params) {
		warn(null,msg,params);
	}
	
	public final void warn(CharSequence msg, LoggingEventParametersCollector collector) {
		warn(null,msg,collector);
	}

	public void warn(Throwable throwable, CharSequence message,Object ... params) {
		warn(throwable, message, new ObjectArrayLoggingEventParametersCollector(params));
	}
	
	public void warn(Throwable throwable, CharSequence message,LoggingEventParametersCollector collector) {
		log(new LoggingEvent(name, LoggingLevel.WARN,message,throwable,collector));
	}

	public final void info(CharSequence msg, Object ... params) {
		info(null,msg,params);
	}
	
	public final void info(CharSequence msg, LoggingEventParametersCollector collector) {
		info(null,msg,collector);
	}

	public void info(Throwable throwable, CharSequence message,Object ... params) {
		info(throwable, message, new ObjectArrayLoggingEventParametersCollector(params));
	}
	
	public void info(Throwable throwable, CharSequence message,LoggingEventParametersCollector collector) {
		log(new LoggingEvent(name, LoggingLevel.INFO,message,throwable,collector));
	}
	
	public final void debug(CharSequence msg, Object ... params) {
		debug(null,msg,params);
	}
	
	public final void debug(CharSequence msg, LoggingEventParametersCollector collector) {
		debug(null,msg,collector);
	}

	public void debug(Throwable throwable, CharSequence message,Object ... params) {
		debug(throwable, message, new ObjectArrayLoggingEventParametersCollector(params));
	}
	
	public void debug(Throwable throwable, CharSequence message,LoggingEventParametersCollector collector) {
		log(new LoggingEvent(name, LoggingLevel.DEBUG,message,throwable,collector));
	}
	
	public final void trace(CharSequence msg, Object ... params) {
		trace(null,msg,params);
	}
	
	public final void trace(CharSequence msg, LoggingEventParametersCollector collector) {
		trace(null,msg,collector);
	}

	public void trace(Throwable throwable, CharSequence message,Object ... params) {
		trace(throwable, message, new ObjectArrayLoggingEventParametersCollector(params));
	}
	
	public void trace(Throwable throwable, CharSequence message,LoggingEventParametersCollector collector) {
		log(new LoggingEvent(name, LoggingLevel.TRACE,message,throwable,collector));
	}

	
	public final void log(LoggingEvent event){
		doLog(event);
	}

	protected abstract void doLog(LoggingEvent event);

}
