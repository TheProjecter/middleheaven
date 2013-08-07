/**
 * 
 */
package org.middleheaven.global.text;

/**
 * Formats an object value to a {@link String} and back.
 * 
 * @param <T> the type of the formated object.
 */
public interface ParsableFormatter<T> extends Formatter<T> {

    /**
     * Retrives an object from it's formated {@link String} value.
     * @param stringValue the object formated string value.
     * @return the object represented by the formated string.
     */
    public T parse(String stringValue);
}
