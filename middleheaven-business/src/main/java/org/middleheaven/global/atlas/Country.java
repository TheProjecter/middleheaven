package org.middleheaven.global.atlas;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import org.middleheaven.culture.CountryIsoCode;
import org.middleheaven.culture.Culture;
import org.middleheaven.culture.IsoCode;
import org.middleheaven.culture.Language;
import org.middleheaven.global.address.AddressModel;
import org.middleheaven.global.address.DefaultAddressModel;
import org.middleheaven.quantity.money.Currency;
import org.middleheaven.quantity.money.ISOCurrency;

public abstract class Country extends AbstractAtlasLocale implements Serializable {

	private String name;
	private List<Language> languages = Collections.emptyList();

	protected Country(IsoCode isoCode){
		super(null,isoCode,isoCode.toString());
	}
	
	protected Country(CountryIsoCode isoCode, String name){
		super(null,isoCode,name);
	}
	
	public String toString(){
		return this.name;
	}

	@Override
	public boolean isCountry() {
		return true;
	}

	@Override
	public boolean isDivision() {
		return false;
	}

	@Override
	public boolean isTown() {
		return false;
	}
	
	public Currency getCurrentCurrency (){
		return new ISOCurrency(java.util.Currency.getInstance(new Locale(getLanguage().toString(),this.ISOCode().toString())));
	}

	public Language getLanguage(){
		return languages.get(0);
	}

	public List<Language> getLanguages(){
		return languages;
	}

	protected void addLanguage(Language language){
		if (languages.isEmpty()){
			languages= new CopyOnWriteArrayList<Language>();
		}
		if (!this.languages.contains(language)){
			this.languages.add(language);
		}

	}
	
	public Culture getCulture(){
		return Culture.valueOf(getLanguage().toString(),this.ISOCode().toString());
	}

	public final AddressModel getAddressModel(){
		return DefaultAddressModel.getInstance();
		/*
		try {
			AddressModelService service = ServiceRegistry.getService(AddressModel.class);
			return service.getAddressModel(this);
		} catch (ServiceNorFoundException e ){
			return DefaultAddressModel.getInstance();
		}
		*/
	}


	protected final void setName(String name){
		this.name = name;
	}


	@Override
	public final String getName() {
		return  this.name;
	}

	@Override
	public final AtlasLocale getParent() {
		return Atlas.ATLAS;
	}

	@Override
	public final String getQualifiedDesignation() {
		return Atlas.ATLAS.getQualifiedDesignation() + "." + ISOCode();
	}

	public boolean equals(Object other){
		return other instanceof Country && equalsOther((Country)other);
	}

	public boolean equalsOther(Country other){
		return this.ISOCode().equals(other.ISOCode()); 
	}

	public int hashCode(){
		return this.ISOCode().hashCode();
	}
}
