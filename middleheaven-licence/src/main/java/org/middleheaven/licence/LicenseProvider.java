package org.middleheaven.licence;



public interface LicenseProvider {

	public License getLicence(String featureID, String version);
	
}
