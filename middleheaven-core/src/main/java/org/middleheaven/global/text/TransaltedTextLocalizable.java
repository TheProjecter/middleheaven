/**
 * 
 */
package org.middleheaven.global.text;

import java.io.Serializable;

/**
 * 
 */
class TransaltedTextLocalizable extends LocalizableText {


	private static final long serialVersionUID = -6311812794303023037L;
	
	private String text;
	private String domain;
	private String message;
	private Serializable[] params;
	
	TransaltedTextLocalizable (String text){
		this.text = text;
	}
	
	TransaltedTextLocalizable (String text, String domain, String message, Serializable ... params){
		this.text = text;
		this.domain = domain;
		this.message = message;
		this.params = params;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDomain() {
		return domain;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMessageKey() {
		return message;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Serializable[] getMessageParams() {
		return params;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLocalized() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return text;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other) {
		return other instanceof TransaltedTextLocalizable && equalsOther((TransaltedTextLocalizable) other);
	}
	
	private boolean equalsOther(TransaltedTextLocalizable other) {
		return this.text.equals(other.text);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return text.hashCode();
	}

}
