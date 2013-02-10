package org.middleheaven.process.web;

import org.middleheaven.quantity.time.Period;
import org.middleheaven.util.Hash;

/**
 * A technology independent representation of a Cookie. 
 */
public class HttpCookie {

	private String name;
	private String value;
	private String comment;
	private String domain;
	private Period maxAge = null; 
	private String path;
	private boolean secure;
	private int version = 1;

	public HttpCookie(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public HttpCookie expire(){
		this.maxAge = Period.seconds(0);
		return this;
	}
	
	public String getComment() {
		return comment;
	}

	public String getDomain() {
		return domain;
	}

	/**
	 * Cookie age in secounds
	 * @return
	 */
	public Period getMaxAge() {
		return maxAge;
	}

	public String getPath() {
		return path;
	}

	public boolean isSecure() {
		return secure;
	}

	public int getVersion() {
		return version;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value){
		this.value = value;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setMaxAge(Period maxAge) {
		this.maxAge = maxAge;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	public void setVersion(int version) {
		if (version != 0 && version != 1) {
			throw new IllegalArgumentException("Cookie version should be 0 or 1");
		}
		
		this.version = version;
	}

	public boolean equals(Object other) {
		return other instanceof HttpCookie && equalsOther((HttpCookie)other);
	}

	private boolean equalsOther(HttpCookie other){
		return equalsIgnoreCase(getName(), other.getName()) &&
		equalsIgnoreCase(getDomain(), other.getDomain()) &&
		equals(getPath(), other.getPath());
	}

	public int hashCode() {
		return Hash.hash(name).hash(domain).hash(path).hashCode();
	}

	private static boolean equalsIgnoreCase(String s, String t) {
		if (s == t) {
			return true;
		}
		if ((s != null) && (t != null)) {
			return s.equalsIgnoreCase(t);
		}
		return false;
	}

	private static boolean equals(String s, String t) {
		if (s == t) {
			return true;
		}
		if ((s != null) && (t != null)) {
			return s.equals(t);
		}
		return false;
	}



}
