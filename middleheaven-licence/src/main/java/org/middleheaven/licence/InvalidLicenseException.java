package org.middleheaven.licence;

public class InvalidLicenseException extends LicenseException {


	private static final long serialVersionUID = 1L;

	public InvalidLicenseException() {
		super("Invalid Licence");
	}

}
