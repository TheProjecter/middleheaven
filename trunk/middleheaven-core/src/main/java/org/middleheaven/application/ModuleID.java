package org.middleheaven.application;

import java.io.Serializable;

import org.middleheaven.util.Version;

/**
 * 
 */
public class ModuleID implements Serializable {


	private static final long serialVersionUID = 1475241472644564012L;
	private String identifier;
	private Version version;
	
	public ModuleID(String identifier, Version version) {
		super();
		this.identifier = identifier;
		this.version = version;
	}

	public String getIdentifier() {
		return identifier;
	}

	public Version getVersion() {
		return version;
	}
	
	public boolean equals(Object other) {
		return other instanceof ModuleID && equals((ModuleID) other);
	}

	public boolean equals(ModuleID other) {
		return this.identifier.equals(other.identifier) && this.version.equals(other.version);
	}

	public int hashCode() {
		return this.identifier.hashCode() ^ version.hashCode();
	}
	
	public String toString(){
		return identifier + " " + version;
	}
}
