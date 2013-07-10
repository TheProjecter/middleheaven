package org.middleheaven.mail.service;

import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.MediaManagedFile;
import org.middleheaven.mail.MailAsynchrounsCallback;
import org.middleheaven.mail.MailException;
import org.middleheaven.mail.MailMessage;
import org.middleheaven.mail.MailRecipientType;
import org.middleheaven.mail.MailSendingService;
import org.middleheaven.mail.MailTransmissionResult;

/**
 * Implementation of {@link MailSendingService} that uses standard JavaMail API
 * for sending email.
 */
public abstract class AbstractMailSessionSendingService implements MailSendingService{

	@Override
	public void send(MailMessage email) {
		executeEmailSend(email);
	}

	@Override
	public void send(MailMessage email, MailAsynchrounsCallback callback) {
		new EmailSenderThread(email,callback).start();
	}

	protected abstract Session getMailSession() throws MailException;

	private void executeEmailSend(MailMessage email){
		try {
			Session session = this.getMailSession();
			Transport.send(transformToMimeMessage(session,email));
		} catch (MessagingException e) {
			throw MailException.manage(e);
		}
	}

	protected Message transformToMimeMessage(Session session, MailMessage email) throws MessagingException{

		// create a message
		MimeMessage message = new MimeMessage(session);

		// set sender address
		message.setFrom( new InternetAddress(email.getFrom()));

		// set recipients
		int i;
		List<String> recipients = email.getRecipients(MailRecipientType.TO);
		Address[] adresses = new InternetAddress[recipients.size()];
		for (i=0; i<adresses.length ; i++){
			adresses[i] = new InternetAddress(recipients.get(i));
		}
		message.setRecipients(Message.RecipientType.TO, adresses);

		// set copy recipients
		recipients = email.getRecipients(MailRecipientType.CC);
		adresses = new InternetAddress[recipients.size()];
		for (i=0; i<adresses.length  ; i++){
			adresses[i] = new InternetAddress(recipients.get(i));
		}
		message.setRecipients(Message.RecipientType.CC, adresses);

		// set blind copy recipients
		recipients = email.getRecipients(MailRecipientType.BCC);
		adresses = new InternetAddress[recipients.size()];
		for (i=0; i<adresses.length  ; i++){
			adresses[i] = new InternetAddress(recipients.get(i));
		}
		message.setRecipients(Message.RecipientType.BCC, adresses);

		// set subject
		message.setSubject(email.getSubject());
		// set date
		message.setSentDate(email.getSendDate());


		// set body
		DataSource ds = new MediaManagedFileContentDataSource("body", email.getBody());

		MimeBodyPart bodyPart = new MimeBodyPart();
		bodyPart.setDataHandler(new DataHandler(ds));

		final Multipart mp = new MimeMultipart();
		mp.addBodyPart(bodyPart);


		final ManagedFileRepository attachments = email.getAttachments();
		ManagedFile root  = attachments.retrive(attachments.getRoots().iterator().next());

		for (ManagedFile attachment : root.children()){
			try{
				MimeBodyPart part = new MimeBodyPart();
				part.setDataHandler(new DataHandler(new ManagedFileDataSource((MediaManagedFile)attachment)));
				mp.addBodyPart(part);
			} catch (MessagingException e) {
				throw MailException.manage(e);
			}
		}
		
		

		// set contents    
		message.setContent(mp);

		// Updates the appropriate header fields of this message 
		// to be consistent with the message's contents.
		message.saveChanges();

		return message;

	}

	private class EmailSenderThread extends Thread {
		MailMessage email;
		MailAsynchrounsCallback callback;
		public EmailSenderThread(MailMessage email, MailAsynchrounsCallback callback) {
			super();
			this.email = email;
			this.callback = callback;
		}

		public void run(){
			try{
				executeEmailSend(email);
				callback.onSent(new MailTransmissionResult( email, null));
			} catch (Exception e){
				callback.onSent(new MailTransmissionResult( email, e));
			}
		}
	}
}
