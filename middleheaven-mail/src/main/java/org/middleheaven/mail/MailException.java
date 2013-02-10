package org.middleheaven.mail;

public class MailException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public static MailException manage(Exception o){
		try{
			throw o;
		} catch (javax.mail.AuthenticationFailedException e){
			return new AuthenticationFailedMailException(e);
		}catch (Exception e){
			return new MailException(e);
		}
	}

	public MailException (Throwable cause){
		super(cause);
	}
}
