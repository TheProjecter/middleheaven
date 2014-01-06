package org.middleheaven.util.identity;


/**
 * Representation of an {@link Identity} using a {@link Integer}.
 */
public class IntegerIdentity extends Identity  {

	private static final long serialVersionUID = 1499347363552161220L;
	
	private int value;
	
	/**
	 * Creates a {@link IntegerIdentity} from a integer value present in a {@link String}.
	 * @param value the value to use as identity.
	 * @return the corresponding {@link IntegerIdentity}.
	 */
	public static IntegerIdentity valueOf(String value) {
		return new IntegerIdentity(Integer.parseInt(value));
	}
	
	/**
	 * Creates a {@link IntegerIdentity} from a integer value present in an {@link Integer}.
	 * @param value the value to use as identity.
	 * @return the corresponding {@link IntegerIdentity}.
	 */
	public static IntegerIdentity valueOf(Integer value) {
		return new IntegerIdentity(value.intValue());
	}
	
	/**
	 * Creates a {@link IntegerIdentity} from a integer value present in an <code>int</code>.
	 * @param value the value to use as identity.
	 * @return the corresponding {@link IntegerIdentity}.
	 */
	public static IntegerIdentity valueOf(int value) {
		return new IntegerIdentity(value);
	}
	
	private IntegerIdentity(int value) {
		this.value = value;
	}

	@Override
	public boolean equalsIdentity(Identity other) {
		return other instanceof IntegerIdentity && equalsIntegerIdentity((IntegerIdentity)other);
	}
	
	private boolean equalsIntegerIdentity(IntegerIdentity other) {
		return this.value == other.value;
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}

	public int intValue(){
		return value;
	}
	
	public IntegerIdentity next(){
		return new IntegerIdentity(this.value+1);
	}


}
