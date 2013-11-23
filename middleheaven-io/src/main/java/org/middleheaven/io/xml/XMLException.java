/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.io.xml;

import org.middleheaven.io.ManagedIOException;

public class XMLException extends ManagedIOException {

    protected XMLException(Throwable cause) {
        super(cause);
    }

    protected XMLException(String cause) {
        super(cause);
    }
}
