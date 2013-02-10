/**
 * 
 */
package org.middleheaven.licence;


/**
 * An "void" license. This implementation is invalid and throws an 
 * <code>InvalidLicenceException</code> on chekout.
 */
public class VoidLicense implements License{
	
	private String feature;
	
	public VoidLicense(String featureID){
		this.feature = featureID;
	}
	
	@Override
	public void checkIn() {
		//no-op
	}

	@Override
	public void checkOut() {
		throw new InvalidLicenseException();
	}

	@Override
	public String getAttribute(String name) {
		return null;
	}

	@Override
	public String getFeatureID() {
		return feature;
	}

	@Override
	public boolean isValid() {
		return false;
	}
	
}