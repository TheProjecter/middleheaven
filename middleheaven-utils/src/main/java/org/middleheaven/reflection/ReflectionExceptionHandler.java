package org.middleheaven.reflection;

import java.lang.reflect.InvocationTargetException;

import org.middleheaven.core.exception.ExceptionHandler;

public class ReflectionExceptionHandler implements ExceptionHandler<Exception,RuntimeException> {

	@Override
	public RuntimeException handle(Exception exception) {
		try{
			throw exception;
		} catch (IllegalAccessException e) {
			return new IllegalAccessReflectionException(e);
		} catch (IllegalArgumentException e) {
			return new IllegalAccessReflectionException(e);
		} catch (NoSuchMethodException e) {
			return new NoSuchMethodReflectionException(e);
		} catch (InvocationTargetException e) {
			return new InvocationTargetReflectionException(e);
		} catch (Exception e){
			return new ReflectionException(e);
		}
	}

}
