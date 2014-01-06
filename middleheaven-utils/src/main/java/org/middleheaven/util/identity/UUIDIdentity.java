package org.middleheaven.util.identity;

import java.io.Serializable;
import java.util.UUID;

/**
 * Identity based on a Universal Unique Identifier (UUID).
 * 
 * This implementation uses the {@link UUID} class to produce the identifiers.
 */
public class UUIDIdentity extends Identity implements Serializable {

	private static final long serialVersionUID = 3448207591627287085L;
	
	private UUID value;
	
	public static UUIDIdentity valueOf(String value) {
		return new UUIDIdentity(value);
	}
	
	public static UUIDIdentity next() {
		return new UUIDIdentity(UUID.randomUUID());
	}
	
	public UUIDIdentity(UUID value){
		this.value = value;
	}
	
	public UUIDIdentity(String value){
		this.value = UUID.fromString(value);
	}
	
	@Override
	protected boolean equalsIdentity(Identity other) {
		return other instanceof UUIDIdentity && equalsUUIDIdentity((UUIDIdentity)other);
	}
	
	protected boolean equalsUUIDIdentity(UUIDIdentity other) {
		return this.value.equals(other.value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public String toString() {
		return value.toString();
	}



}
