package org.middleheaven.global.atlas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.middleheaven.culture.CountryIsoCode;
import org.middleheaven.culture.IsoCode;
import org.middleheaven.culture.Language;

public abstract class AtlasModule {

	Map<IsoCode , List<Language> > countryLocales;
	
	protected synchronized Language findLocaleForCountry(IsoCode isoCode){
		if (countryLocales==null){
			// load locales
			final Locale[] locales = Locale.getAvailableLocales();
			countryLocales = new HashMap<IsoCode , List<Language> >();
			for (Locale locale : locales ){
				List<Language> list = countryLocales.get(locale.getCountry());
				if (list==null){
					list=new ArrayList<Language>();
					countryLocales.put(new CountryIsoCode(locale.getCountry()), list);
				}
				list.add(Language.valueOf(locale.getLanguage()));
			}
		}
		
		List<Language> list =  countryLocales.get(isoCode);
		if (list==null || list.isEmpty()){
			return Language.valueOf("");
		} else {
			return list.get(0);
		}
		
		
	
	}
	
	
	public abstract void loadAtlas(AtlasContext context);
}
