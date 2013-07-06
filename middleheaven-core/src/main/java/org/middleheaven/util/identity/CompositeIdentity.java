package org.middleheaven.util.identity;

import java.util.Arrays;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.util.Hash;

/**
 * An {@link Identity} composed of other identities.
 */
public final class CompositeIdentity extends Identity {

	private static final long serialVersionUID = -1592992685697748450L;
	
	private final Identity[] identities;
	private final int hashCode;
	
	public static CompositeIdentity valueFor (Identity a, Identity b, Identity ... others ){
		Identity[] identities = new Identity[2 + others.length];
		
		identities[0] = a;
		identities[1] = b;
		
		System.arraycopy(others, 0, identities, 2, others.length);
		
		return new CompositeIdentity(identities);
	}
	
	private CompositeIdentity(Identity[] identities){
		this.identities = CollectionUtils.duplicateArray(identities);
		this.hashCode = Hash.hash(identities).hashCode();
	}
	
	@Override
	public boolean equalsIdentity(Identity other) {
		return other instanceof CompositeIdentity && Arrays.equals(((CompositeIdentity)other).identities , this.identities);
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public String toString() {
		return Arrays.toString(identities);
	}



}
