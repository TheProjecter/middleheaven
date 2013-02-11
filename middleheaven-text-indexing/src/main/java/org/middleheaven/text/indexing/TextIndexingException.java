package org.middleheaven.text.indexing;

/**
 * 
 */
public final class TextIndexingException extends RuntimeException{


	private static final long serialVersionUID = 3282058266172271396L;

	public static TextIndexingException handle (Throwable other){
		return new TextIndexingException(other);
	}
	
	private TextIndexingException(Throwable other){
		super(other);
	}
}
