package org.middleheaven.util.identity;

import org.middleheaven.util.Hash;

/**
 * Representation of an {@link Identity} using a {@link Long}.
 * 
 */
public final class LongIdentity extends Identity {


	private static final long serialVersionUID = -5911648867578196321L;
	
	private long value;

	
	/**
	 * Creates a {@link LongIdentity} from a long value present in a {@link String}.
	 * @param value the value to use as identity.
	 * @return the corresponding {@link LongIdentity}.
	 */
	public static LongIdentity valueOf(String value) {
		return valueOf(Long.parseLong(value));
	}

	/**
	 * Creates a {@link LongIdentity} from a long value present in a {@link Long}.
	 * @param value the value to use as identity.
	 * @return the corresponding {@link LongIdentity}.
	 */
	public static LongIdentity valueOf(Long value) {
		return  valueOf(value.longValue());
	}
	
	/**
	 * Creates a {@link LongIdentity} from a long value present in an <code>long</code>.
	 * @param value the value to use as identity.
	 * @return the corresponding {@link LongIdentity}.
	 */
	public static LongIdentity valueOf(long value) {
		return new LongIdentity(value);
	}
	
	private LongIdentity(long value) {
		this.value = value;
	}

	@Override
	public boolean equalsIdentity(Identity other) {
		return other instanceof LongIdentity && equalsLongIdentity((LongIdentity)other);
	}

	private boolean equalsLongIdentity(LongIdentity other) {
		return this.value == other.value;
	}

	@Override
	public int hashCode() {
		return Hash.hash(value).hashCode();
	}

	@Override
	public String toString() {
		return Long.toString(value);
	}

	public long longValue(){
		return value;
	}

	public LongIdentity next(){
		return new LongIdentity(this.value+1);
	}


}
