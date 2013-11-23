package org.middleheaven.global.atlas;

import java.util.Collection;

import org.middleheaven.culture.IsoCode;

public interface AtlasLocale {

	public boolean isCountry();
	public boolean isTown();
	public boolean isDivision();
	
	public IsoCode ISOCode();
	public String getName();
	public String getQualifiedDesignation();
	public AtlasLocale getParent();
	public Collection<AtlasLocale> getChildren(); 
	public AtlasLocale getChild(String designation); 
}
