/*
 * Created on 5/Ago/2006
 *
 */
package org.middleheaven.reflection;

/**
 * Represents a {@link ClassNotFoundException} in the {@link ReflectionException} tree.
 */
public class NoSuchClassReflectionException extends ClassloadingException {


	private static final long serialVersionUID = -9020721820233512164L;
	
	protected final String className;
	  
	public NoSuchClassReflectionException(String className, Exception cause){
	   super(className + " could not be found on the classpath", cause);
	   this.className =  className;
	}
	  
    public NoSuchClassReflectionException(String className){
        super(className + " could not be found on the classpath");
        this.className =  className;
    }


    public String getClassName() {
        return className;
    }


}
