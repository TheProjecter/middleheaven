/**
 * 
 */
package org.middleheaven.culture;

/**
 * 
 */
public class StringIsoCode implements IsoCode{

	private static final long serialVersionUID = -7367532905263944230L;

	private String code;

	public StringIsoCode(String code){
		this.code = code;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public String toString(){
		return code;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public int hashCode(){
		return code.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof StringIsoCode) && equalsCountryIsoCode((StringIsoCode)obj); 
	}


	private boolean equalsCountryIsoCode(StringIsoCode other) {
		return this.code.equals(other.code);
	}
}
