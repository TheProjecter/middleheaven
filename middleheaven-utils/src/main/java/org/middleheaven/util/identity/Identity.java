package org.middleheaven.util.identity;

import java.io.Serializable;

/**
 * Represents an object's identity.
 * 
 */
public abstract class Identity implements Serializable{


	private static final long serialVersionUID = 6343097297742844651L;

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public final boolean equals(Object other){
		return other !=null && this.getClass().isAssignableFrom(other.getClass()) && equalsIdentity((Identity)other);
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public abstract int hashCode();

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public abstract String toString();
	
	
	/**
	 * Provides the specific equals comparison algorithm. 
	 * @param other the {@link Identity} to check for equallty.
	 * @return <code>true</code> if the this object is equal to <code>other</code>, <code>false</code> otherwise.
	 */
	protected abstract boolean equalsIdentity(Identity other);
	
}
