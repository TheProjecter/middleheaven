package org.middleheaven.core.wiring;

public class ConfigurationException extends RuntimeException {

	private static final long serialVersionUID = -4470948814581960440L;

	public ConfigurationException (String cause){
		super(cause);
	}
}
