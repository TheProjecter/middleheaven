package org.middleheaven.quantity.unit;



/**
 * Implements Unit as a pair Dimension , simbol
 * 
 *
 */
class BaseUnit<E extends Measurable> extends Unit<E> {

	private Dimension<E> dim;
	private String symbol;
	
	protected BaseUnit(Dimension<E> dim, String symbol){
		this.dim = dim;
		this.symbol = symbol;
	}
	
	public Dimension<E> dimension() {
		return dim;
	}
	
	public String symbol() {
		return symbol;
	}
	
	public boolean equalsOther(Unit<?> other) {
		return this.symbol.equals(other.symbol()) && this.dim.equals(other.dimension());
	}
	


	




	
}
