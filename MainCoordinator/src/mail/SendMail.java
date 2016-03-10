package mail;

import javax.mail.*;
import javax.mail.internet.*;


public class SendMail
{
	public static final String FROM    = "smarthomephidget@gmail.com";
	public static final String SUBJECT = "Petit test";


public void sendMail(String to, String TypeAlert, String msg) throws AddressException, MessagingException
   {    
	   
	   EmailUtil EmailUtil = new EmailUtil();
       
       System.out.println("Procédure envoi de mail");
	   EmailUtil.sendEmailSSL(to, "[SmartHome - "+ TypeAlert +"]", msg);
	   System.out.println("Mail envoyé");
	    
   }

}