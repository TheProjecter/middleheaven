/**
 * 
 */
package org.middleheaven.process.web.server;

import java.util.Enumeration;
import java.util.Iterator;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.TransformedIterator;
import org.middleheaven.process.Attribute;
import org.middleheaven.process.ContextScope;
import org.middleheaven.process.ObjectAttribute;
import org.middleheaven.process.ScopedAttributesResolutionStrategy;
import org.middleheaven.util.coersion.TypeCoercing;
import org.middleheaven.util.function.Mapper;

/**
 * 
 */
abstract class AbstractEnumerationBasedScopedAttributesResolutionStrategy implements ScopedAttributesResolutionStrategy{

	private ContextScope scope;

	public AbstractEnumerationBasedScopedAttributesResolutionStrategy (ContextScope scope){
		this.scope = scope;
	}
	
	protected abstract Enumeration<String> getEnumeration();
	
	protected abstract Object getValue(String name);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReaddable() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWritable() {
		return true;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public final int size() {
		int count = 0;
		Enumeration enumeration = this.getEnumeration();
		while (enumeration.hasMoreElements()){
			count++;
		}
		return count;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isEmpty() {
		return this.getEnumeration().hasMoreElements();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Attribute> iterator() {
		Iterator<String> it = CollectionUtils.enumationIterator(this.getEnumeration());
		
		return TransformedIterator.transform(it, new Mapper<Attribute, String>(){

			@Override
			public Attribute apply(String next) {
				return new ObjectAttribute(next, getValue(next));
			}
			
		});
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final ContextScope getScope() {
		return scope;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <T> T getAttribute(String name, Class<T> type) {
		return TypeCoercing.coerce(this.getValue(name), type);
	}

	

}
