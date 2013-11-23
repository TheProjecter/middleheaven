/**
 * 
 */
package org.middleheaven.application;

import org.middleheaven.util.Version;


/**
 * 
 */
public class Application {


	private Version version;
	private ApplicationActivator activator;
	private String applicationId;

	public Application(String applicationId, Version version, ApplicationActivator activator) {
		super();
		this.applicationId = applicationId;
		this.version = version;
		this.activator = activator;
	}

	
	
	/**
	 * Obtains {@link String}.
	 * @return the applicationId
	 */
	public String getApplicationId() {
		return applicationId;
	}



	/**
	 * Obtains {@link Version}.
	 * @return the version
	 */
	public Version getVersion() {
		return version;
	}

	/**
	 * Obtains {@link ApplicationActivator}.
	 * @return the activator
	 */
	public ApplicationActivator getActivator() {
		return activator;
	}


	public int hashCode(){
		return this.applicationId.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Application) && equalsApplication((Application)obj); 
	}


	private boolean equalsApplication(Application other) {
		return this.applicationId.equals(other.applicationId);
	}

	/**
	 * @return
	 */
	public ApplicationVersion getApplicationVersion() {
		return new ApplicationVersion(this.applicationId, version);
	}

}
