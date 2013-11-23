package org.middleheaven.global.atlas;

import java.io.Serializable;
import java.util.Collection;

import org.middleheaven.culture.IsoCode;


public abstract class AbstractAtlasLocale implements AtlasLocale , Serializable{


	private static final long serialVersionUID = -7228141508511762236L;
	
	private IsoCode isoCode;
	private AtlasLocale parent;
	protected String name;

    
	protected AbstractAtlasLocale(AtlasLocale parent,  IsoCode isoCode , String name){
		this.isoCode = isoCode;
		this.parent = parent;
		this.name = name;
	}
    
	
	public IsoCode ISOCode(){
		return isoCode;
	}
	
	public String subDevision(){
		return isoCode.toString();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public AtlasLocale getParent() {
		return parent;
	}

	@Override
	public String getQualifiedDesignation() {
		return parent.getQualifiedDesignation() + "." + isoCode;
	}
	
	public boolean equals(Object other){
		return other instanceof AtlasLocale && equalsOther((AtlasLocale)other);
	}
	
	private boolean equalsOther(AtlasLocale other){
		return this.isoCode.equals(other.getName()) && this.parent.equals(other.getParent()); 
	}
	
	public int hashCode(){
		return this.isoCode.hashCode() ^ this.parent.hashCode();
	}

	public String toString(){
		return name;
	}
	
	@Override
	public abstract AtlasLocale getChild(String designation);

	@Override
	public abstract Collection<AtlasLocale> getChildren() ;
}
