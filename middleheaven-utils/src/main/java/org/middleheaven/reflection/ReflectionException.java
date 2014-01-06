package org.middleheaven.reflection;

import java.lang.reflect.InvocationTargetException;

public class ReflectionException extends RuntimeException {


	public ReflectionException(Throwable t){
		super(t);
	}

	public ReflectionException(String msg) {
		super(msg);
	}

	protected ReflectionException() {
	}

	/**
	 * Constructor.
	 * @param string
	 * @param e
	 */
	public ReflectionException(String message, Throwable t ) {
		super(message, t);
	}

	public static RuntimeException manage(Exception t) {
		
		try {
			throw t;
		} catch (SecurityException e) {
			return new IllegalAccessReflectionException(e);
		} catch (IllegalArgumentException e) {
			return new IllegalAccessReflectionException(e);
		} catch (InvocationTargetException e) {
			return new InvocationTargetReflectionException(e);
		} catch (InstantiationException e) {
			return new InstantiationReflectionException(e.getMessage());
		} catch (IllegalAccessException e) {
			return new IllegalAccessReflectionException(e);
		} catch(NoSuchMethodException e){
			return new NoSuchMethodReflectionException(e);
		}  catch (RuntimeException e) {
			return e;
		} catch (Exception e){
			return new ReflectionException(e);
		}
		
		
	}

}
