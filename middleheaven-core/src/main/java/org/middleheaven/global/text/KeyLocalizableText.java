/*
 * Created on 2006/08/26
 *
 */
package org.middleheaven.global.text;

import java.io.Serializable;

import org.middleheaven.util.Hash;

/**
 * 
 */
final class KeyLocalizableText extends LocalizableText{

	private static final long serialVersionUID = 3624294714337027974L;
	
	private String label;
	private String domain;
	private Serializable[] params = new Serializable[0];

	// used by TextLocalizable
    KeyLocalizableText(String domain, String label , Serializable ... params){
		this.label = label;
		this.domain = domain;
		this.params = params;
	}
	
	/**
	 * The name of the resource bundle domain.
	 * 
	 * @return the resource domain name.
	 */
	public String getDomain(){
		return domain;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public String toString(){
		return domain + ":" + label;
	}

	/**
	 * The message parameters to use in the translation
	 * 
	 * @return the message parameters to use in the translation
	 */
	public Serializable[] getMessageParams() {
		return this.params;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean equals(Object other){
		return other instanceof  KeyLocalizableText && ((KeyLocalizableText)other).label.equals(label);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
    public int hashCode(){
    	return Hash.hash(label).hashCode();
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMessageKey() {
		return label;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLocalized() {
		return false;
	}

}
