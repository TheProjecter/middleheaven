package org.middleheaven.logging.writters.mail;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.middleheaven.core.bootstrap.ServiceRegistry;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.logging.LogBookWriter;
import org.middleheaven.logging.LogWritingIOExcepiton;
import org.middleheaven.logging.LoggingConfiguration;
import org.middleheaven.logging.LoggingEvent;
import org.middleheaven.logging.writters.AbstractLogFormat;
import org.middleheaven.logging.writters.FormatableLogWriter;
import org.middleheaven.logging.writters.LogFormat;
import org.middleheaven.mail.MailMessage;
import org.middleheaven.mail.MailSendingService;
import org.middleheaven.util.StringUtils;

/**
 * 
 */
public class LogEmailWriter extends LogBookWriter implements FormatableLogWriter {

    private List<LoggingEvent> buffer = new ArrayList<LoggingEvent>();
    private int bufferMax = 1;
    private String emailTo = null;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH'h'mm");

    private LogFormat format = new AbstractLogFormat(){

        public void writerHeader(PrintWriter writer) throws LogWritingIOExcepiton {
        	writer.println("This is an automatic message. Please do not respond to it.");
        }

        public void format(LoggingEvent event,PrintWriter message ) throws LogWritingIOExcepiton {
            message.append(dateFormat.format(new Date(event.getTime())));
            message.append(" - ");
            message.append(event.getLevel().toString());
            message.append(" - ");
            message.append(fillParamterHolders(event));
            message.append("\n");
            if (event.hasThrowable()){
                message.append("\n");
                message.append("Stacktrace:");
                message.append("\n");
                message.append(StringUtils.getStackStrace(event.getThrowable()));
                message.append("\n");
            }

        }

    };



    public LogEmailWriter(){

    }

    public void config(Map<String,String> params, LoggingConfiguration configuration) {
        String s = (String)params.get("buffer.size");
        if (s!=null){
            bufferMax = Integer.parseInt(s.trim());
        }
        emailTo = (String)params.get("mail.to");

    }

    public void setLogFormat(LogFormat format) {
        this.format = format;

    }

    public LogFormat getLogFormat() {
        return format;
    }

    public void write(LoggingEvent event) {
        buffer.add(event);
        if (buffer.size() >= bufferMax ){
            this.sendEmail();
        }
    }

    protected void sendEmail()  {
        if (emailTo!=null && buffer.size()>0){

        	MailSendingService emailService = ServiceRegistry.getService(MailSendingService.class);
        	

            MailMessage email = new MailMessage();
            String[] mails = emailTo.trim().split(",");
            email.addToAdress(mails);

            email.setSubject("Log Events" + dateFormat.format(new Date()));
            email.setFrom("midleheave.logging@" + getPrimaryAddress().toString());

            PrintWriter message  = new PrintWriter(new ByteArrayOutputStream());

            format.writerHeader(message);
            for (int i =0; i < buffer.size();i++){
                LoggingEvent event = (LoggingEvent)buffer.get(i);
                format.format(event,message);
            }
            format.writerFooter(message);

            email.setBody(message.toString());
            emailService.send(email);
            buffer.clear();
        }

    }
    
    public static InetAddress getPrimaryAddress(){
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			throw ManagedIOException.manage(e);
		}
	}


}
