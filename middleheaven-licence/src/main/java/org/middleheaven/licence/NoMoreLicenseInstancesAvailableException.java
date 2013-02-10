package org.middleheaven.licence;

public class NoMoreLicenseInstancesAvailableException extends LicenseException {

	
	public NoMoreLicenseInstancesAvailableException() {
		super("No more licence instances");
	}

	private static final long serialVersionUID = 1L;

}
