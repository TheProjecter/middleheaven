package org.middleheaven.licence;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ComposedProvider implements LicenseProvider {

	List<LicenseProvider> providers = new CopyOnWriteArrayList<LicenseProvider>();
	
	public void addProvider (LicenseProvider other){
		providers.add(other);
	}
	
	@Override
	public License getLicence(String featureID, String version) {
		for (LicenseProvider p: providers){
			License lic = p.getLicence(featureID, version);
			if (lic.isValid()){
				return lic;
			}
		}
		return new VoidLicense(featureID);
	}


}
