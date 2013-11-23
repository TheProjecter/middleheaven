package org.middleheaven.global.atlas;

import java.io.Serializable;

import org.middleheaven.culture.IsoCode;

public abstract class Town  extends AbstractAtlasLocale implements Serializable{

	AtlasLocale parent;
	
	protected Town(AtlasLocale parent , IsoCode isoCode,String name){
		super(parent,isoCode,name);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other){
		return other instanceof Town && equalsOther((Town)other);
	}
	
	/**
	 * 
	 * @param other
	 * @return
	 */
	private boolean equalsOther(Town other){
		return this.ISOCode().equals(other.ISOCode()); 
	}
	
	@Override
	public boolean isCountry() {
		return false;
	}

	@Override
	public boolean isDivision() {
		return false;
	}

	@Override
	public boolean isTown() {
		return true;
	}



}
