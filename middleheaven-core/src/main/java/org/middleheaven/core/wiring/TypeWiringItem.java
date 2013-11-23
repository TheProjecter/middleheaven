/**
 * 
 */
package org.middleheaven.core.wiring;

import org.middleheaven.util.Hash;


/**
 * 
 */
public final class TypeWiringItem implements WiringItem {

	private Class<?> type;

	public TypeWiringItem (Class<?> type){
		this.type = type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getItem() {
		return type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isType() {
		return true;
	}

	
	public int hashCode(){
		return Hash.hash(this.type).hashCode();
	}
	
	public boolean equals(Object other){
		return (other instanceof TypeWiringItem) && ((TypeWiringItem) other).type.equals(this.type);
	}
}
