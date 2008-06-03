package org.middleheaven.global.atlas;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ChronologicalCountryBuilder implements AtlasContext{

	List<CountryInfo> infoCountries = new LinkedList<CountryInfo>();
	List<Country> countries = new LinkedList<Country>();
	
	@Override
	public void addCountryInfo(CountryInfo country) {
		infoCountries.add(country);
	}

	public List<Country> countries (){
		return Collections.unmodifiableList(countries);
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
		
		ChronologicalCountry currentCountry = null;
		for (CountryInfo info : infoCountries){
			if (!info.getIsoCode().equals(currentCountry.ISOCode())){
				// new info;
				// add the old to the list
				if (currentCountry!=null) {
					countries.add(currentCountry);
				}
				currentCountry = new ChronologicalCountry(info.getIsoCode());
			}
			currentCountry.setName(info.getName());
			for (Language language : info.getLanguages()){
				currentCountry.addLanguage(language);
			}
			for (AtlasLocaleInfo atlasLocaleInfo : info.atlasLocations){
				ChronologicalAtlasLocale cal = new ChronologicalAtlasLocale(currentCountry,atlasLocaleInfo.getIsoCode());
				
				currentCountry.addAtlasLocale(cal);
			}
		}
	}
	
	private class ChronologicalCountry extends Country{

		protected ChronologicalCountry(String isoCode) {
			super(isoCode);
		}

		public void addAtlasLocale(ChronologicalAtlasLocale cal) {
			children.put(cal.ISOCode(), cal);
		}

		Map<String,AtlasLocale> children = new HashMap<String,AtlasLocale>();
		
		@Override
		public AtlasLocale getChild(String designation) {
			return children.get(designation);
		}

		@Override
		public Collection<AtlasLocale> getChildren() {
			return Collections.unmodifiableCollection(children.values());
		}

	
	}
	
	private class ChronologicalAtlasLocale extends AbstractAtlasLocale{

		Map<String,AtlasLocale> children;
		
		protected ChronologicalAtlasLocale(AtlasLocale parent, String isoCode) {
			super(parent, isoCode);
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
}
