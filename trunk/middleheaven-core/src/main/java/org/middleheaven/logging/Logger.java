
package org.middleheaven.logging;





/**
 * Provides a convinent set of method to create and send {@link LoggingEvent} to the {@link LoggingService}.
 * 
 */
public abstract class Logger {

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
		fatal(null,msg,params);
	}
	
	public final void error(CharSequence msg, LoggingEventParametersCollector collector) {
		fatal(null,msg,collector);
	}

	public void error(Throwable throwable, CharSequence message,Object ... params) {
		fatal(throwable, message, new ObjectArrayLoggingEventParametersCollector(params));
	}
	
	public void error(Throwable throwable, CharSequence message,LoggingEventParametersCollector collector) {
		log(new LoggingEvent(name, LoggingLevel.FATAL,message,throwable,collector));
	}

	public final void warn(CharSequence msg, Object ... params) {
		fatal(null,msg,params);
	}
	
	public final void warn(CharSequence msg, LoggingEventParametersCollector collector) {
		fatal(null,msg,collector);
	}

	public void warn(Throwable throwable, CharSequence message,Object ... params) {
		fatal(throwable, message, new ObjectArrayLoggingEventParametersCollector(params));
	}
	
	public void warn(Throwable throwable, CharSequence message,LoggingEventParametersCollector collector) {
		log(new LoggingEvent(name, LoggingLevel.FATAL,message,throwable,collector));
	}

	public final void info(CharSequence msg, Object ... params) {
		fatal(null,msg,params);
	}
	
	public final void info(CharSequence msg, LoggingEventParametersCollector collector) {
		fatal(null,msg,collector);
	}

	public void info(Throwable throwable, CharSequence message,Object ... params) {
		fatal(throwable, message, new ObjectArrayLoggingEventParametersCollector(params));
	}
	
	public void info(Throwable throwable, CharSequence message,LoggingEventParametersCollector collector) {
		log(new LoggingEvent(name, LoggingLevel.FATAL,message,throwable,collector));
	}
	
	public final void debug(CharSequence msg, Object ... params) {
		fatal(null,msg,params);
	}
	
	public final void debug(CharSequence msg, LoggingEventParametersCollector collector) {
		fatal(null,msg,collector);
	}

	public void debug(Throwable throwable, CharSequence message,Object ... params) {
		fatal(throwable, message, new ObjectArrayLoggingEventParametersCollector(params));
	}
	
	public void debug(Throwable throwable, CharSequence message,LoggingEventParametersCollector collector) {
		log(new LoggingEvent(name, LoggingLevel.FATAL,message,throwable,collector));
	}
	
	public final void trace(CharSequence msg, Object ... params) {
		fatal(null,msg,params);
	}
	
	public final void trace(CharSequence msg, LoggingEventParametersCollector collector) {
		fatal(null,msg,collector);
	}

	public void trace(Throwable throwable, CharSequence message,Object ... params) {
		fatal(throwable, message, new ObjectArrayLoggingEventParametersCollector(params));
	}
	
	public void trace(Throwable throwable, CharSequence message,LoggingEventParametersCollector collector) {
		log(new LoggingEvent(name, LoggingLevel.FATAL,message,throwable,collector));
	}

	
	public final void log(LoggingEvent event){
		doLog(event);
	}

	protected abstract void doLog(LoggingEvent event);

}