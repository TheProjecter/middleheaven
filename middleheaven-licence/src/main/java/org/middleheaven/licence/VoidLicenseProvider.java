package org.middleheaven.licence;


public class VoidLicenseProvider implements LicenseProvider {

	
	@Override
	public License getLicence(String featureID, String version) {
		return new VoidLicense(featureID);
	}


}
