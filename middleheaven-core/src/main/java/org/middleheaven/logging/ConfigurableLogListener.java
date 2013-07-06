/**
 * 
 */
package org.middleheaven.logging;

import java.util.HashMap;
import java.util.Map;


/**
 * Responds to {@link LoggingEvent}s in a configurable maner.
 */
public class ConfigurableLogListener implements LoggingEventListener {


    final Map<String, LogBook> books = new HashMap<String, LogBook>();
    LoggingConfiguration configuration ;
    
    /**
     * 
     * Constructor. 
     * @param configuration configuration to use.
     * @param configurator configurator to config
     */
	public ConfigurableLogListener(LoggingConfiguration configuration , LoggingConfigurator configurator) {
		this.configuration = configuration;
		configurator.config(this, this.configuration);
	}

	/**
	 * 
	 * @return the current {@link LoggingConfiguration}.
	 */
	public LoggingConfiguration getConfiguration() {
		return configuration;
	}
	
	/**
	 * add a new book to this listener.
	 * @param book 
	 */
    public void addBook(LogBook book){
        books.put(book.name, book);
    }

    /**
     * remove a book from this listener.
     * @param book
     */
    public void removeBook(LogBook book) {
        synchronized (books){
            books.remove(book.name);
        }
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLoggingEvent(LoggingEvent event) {
		LogBook book = this.getLogBook(event.getBookName());
		if (book != null){
			book.log(event);
		}
	}
	
	// determine the logbook given aorg.middleheaven.logging.ConsoleLogBook name. 
    private final LogBook getLogBook(String name) {
        LogBook book;
        synchronized (books){
             book =  books.get(name);
             while (book==null && name.indexOf('.')>0){
            	 name = name.substring(0, name.lastIndexOf('.'));
            	 book = books.get(name);
             }
        }
        
        if (book == null){
          book =  books.get("");
        }
        
        return book;

    }
}
