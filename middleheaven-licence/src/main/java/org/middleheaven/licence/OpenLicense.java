package org.middleheaven.licence;

public final class OpenLicense extends AbstractLicense{

	public OpenLicense(String feature) {
		super(feature);
	}

	@Override
	public boolean isValid() {
		return true;
	}
	
	@Override
	public void checkIn() {
		//no-op
	}

	@Override
	public void checkOut() {
		// no-op
	}

	@Override
	public String getAttribute(String name) {
		return null;
	}




}
