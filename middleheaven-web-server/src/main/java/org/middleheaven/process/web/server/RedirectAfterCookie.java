package org.middleheaven.process.web.server;

import org.middleheaven.crypto.Base64CipherAlgorithm;
import org.middleheaven.process.web.HttpCookie;
import org.middleheaven.process.web.server.action.URLOutcome;

public class RedirectAfterCookie extends HttpCookie {

	
	public RedirectAfterCookie (HttpCookie other){
		super(other.getName(), other.getValue());
		this.setComment(other.getComment());
		this.setDomain(other.getDomain());
		this.setMaxAge(other.getMaxAge());
		this.setPath(other.getPath());
		this.setSecure(other.isSecure());
		this.setVersion(other.getVersion());
		
		this.setMaxAge(null); // session only
	}
	
	/**
	 * 
	 * @param name name of the cookie
	 * @param url url to redirect to
	 */
	public RedirectAfterCookie(String name, String url) {
		super(name, encode(url));
		this.setMaxAge(null); // session only
	}

	private static String encode(String url) {
		return new String(new Base64CipherAlgorithm().cipher(url.getBytes()));
	}
	
	private static String decode(String url) {
		return new String(new Base64CipherAlgorithm().revertCipher(url.getBytes()));
	}
	
	public String getUrl(){
		return decode(this.getValue());
	}
	
	public Outcome asOutcome(){
		return  URLOutcome.forUrl(getUrl()).byRedirecting();
	}

	
}
