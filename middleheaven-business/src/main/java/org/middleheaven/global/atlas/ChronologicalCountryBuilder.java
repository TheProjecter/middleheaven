package org.middleheaven.global.atlas;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.middleheaven.culture.IsoCode;
import org.middleheaven.culture.Language;

public class ChronologicalCountryBuilder implements AtlasContext{

	List<CountryInfo> infoCountries = new LinkedList<CountryInfo>();
	Map<IsoCode,Country> countries = new HashMap<IsoCode,Country>();
	
	@Override
	public void addCountryInfo(CountryInfo country) {
		if (country!=null){
			infoCountries.add(country);
		}
	}

	public Collection<Country> countries (){
		return Collections.unmodifiableCollection(countries.values());
	}

	public void build(){
		
		Collections.sort(infoCountries , new Comparator<CountryInfo>(){

			@Override
			public int compare(CountryInfo a, CountryInfo b) {
				int d = a.getName().compareTo(b.getName());
				if (d==0){
					return a.lastUpdateDate.compareTo(b.lastUpdateDate);
				}
				return d;
			}

		});
		
		// for each country
		for (CountryInfo countryInfo : infoCountries){
			
			ChronologicalCountry currentCountry = new ChronologicalCountry(countryInfo.getIsoCode());
			currentCountry.setName(countryInfo.getName());
			
			// load languages
			for (Language language : countryInfo.getLanguages()){
				currentCountry.addLanguage(language);
			}
			
			// load division
			for (AtlasLocaleInfo atlasLocaleInfo : countryInfo.atlasLocations){
			
				if (atlasLocaleInfo instanceof CountryDivisionInfo){
					ChronologicalDivision div = new ChronologicalDivision(currentCountry , atlasLocaleInfo.getIsoCode(),
							((CountryDivisionInfo)atlasLocaleInfo).getType());
					div.setName(atlasLocaleInfo.getName());
					
					// cities
					for (AtlasLocaleInfo cityInfo : atlasLocaleInfo.atlasLocations){
						div.addAtlasLocale( new ChronologicalAtlasLocale(div,cityInfo.getIsoCode(), cityInfo.getName()));
					}
					
					currentCountry.addAtlasLocale(div);
				} else {
					ChronologicalAtlasLocale la = new ChronologicalAtlasLocale(currentCountry,atlasLocaleInfo.getIsoCode(), atlasLocaleInfo.getName());
					
					// cities
					for (AtlasLocaleInfo cityInfo : atlasLocaleInfo.atlasLocations){
						la.addAtlasLocale( new ChronologicalAtlasLocale(la,cityInfo.getIsoCode(), cityInfo.getName()));
					}
					
					currentCountry.addAtlasLocale(la);
				}
			}
			
			countries.put(currentCountry.ISOCode() , currentCountry);
		}
	}
	
	private static class ChronologicalCountry extends Country{
		Map<IsoCode,AtlasLocale> children = new HashMap<IsoCode,AtlasLocale>();
		
		protected ChronologicalCountry(IsoCode isoCode) {
			super(isoCode);
		}

		public void addAtlasLocale(AtlasLocale cal) {
			children.put(cal.ISOCode(), cal);
		}

		
		@Override
		public AtlasLocale getChild(String designation) {
			return children.get(designation);
		}

		@Override
		public Collection<AtlasLocale> getChildren() {
			return Collections.unmodifiableCollection(children.values());
		}

	}
	
	private static class ChronologicalDivision extends CountryDivision{

		final Map<IsoCode,AtlasLocale> children = new HashMap<IsoCode,AtlasLocale>();
		
		protected ChronologicalDivision(Country country, IsoCode isoCode, CountryDivisionType type) {
			super(country, isoCode,type);
		}

		@Override
		public AtlasLocale getChild(String designation) {
			return children.get(designation);
		}

		@Override
		public Collection<AtlasLocale> getChildren() {
			return Collections.unmodifiableCollection(children.values());
		}
		
		public void addAtlasLocale(AtlasLocale cal) {
			children.put(cal.ISOCode(), cal);
		}

	}

	
	private static class ChronologicalAtlasLocale extends AbstractAtlasLocale{

		final Map<IsoCode,AtlasLocale> children = new HashMap<IsoCode,AtlasLocale>();
		
		protected ChronologicalAtlasLocale(AtlasLocale parent, IsoCode isoCode, String name) {
			super(parent, isoCode,name);
		}

		@Override
		public AtlasLocale getChild(String designation) {
			return children.get(designation);
		}

		@Override
		public Collection<AtlasLocale> getChildren() {
			return Collections.unmodifiableCollection(children.values());
		}

		public void addAtlasLocale(AtlasLocale cal) {
			children.put(cal.ISOCode(), cal);
		}

		@Override
		public boolean isCountry() {
			return false;
		}

		@Override
		public boolean isDivision() {
			return this.getParent().isCountry();
		}

		@Override
		public boolean isTown() {
			return this.getParent().isDivision();
		}

	}

	public Country get(String isoCode) {
		return countries.get(isoCode);
	}
}
