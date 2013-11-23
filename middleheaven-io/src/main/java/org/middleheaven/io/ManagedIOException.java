/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;


/**
 * Wrapping exception for all {@link IOException} like exception.
 */
public class ManagedIOException extends RuntimeException {

    
	private static final long serialVersionUID = 5373879198361157788L;

	/**
	 * 
	 * @param exception {@link IOException} that is to be handled.
	 * @return
	 */
	public static ManagedIOException manage(IOException exception) {
        if (exception instanceof FileNotFoundException){
            return new FileNotFoundManagedException(exception.getMessage());
        } else if  (exception instanceof SocketTimeoutException){
            return new RemoteComunicationTimeoutException(exception.getMessage());
        } 
        return new ManagedIOException(exception);
    }
    
    public ManagedIOException (Throwable cause){
        super(cause);
    }
    
    public ManagedIOException (String msg){
        super(msg);
    }
    
    public ManagedIOException (String msg,Throwable cause){
        super(msg,cause);
    }
    
}
