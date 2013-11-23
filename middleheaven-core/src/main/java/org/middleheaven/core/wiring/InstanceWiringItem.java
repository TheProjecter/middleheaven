/**
 * 
 */
package org.middleheaven.core.wiring;

import org.middleheaven.util.Hash;


/**
 * 
 */
public final class InstanceWiringItem implements WiringItem {

	
	private Object item;
	
	public InstanceWiringItem (Object item){
		this.item = item;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getItem() {
		return item;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isType() {
		return false;
	}
	
	public int hashCode(){
		return Hash.hash(this.item).hashCode();
	}
	
	public boolean equals(Object other){
		return (other instanceof InstanceWiringItem) && ((InstanceWiringItem) other).item.equals(this.item);
	}

}
