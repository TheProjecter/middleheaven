package org.middleheaven.global.atlas;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.culture.CountryIsoCode;
import org.middleheaven.culture.IsoCode;

public class AtlasLocaleInfo {

	
	private String name;
	private CountryIsoCode isoCode;
	private AtlasLocaleInfo parent;
	List<AtlasLocaleInfo> atlasLocations = new LinkedList<AtlasLocaleInfo>();
	
	public AtlasLocaleInfo( CountryIsoCode isoCode,AtlasLocaleInfo parent) {
		if (isoCode==null){
			throw new IllegalArgumentException("ISO code is required");
		}
		this.isoCode = isoCode;
		this.parent = parent;
	}
	
	public Collection<AtlasLocaleInfo> getCildrenAtlasLocations() {
		return atlasLocations;
	}
	
	public void addAtlasLocale(AtlasLocaleInfo child) {
		this.atlasLocations.add(child);
	}

	
	public String getName() {
		return name;
	}
	
	public IsoCode getIsoCode() {
		return isoCode;
	}
	
	public AtlasLocaleInfo getParent() {
		return parent;
	}

	public AtlasLocaleInfo setName(String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other) {
		return other instanceof AtlasLocaleInfo
				&& equalsOther((AtlasLocaleInfo) other);
	}

	private boolean equalsOther(AtlasLocaleInfo other) {
		return this.isoCode.equals(other.isoCode);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return isoCode.hashCode();
	}
}
