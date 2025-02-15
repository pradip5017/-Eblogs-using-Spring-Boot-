package com.eblog.demo.service;

import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/*
 MailSender interface: the top-level interface that provides basic functionality for sending simple emails
JavaMailSender interface: the subinterface of the above MailSender. It supports MIME messages and is mostly used in conjunction with the MimeMessageHelper class for the creation of a MimeMessage. It's recommended to use the MimeMessagePreparator mechanism with this interface.
JavaMailSenderImpl class provides an implementation of the JavaMailSender interface. It supports the MimeMessage and SimpleMailMessage.
 MimeMessageHelper class: helper class for the creation of MIME messages. It offers support for images, typical mail attachments and text content in an HTML layout.
 */
/*
 *  #SMTP is used to transmit e-mail between e-mail servers and from e-mail clients to e-mail servers
#STARTTLS is a Channel Security Upgrade for safer delivery of message. 
It tells an email server that an email client (including an email client running in a web browser) wants to turn an existing insecure connection into a secure one.
 #TLS (Transport Layer Security) are standard protocols used to secure email transmissions. 
 These protocols encrypt connections between two computers over the internet
 Multipurpose Internet Mail Extensions (MIME) is an Internet standard that is used to support the transfer of single or multiple text and non-text attachments. Non-text attachments can include graphics, audio, and video files.
 */


@Service
public class EmailService {
	@Value("${spring.mail.username}")
	private String fromEmail; //pujaxyz
		
	@Autowired
	private JavaMailSender javaMailSender;
	
	                      //    vai                   
	public  boolean  sendMail(String to,String subject,String body)
	{
		try {
			
			MimeMessage mimeMessage =javaMailSender.createMimeMessage();
			
			
			
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
			mimeMessageHelper.setFrom(fromEmail);
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(body);
			
			mimeMessage.setContent(body,"text/html");
		    javaMailSender.send(mimeMessage);
			return true;
			
		} catch (Exception e) {
			return false;
		}
	}
	                          //6
  public int generateOTP(int otpLength) {
    StringBuilder otp = new StringBuilder();  //Otp ="", Otp=636366
    
    //int
    
    Random random = new Random();
    
    
       //  6        6   <6  0,1,2,3,4,5
    for (int i =0; i < otpLength; i++) {
      otp.append(random.nextInt(10));  //1-10    =>   735816
    }
    
    
    return Integer.parseInt(otp.toString());
  }
}
