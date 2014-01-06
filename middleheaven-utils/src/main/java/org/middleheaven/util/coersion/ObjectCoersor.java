package org.middleheaven.util.coersion;

/**
 * 
 * 
 *
 */
public interface ObjectCoersor<O> {

	 public <T> T convert(O value, Class<T> targetClass);
}
