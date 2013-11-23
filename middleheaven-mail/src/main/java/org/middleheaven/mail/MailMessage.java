package org.middleheaven.mail;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.middleheaven.io.StreamableContent;
import org.middleheaven.io.repository.CharSequenceMediaManagedFileContent;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.set.SetManagedFileRepository;



/**
 * An email message. This class facilitates email creation abstracting all technology dependencies
 * 
 */
public class MailMessage implements Serializable {
 
	private static final long serialVersionUID = 1L;
	
	private StreamableContent body;
	private String from;
    private String subject;
    private Map<MailRecipientType,List<String>> recipients = new EnumMap<MailRecipientType, List<String>>(MailRecipientType.class);
    private Date sendDate;
	private SetManagedFileRepository attachments = SetManagedFileRepository.newInstance(); 
	private MailPriority priority = MailPriority.NORMAL; 
	
	public MailPriority getPriority() {
		return priority;
	}

	public void setPriority(MailPriority priority) {
		this.priority = priority;
	}

	/**
     * Message Sender
	 * @param senderEmailAddress
	 */ 
	public MailMessage setFrom(String senderEmailAddress) {
        this.from = senderEmailAddress;
        return this;
	}
	 
    /**
     * Message subject
     * @param subjectEmailAddress
     */
	public MailMessage setSubject(String subjectEmailAddress) {
        this.subject = subjectEmailAddress;
        return this;
	}
	 
    /**
     * Message body as a simple , unformatted string
     * @param body
     */
	public MailMessage setBody(CharSequence body) {
	    this.body = new CharSequenceMediaManagedFileContent(body, "text/plain");
	    return this;
    }
	
	  /**
     * Message body as a formatted string. 
     * @param body
     * @param contentType determines the type of formatting, e.g. {@code text/html} will be interpreted as a HTML formatting.
     */
	public MailMessage setBody(CharSequence body, String contentType) {
		this.body = new CharSequenceMediaManagedFileContent(body, contentType);
	    return this;
    }
	 
	 
    /**
     * Add an attachment
     * @param atachment to be added
     */
	public MailMessage addAtachment(ManagedFile attachment) {
		if (!attachment.getContent().isContentTypeReadable()){
			throw new IllegalArgumentException("Atachment must have readable content type");
		}
        attachments.store(attachment);
        return this;
	}
	

    /**
     * Add a recipient address
     * @param adress
     */
    public MailMessage addToAdress(String ... adress){
        addRecipients(MailRecipientType.TO, adress);
        return this;
    }
    
    /**
     * Add a copy recipient address
     * @param adress
     */
    public MailMessage addCCAdress(String ... adress){
        addRecipients(MailRecipientType.CC, adress);
        return this;
    }
    
    /**
     * Add a blind copy recipient address
     * @param adress
     */
    public MailMessage addBCCAdress(String ... adress){
        addRecipients(MailRecipientType.BCC, adress);
        return this;
    }

	public void addRecipients(MailRecipientType type, String ... addresses) {
        List<String> adresses = this.recipients.get (type);
        if (adresses==null){
            adresses = new ArrayList<String>();
        }
        
        for (String address : addresses){
            adresses.add(address);
        }

        this.recipients.put(type,adresses);
            
	}
	 
	public List<String> getRecipients(MailRecipientType type) {
		List<String> adresses = this.recipients.get(type);
        if (adresses==null){
        	return Collections.emptyList();
        } else {
        	return Collections.unmodifiableList(adresses);
        }
	}
	 
    /**
     * 
     * @return sender address
     */
	public String getFrom() {
		return this.from;
	}
	 
    /**
     * 
     * @return message body
     */
	public StreamableContent getBody() {
		return this.body;
	}
	 
	
	public ManagedFileRepository getAttachments(){
		return this.attachments;
	}
	 
    /**
     *  
     * @return message subject
     */
	public String getSubject() {
		return this.subject;
	}
	 
	/**
	 * The send date. If not defined return the current date
	 */
	public Date getSendDate() {
		return this.sendDate==null ? new Date(): (Date)this.sendDate.clone();
	}
	 
    /**
     * 
     * The apparent date the email is sent. This is not the real date when the 
     * email is send, is only the date the user wants to appear as the send date.
     * @param date - email sending date
     */
	public void setSendDate(Date date) {
        this.sendDate = (Date)date.clone(); 
	}

	 
    
    
}
 
