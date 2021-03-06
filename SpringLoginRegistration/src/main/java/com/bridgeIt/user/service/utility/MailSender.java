package com.bridgeIt.user.service.utility;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailSender {

	
	@Autowired
	JavaMailSender mailSender;
	
	
	public void consumed(UserMail mail) {
		
	
		
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", "localhost");
		Session session = Session.getDefaultInstance(properties);
		MimeMessage mimeMessage = new MimeMessage(session);
		try {
			mimeMessage.setFrom(new InternetAddress("tradefinancebridgelabz@gmail.com"));
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.getTo()));
			mimeMessage.setSubject("Trade Finance verification");
			mimeMessage.setText(mail.getMessage());
		} catch (AddressException e) {
			
			e.printStackTrace();
		} catch (MessagingException e) {
			
			e.printStackTrace();
		}

		mailSender.send(mimeMessage);
		System.out.println("mail is sent");
		
	      
	}
	
}

