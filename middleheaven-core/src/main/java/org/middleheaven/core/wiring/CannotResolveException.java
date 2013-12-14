package org.middleheaven.core.wiring;


public class CannotResolveException extends BindingException {


	private static final long serialVersionUID = 3845085385845398440L;

	public CannotResolveException(String contractName,String name) {
		super("Cannot resolve instance of" +  contractName + " named " + name);
	}

}
