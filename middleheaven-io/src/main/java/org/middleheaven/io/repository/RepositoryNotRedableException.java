/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.io.repository;

import org.middleheaven.io.ManagedIOException;


public class RepositoryNotRedableException extends ManagedIOException{

    public RepositoryNotRedableException(String repositoyClassName) {
        super(repositoyClassName + " repository is not readable");
    }

}
