package org.middleheaven.aas;

import org.middleheaven.util.StringUtils;

/**
 * A credencial that has a name associated with it.
 */
public final class NamedCredential implements Credential{

	
	private static final long serialVersionUID = 610928649199901149L;
	
	private final String name;

	public NamedCredential(String name) {
		super();
		if ( StringUtils.isEmptyOrBlank(name)){
			throw new IllegalArgumentException("Name is required to construct a Name Credential");
		}
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	
	public boolean equals(Object other){
		return other instanceof NamedCredential && ((NamedCredential)other).name.equals(this.name);
	}
	
	public int hashCode(){
		return name.hashCode();
	}
	
}
