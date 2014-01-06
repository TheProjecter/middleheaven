/*
 * Created on 5/Ago/2006
 *
 */
package org.middleheaven.reflection;

public class ClassloadingException extends ReflectionException {

	private static final long serialVersionUID = 9177321454586747094L;

	public ClassloadingException(String msg) {
        super(msg);
    }

	public ClassloadingException(Exception e) {
		super(e);
	}
	
	public ClassloadingException(String msg, Exception e) {
		super(msg, e);
	}

}
