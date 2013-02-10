package org.middleheaven.licence;

public interface License {

	public String getFeatureID();
	
	/**
	 * Tests if the license is valid.
	 * Validity logic is imposed by the license it self witch is free to implemented
	 * as it sees fit.
	 * @return <code>true</code> is the license is valid, <code>false</code> otherwise.
	 */
	public boolean isValid();
	
	/**
	 * Allocates an instance of the license. Is not more instances are available 
	 * a <code>LicenceException</code> is thrown 
	 * @throws InvalidLicenseException if the license is invalid
	 * @throws NoMoreLicenseInstancesAvailableException if no more instances are available at the moment
	 * 
	 */
	public void checkOut();
	
	/**
	 * De-allocates an instance of the license. 
	 */
	public void checkIn();
	
	/**
	 * Returns the value of a licenceAtribute
	 * @param name name of the attribute
	 * @return the value of the attribute for this license
	 */
	public String getAttribute(String name);
	
}
