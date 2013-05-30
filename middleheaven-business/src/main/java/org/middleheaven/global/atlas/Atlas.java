package org.middleheaven.global.atlas;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.middleheaven.core.bootstrap.ServiceRegistry;
import org.middleheaven.global.IsoCode;
import org.middleheaven.global.StringIsoCode;


public class Atlas implements AtlasLocale{

	public static final Atlas ATLAS = new Atlas();

	private Atlas(){}
	
	public static Country getCountry (String isoCode){
		return (Country)ATLAS.getChild(isoCode);
	}
	
	/**
	 * Returns all current countries in the World
	 */
	@Override
	public Set<AtlasLocale> getChildren() {
		AtlasService service = ServiceRegistry.getService(AtlasService.class);
		return Collections.unmodifiableSet(new HashSet<AtlasLocale>(service.findALLCountries()));
	}

	@Override
	public IsoCode ISOCode() {
		return new StringIsoCode("atlas");
	}
	
	public String getName(){
		return "Atlas";
	}

	@Override
	public AtlasLocale getParent() {
		return null;
	}

	@Override
	public String getQualifiedDesignation() {
		return "atlas";
	}

	@Override
	public AtlasLocale getChild(String designation) {
		return ServiceRegistry.getService(AtlasService.class).findCountry(designation);
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
		return false;
	}
}
