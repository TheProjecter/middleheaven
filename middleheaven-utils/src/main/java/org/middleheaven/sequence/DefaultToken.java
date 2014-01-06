package org.middleheaven.sequence;

/**
 * Generic implementation of <code>SequenceToken</code>. The value is stored at creation an retrieved with <code>getValue()</code>
 * 
 *
 * @param <T> The sequence value type
 */
public class DefaultToken<T> implements SequenceToken<T> {

	
	private T value;
	
	public DefaultToken(T value){
		this.value = value;
	}
	
	@Override
	public T value() {
		return value;
	}

}
