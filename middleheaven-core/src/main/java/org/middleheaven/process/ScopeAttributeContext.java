/**
 * 
 */
package org.middleheaven.process;

import org.middleheaven.collections.Enumerable;

/**
 * 
 */
public interface ScopeAttributeContext extends Enumerable<Attribute>{

	/**
	 * 
	 */
	public ContextScope getContextScope();
	
	/**
	 * Gets the attribute.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param scope
	 *            the scope
	 * @param name
	 *            the name
	 * @param type
	 *            the type
	 * @return the attribute
	 */
	public <T> T getAttribute(String name, Class<T> type);
	
	/**
	 * Sets the attribute.
	 * 
	 * @param scope
	 *            the scope
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public void setAttribute(String name, Object value);

	/**
	 * @param name
	 */
	public void removeAttribute(String name);
	
	
	
}
