/**
 * 
 */
package org.middleheaven.ui;

import java.io.Serializable;

/**
 * 
 */
public final class UIDimension implements Serializable {

	
	private static final long serialVersionUID = 1961727658321457634L;
	
	
	public float value;
	public UIDimensionUnit unit;
	
	/**
	 * 
	 * @param value
	 * @param unit
	 * @return
	 */
	public static UIDimension valueOf(float value, UIDimensionUnit unit){
		return new UIDimension(value, unit);
	}
	
	public static UIDimension pixels(int value){
		return new UIDimension(value, UIDimensionUnit.PIXELS);
	}
	
	private UIDimension(float value, UIDimensionUnit unit) {
		super();
		this.value = value;
		this.unit = unit;
	}

	/**
	 * Obtains {@link float}.
	 * @return the value
	 */
	public float getValue() {
		return value;
	}
	/**
	 * Obtains {@link UIDimensionUnit}.
	 * @return the unit
	 */
	public UIDimensionUnit getUnit() {
		return unit;
	}
	
	
}
