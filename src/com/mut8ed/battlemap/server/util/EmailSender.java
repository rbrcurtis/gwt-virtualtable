package com.mut8ed.battlemap.server.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;
import org.apache.log4j.Logger;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.amazonaws.services.simpleemail.model.SendRawEmailResult;
import com.mut8ed.battlemap.shared.exception.EmailException;

public class EmailSender {
	final static Logger logger = Logger.getLogger(EmailSender.class);

	public static void main(String[] args) throws Exception {

		String to = "";
		Map<String, Object> attr = new HashMap<String, Object>();
		attr.put("","");
		sendEmail("welcome", attr, to);
		//		sendEmail(subject, body, to);

	}

	public static void sendEmail(String templateName, Map<String, Object> attributes, String to){

		try {
			StringTemplateGroup group = new StringTemplateGroup("laGroup", "/var/www/templates", DefaultTemplateLexer.class);
			StringTemplate st = group.getInstanceOf(templateName);
			
			st.setAttributes(attributes);

			Properties props = new Properties();
			props.load(new FileInputStream("/var/www/templates/subjects.properties"));

			String subject = props.get(templateName).toString();
			String body = st.toString();
			
			try {
				sendEmail(subject, body, to);
			} catch (Exception e) {
				throw new EmailException(e);
			}

		} catch (EmailException e){
			throw e;
		} catch (Exception e){
			logger.error(e,e);
		}

	}

	public static void sendEmail(String subject, String body, String to){
		try {

			logger.info("sending '"+subject+"' to "+to);

			// JavaMail representation of the message
			Session s = Session.getInstance(new Properties(), null);
			MimeMessage msg = new MimeMessage(s);

			// Sender and recipient
			msg.setFrom(new InternetAddress("noreply@mystictriad.com"));
			msg.addRecipient(
					Message.RecipientType.TO, 
					new InternetAddress(to));
			// Subject
			msg.setSubject(subject);

			// Add a MIME part to the message
			MimeMultipart mp = new MimeMultipart();

			BodyPart part = new MimeBodyPart();
			part.setContent(body, "text/html");
			mp.addBodyPart(part);

			msg.setContent(mp);

			// Set AWS access credentials
			AmazonSimpleEmailServiceClient client = 
					new AmazonSimpleEmailServiceClient(
							new BasicAWSCredentials(
									"",
									""));

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			msg.writeTo(out);

			RawMessage rm = new RawMessage();
			rm.setData(ByteBuffer.wrap(out.toString().getBytes()));

			SendRawEmailResult result = client.sendRawEmail(new SendRawEmailRequest(rm));
			System.out.println("send to "+to+" succeeded, returned result "+result.getMessageId());

		} catch (Exception e) {
			logger.error("send to address "+to+" returned "+e);
			throw new EmailException(e);
		}
	}
}
