/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.io.xml;

/**
 * Exception throwned when a resquested attribute does not existe in the xml document.
 */
public class XMLAttributemissingException extends XMLException {


	private static final long serialVersionUID = 1192100347391642740L;

	protected XMLAttributemissingException(String attributeName, String elementName) {
        super("Required attribute '" + attributeName + "' for element '" + elementName + "' is missing");
    }

}
