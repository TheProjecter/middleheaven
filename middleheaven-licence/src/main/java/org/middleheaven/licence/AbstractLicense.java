package org.middleheaven.licence;

public abstract class AbstractLicense implements License {

	private final String feature;
	public AbstractLicense(String feature){
		this.feature= feature;
	}
	
	
	@Override
	public final String getFeatureID() {
		return feature;
	}



}
