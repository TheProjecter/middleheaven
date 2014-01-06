/**
 * 
 */
package org.middleheaven.quantity.unit;

import org.middleheaven.util.Hash;

class UnitPower implements Comparable<UnitPower>{



	Unit unit;
	
	int exponent;
	
	public UnitPower(Unit unit, int exponent){
		this.unit = unit;
		this.exponent = exponent;
	}
	
	public int exponent() {
		return exponent;
	}

	public Unit unit() {
		return unit;
	}

	public String toString(){
		StringBuilder builder = new StringBuilder().append(this.unit);
		if (this.exponent!=1){
			builder.append('^').append(exponent);
		}
		return builder.toString();
	}

	public int compareTo(UnitPower other) {
		return this.unit.symbol().compareTo(other.unit.symbol());
	}
	
	public boolean equals(Object other){
		return other instanceof UnitPower && equalsOther((UnitPower)other);
	}
	
	/**
	 * @param other
	 * @return
	 */
	private boolean equalsOther(UnitPower other) {
		return this.exponent == other.exponent && this.unit.equals(other.unit);
	}

	public int hashCode(){
		return Hash.hash(exponent).hash(unit).hashCode();
	}
}