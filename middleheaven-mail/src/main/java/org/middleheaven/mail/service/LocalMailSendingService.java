package org.middleheaven.mail.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.middleheaven.core.annotations.Service;
import org.middleheaven.mail.MailSendingService;

/**
 * Implementation of {@link MailSendingService} that uses standard JavaMail API
 * for sending email 
 * 
 */
@Service
public class LocalMailSendingService extends AbstractMailSessionSendingService {

	boolean debug = false;
	private String stmpHost;
	private String userName;
	private String password;
	
	public void setDebug(boolean debug){
		this.debug = debug;
	}

	public void setSMTPHost(String host){
		stmpHost = host;
	}

	public void setUsername(String username){
		this.userName = username;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	protected Session getMailSession(){
		//Set the host smtp address
		Properties props = new Properties();
		props.put("mail.smtp.host", stmpHost);

		// create some properties and get the default Session
		Authenticator auth = new Authenticator(){
			public PasswordAuthentication getPasswordAuthentication(){
				 return new PasswordAuthentication(userName,password);
			}
		};
		
		Session session = Session.getDefaultInstance(props,auth );
		session.setDebug(debug);
		return session;
	}
}
