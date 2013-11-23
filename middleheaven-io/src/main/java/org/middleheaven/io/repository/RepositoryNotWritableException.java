/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.io.repository;

import org.middleheaven.io.ManagedIOException;


public class RepositoryNotWritableException extends ManagedIOException{

    public RepositoryNotWritableException(String repositoyClassName) {
        super(repositoyClassName + " repository is not writable");
    }

}
