/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.io;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.middleheaven.io.repository.FileNotFoundManagedException;


public class ManagedIOException extends RuntimeException {

    
	private static final long serialVersionUID = 5373879198361157788L;

	public static ManagedIOException manage(IOException ioe) {
        if (ioe instanceof FileNotFoundException){
            return new FileNotFoundManagedException(ioe.getMessage());
        }
        return new ManagedIOException(ioe);
    }
    
    protected ManagedIOException (Throwable cause){
        super(cause);
    }
    
    protected ManagedIOException (String msg){
        super(msg);
    }
    
    protected ManagedIOException (String msg,Throwable cause){
        super(msg,cause);
    }
    
}
