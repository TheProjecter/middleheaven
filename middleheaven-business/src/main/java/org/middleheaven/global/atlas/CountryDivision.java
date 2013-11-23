package org.middleheaven.global.atlas;

import java.io.Serializable;

import org.middleheaven.culture.IsoCode;

public abstract class CountryDivision extends AbstractAtlasLocale implements  Serializable{


	private static final long serialVersionUID = -2027599092597564580L;
	
	private CountryDivisionType type;

	protected CountryDivision(Country country, IsoCode isoCode,CountryDivisionType type){
		super(country, isoCode,isoCode.toString());
		this.type = type;
	}
    
	
	@Override
	public boolean isCountry() {
		return false;
	}

	@Override
	public boolean isDivision() {
		return true;
	}

	@Override
	public boolean isTown() {
		return false;
	}

	
	public void setName(String name){
		this.name = name;
	}
	
	public CountryDivisionType getType(){
		return type;
	}
	
	@Override
	public String getQualifiedDesignation() {
		return this.getParent().getQualifiedDesignation() + "." + super.ISOCode();
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean equals(Object other){
		return other instanceof CountryDivision && equalsOther((CountryDivision)other);
	}
	
	private boolean equalsOther(CountryDivision other){
		return this.ISOCode().equals(other.ISOCode()) && this.getParent().equals(other.getParent()); 
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public int hashCode(){
		return this.ISOCode().hashCode() ^ this.getParent().hashCode();
	}
}
