package org.middleheaven.process.web.server;

import java.util.Collections;

import org.middleheaven.aas.Credential;
import org.middleheaven.aas.NamedCredential;
import org.middleheaven.aas.Signature;
import org.middleheaven.aas.TimedSignature;
import org.middleheaven.crypto.Base64CipherAlgorithm;
import org.middleheaven.process.ContextScope;
import org.middleheaven.process.web.HttpCookie;
import org.middleheaven.quantity.time.Period;

class HttpContextCookiesSignatureStore extends HttpContextSignatureStore {

	private static final String SIGNATURE_ATTRIBUTE = "mh_user_signature";
	private final Period sessionTimeout; 
	private HttpServerContext context;

	public HttpContextCookiesSignatureStore(HttpServerContext context, Period sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
		this.context = context;
	}
	
	@Override
	public Signature getSignature() {
		HttpCookie cookie = context.getAttributes().getAttribute(ContextScope.REQUEST_COOKIES, SIGNATURE_ATTRIBUTE, HttpCookie.class);
		
		if (cookie !=null){
			String ticket = new String(new Base64CipherAlgorithm().revertCipher(cookie.getValue().getBytes()));
			
			String name = ticket.split("@")[0];
			String hash = ticket.split("@")[1];
			if (hash.equals(Integer.toHexString(name.hashCode() * 37))){
				return new TimedSignature(Collections.singleton(new NamedCredential(name)), cookie.getMaxAge());
			}
		}
		
		return null;
	}

	@Override
	public void setSignature(Signature signature) {
		
		HttpCookie cookie= new HttpCookie(SIGNATURE_ATTRIBUTE, "");
		cookie.expire();
		if (signature.isValid()){
			String name=null;
			for (Credential c : signature.getCredentials()){
				if (c instanceof NamedCredential){
					name = ((NamedCredential)c).getName();
					break;
				}
			}
			
			if (name != null){
				// the user is identified
				String ticket = name + "@" + Integer.toHexString(name.hashCode() * 37);
				String value = new String(new Base64CipherAlgorithm().cipher(ticket.getBytes()));
				cookie = new HttpCookie(SIGNATURE_ATTRIBUTE, value);
				cookie.setDomain(context.getRequestUrl().getHost());
				cookie.setPath(context.getContextPath());
				cookie.setMaxAge(sessionTimeout);
			}
		} 
		context.getAttributes().setAttribute(ContextScope.REQUEST_COOKIES, SIGNATURE_ATTRIBUTE, cookie);
	}



}
