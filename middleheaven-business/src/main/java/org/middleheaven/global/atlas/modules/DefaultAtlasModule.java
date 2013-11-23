package org.middleheaven.global.atlas.modules;

import java.util.Locale;

import org.middleheaven.culture.CountryIsoCode;
import org.middleheaven.global.atlas.AtlasContext;
import org.middleheaven.global.atlas.AtlasModule;
import org.middleheaven.global.atlas.CountryInfo;
import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.quantity.time.DateHolder;

public class DefaultAtlasModule extends AtlasModule {


	@Override
	public void loadAtlas(AtlasContext context) {
		String[] isoCodes = Locale.getISOCountries();
		String[] split = System.getProperty("java.version").split("\\.");
		
		DateHolder time = CalendarDate.date(1900 + Integer.parseInt(split[0]) ,Integer.parseInt(split[1]) ,1);
		
		for (String code : isoCodes ){
			final CountryIsoCode isoCode = new CountryIsoCode(code);
			CountryInfo info = new CountryInfo(isoCode , time);
			info.setName(code);
			
			info.addLanguage(this.findLocaleForCountry(isoCode));
			context.addCountryInfo(info);
		}
		
		
	}

	
}
