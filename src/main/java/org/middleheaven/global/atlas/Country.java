package org.middleheaven.global.atlas;

import java.io.Serializable;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import org.middleheaven.global.address.AddressModel;
import org.middleheaven.global.address.DefaultAddressModel;

public abstract class Country extends AbstractAtlasLocale implements Serializable {

	private String name;
	private List<Language> languages = Collections.emptyList();

	protected Country(String isoCode){
		super(null,isoCode);
	}

	public Currency getCurrentCurrency (){
		return Currency.getInstance(new Locale(getLanguage().toString(),this.ISOCode()));
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

	public Locale toLocale(){
		return new Locale(getLanguage().toString(),this.ISOCode());
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


	public final String getName(){
		return name;
	}

	protected final void setName(String name){
		this.name = name;
	}


	@Override
	public final String getDesignation() {
		return this.ISOCode();
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
		return other instanceof Country && equals((Country)other);
	}

	public boolean equals(Country other){
		return this.ISOCode().equals(other.ISOCode()); 
	}

	public int hashCode(){
		return this.ISOCode().hashCode();
	}
}