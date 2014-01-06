package org.middleheaven.culture;

import java.util.Map;
import java.util.TreeMap;

/**
 * Flyweight implementation for Language
 */
public class Language implements Comparable<Language>{

	
	private String code;
	private static final Map<String, Language> languagesCache = new TreeMap<String, Language>();
	
	private Language(String code){
		this.code = code;
	}
	
	public boolean equals(Object other) {
		return other instanceof Language && equalsOther((Language) other);
	}

	private boolean equalsOther(Language other) {
		return this.code.equals(other.code);
	}

	public int hashCode() {
		return code.hashCode();
	}
	
	public String toString() {
		return code;
	}
	
	/**
	 * Assert if the language represented by this is 
	 * represented by the ISO 639 language code.
	 * 
	 * @param isoCode 
	 * @return
	 */
	public boolean hasCode (String isoCode){
		return code.equalsIgnoreCase(isoCode);
	}

	@Override
	public int compareTo(Language other) {
		return this.code.compareTo(other.code);
	}

	public static Language valueOf(String code) {
		Language lang = languagesCache.get(code);
		if ( lang == null){
			lang = new Language(code);
			languagesCache.put(code,lang);
		}
		return lang;
	}
}
