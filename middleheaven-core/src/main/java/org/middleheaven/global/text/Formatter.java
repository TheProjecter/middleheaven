/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.global.text;

import java.io.Serializable;

/**
 * Formats an object value to a {@link String}.
 * The {@link Formatter} do not was to be able to parse back to the object form. 
 * For a type of {@link Formatter} able to do that see: {@link ParsableFormatter}.
 * 
 * @param <T> the type of the formated object.
 */
public interface Formatter<T> extends Serializable {

	/**
	 * Formats the object value as a {@link String}.
	 * @param object object to be formated
	 * @return the value of the object as a formated {@link String}.
	 */
    public String format(T object);
    

    
}
