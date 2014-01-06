package org.middleheaven.util.coersion;

public class CoersionException extends RuntimeException {

	public CoersionException(Exception e) {
		super(e);
	}

	public CoersionException(String msg) {
		super(msg);
	}

}
