package org.middleheaven.quantity.math.structure;

/**
 * Represent a mathematical Group Additive structure.
 * 
 * @param <A> the type of the element in the group.
 */
public interface GroupAdditiveElement<A extends GroupAdditiveElement<A>> extends AlgebricStructureElement<A>{

	/**
	 * Returns the sum of this object with the one specified.
	 * @param other the object to be added
	 * @return this + other
	 */
	public A plus(A other);
	
	/**
	 * Returns the additive inverse of this object.
	 * @return -this
	 */
	public A negate();
	
	/**
	 * Returns the sum of this object with the additive inverse of the one specified.
	 * @param other the object to be added
	 * @return this - other. 
	 */
	public A minus(A other);
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public GroupAdditive<A> getAlgebricStructure();

	/**
	 * @return <code>true</code> if this.equals(this.getAlgebricStructure().zero()), <code>false</code> otherwise.
	 */
	public boolean isZero();
	
}
