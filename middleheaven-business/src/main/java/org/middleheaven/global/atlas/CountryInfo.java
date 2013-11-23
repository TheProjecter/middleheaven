package org.middleheaven.global.atlas;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.culture.CountryIsoCode;
import org.middleheaven.culture.Language;
import org.middleheaven.quantity.time.DateHolder;


public class  CountryInfo extends AtlasLocaleInfo {

	DateHolder lastUpdateDate;
	List<Language> languages = new LinkedList<Language>(); // 0 is official
	

	public CountryInfo(CountryIsoCode isoCountryCode , DateHolder lastUpdateDate){
		super(isoCountryCode, null);
		this.lastUpdateDate = lastUpdateDate;
	}
	
	public DateHolder getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(DateHolder lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Collection<Language> getLanguages() {
		return languages;
	}

	public void addLanguage(Language language) {
		languages.add(language);
	}

	
}
