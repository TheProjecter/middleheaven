package org.middleheaven.global.atlas;

import org.middleheaven.culture.CountryIsoCode;

public class CountryDivisionInfo extends AtlasLocaleInfo{

	private CountryDivisionType type;
	
	public CountryDivisionType getType() {
		return type;
	}

	public void setType(CountryDivisionType type) {
		this.type = type;
	}

	public CountryDivisionInfo(CountryIsoCode isoCode, AtlasLocaleInfo parent) {
		super(isoCode, parent);
	}

	
}
