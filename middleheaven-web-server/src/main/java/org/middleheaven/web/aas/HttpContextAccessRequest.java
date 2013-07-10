/**
 * 
 */
package org.middleheaven.web.aas;

import java.net.InetAddress;

import org.middleheaven.aas.AccessRequest;
import org.middleheaven.aas.CallbackHandler;
import org.middleheaven.aas.Signature;
import org.middleheaven.aas.SignatureStore;
import org.middleheaven.aas.Subject;
import org.middleheaven.process.web.server.HttpServerContext;

public final class HttpContextAccessRequest extends AccessRequest{


	private HttpServerContext context;
	private CallbackHandler handler;
	private SignatureStore store;

	public HttpContextAccessRequest(HttpServerContext context,CallbackHandler handler, SignatureStore store){
		this.context = context;
		this.store = store;
		this.handler = handler;
	}

	@Override
	public CallbackHandler getCallbackHandler() {
		return handler;
	}

	@Override
	public InetAddress getCallerAddress() {
		return context.getHttpChannel().getRemoteAddress();
	}

	Subject subject;

	@Override
	public Subject getSubject() {
		return subject;
	}

	@Override
	protected void setSubject(Subject subject) {
		this.subject = subject;
	}

	@Override
	public Signature getSignature() {
		return store.getSignature();
	}

	@Override
	public void setSignature(Signature signature) {
		store.setSignature(signature);
	}

}