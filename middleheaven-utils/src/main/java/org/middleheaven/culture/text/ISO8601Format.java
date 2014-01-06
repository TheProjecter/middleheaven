/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.culture.text;

import java.text.SimpleDateFormat;

public class ISO8601Format extends SimpleDateFormat {

	private static final long serialVersionUID = 4935280140531119801L;

	public ISO8601Format(){
        super("yyyy-MM-dd'T'HH:mm:ss.SSS");
    }
}
