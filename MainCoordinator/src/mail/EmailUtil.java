package mail;

import java.io.File;
import java.util.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Multipart;

public class EmailUtil {
 
 /**
 * Ressource contenant les éléments statiques liés à la création et l'envoi d'un email.
 */
 private static ResourceBundle smtpBundle = ResourceBundle.getBundle("smtp");
 
 /**
 * Envoi d'un email utilisant une socket SSL (SSLSocketFactory).
 * @param to celui ou ceux qui doivent recevoir l'email (séparation des adresses par des virgules)
 * @param subject sujet de l'email
 * @param content contenu de l'email
 * @throws AddressException les adresses de destinations sont incorrectes
 * @throws MessagingException une erreur est survenue à l'envoi de l'email
 */
 public void sendEmailSSL(String to, String subject, String content) throws AddressException, MessagingException {
 // smtp properties
 Properties props = new Properties();
 /*props.put("mail.smtp.host", smtpBundle.getString("mail.smtp.host"));
 props.put("mail.smtp.socketFactory.port", smtpBundle.getString("mail.smtp.socketFactory.port"));
 props.put("mail.smtp.socketFactory.class", smtpBundle.getString("mail.smtp.socketFactory.class"));
 props.put("mail.smtp.auth", smtpBundle.getString("mail.smtp.auth"));
 props.put("mail.smtp.port", smtpBundle.getString("mail.smtp.port"));*/

 props.put( "mail.smtp.host", "smtp.gmail.com" );
 props.put( "mail.smtp.starttls.enable", "true" );
 props.put( "mail.smtp.auth", "true" );
 props.put( "mail.smtp.port", "587" );
 props.put( "mail.smtp.ssl.trust", "smtp.gmail.com" );
/* props.put( "mail.smtp.socketFactory.port", "587" );
 props.put( "mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory" );*/
 
 // authentification
 Session session = Session.getDefaultInstance(props,
 new javax.mail.Authenticator() {
 @Override
protected PasswordAuthentication getPasswordAuthentication() {
 return new PasswordAuthentication(smtpBundle.getString("mail.session.user"), smtpBundle.getString("mail.session.pass"));}
 }
 );
 
 
/* File file = new File("C:/Users/Thomas Fransolet/Pictures/Famille.jpg"); 
 FileDataSource datasource1 = new FileDataSource(file); 
 DataHandler handler1 = new DataHandler(datasource1); 
 
 MimeBodyPart testFile = new MimeBodyPart(); 
 try { 
	 testFile.setDataHandler(handler1); 
	 testFile.setFileName(datasource1.getName()); 
 } catch (MessagingException e) { 
     e.printStackTrace(); 
 }
 
 
 MimeBodyPart testPiece = new MimeBodyPart(); 
 try { 
	// testPiece.setContent("coucoucoucoucoucou", "text/plain");
	 testPiece.setText("écoucou");
 } catch (MessagingException e) { 
     e.printStackTrace(); 
 } 
 
 MimeMultipart mimeMultipart = new MimeMultipart(); 
 try { 
     mimeMultipart.addBodyPart(testPiece); 
  //   mimeMultipart.addBodyPart(testFile); 
 } catch (MessagingException e) { 
     e.printStackTrace(); 
 } */
 /*
//Première partie du message
BodyPart messageBodyPart = new MimeBodyPart();
messageBodyPart.setContent(content, "text/html; charset=ISO-8859-1");

//Ajout de la première partie du message dans un objet Multipart
Multipart multipart = new MimeMultipart();
multipart.addBodyPart(messageBodyPart);
*/

//Partie de la pièce jointe
/*messageBodyPart = new MimeBodyPart();
DataSource source = new FileDataSource("img1.png");
messageBodyPart.setDataHandler(new DataHandler(source));
messageBodyPart.setFileName("img1.png");*/

//Ajout de la partie pièce jointe
//multipart.addBodyPart(messageBodyPart);


//Create the message part
BodyPart messageBodyPart = new MimeBodyPart();
// Fill the message
messageBodyPart.setText( "TestAlex" );
// Create a multipar message
Multipart multipart = new MimeMultipart();
// Set text message part
multipart.addBodyPart( messageBodyPart );
// Part two is attachment
messageBodyPart = new MimeBodyPart();

/*
//C:\Users\Thomas Fransolet\Pictures
//C:\Users\Thomas Fransolet\workspace\MainCoordinator
String filename = "C:/new.png";
DataSource source = new FileDataSource( filename );
messageBodyPart.setDataHandler( new DataHandler( source ) );
messageBodyPart.setFileName( filename );
multipart.addBodyPart( messageBodyPart );*/


 
 // construct message
 Message message = new MimeMessage(session);
 
 try { 
 message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
 message.setSubject(subject);
// message.setContent(multipart);
 
// message.setContent("loilklk", "text/plain");
 message.setContent(content, "text/html; charset=ISO-8859-1");
// message.setContent(mimeMultipart);
 
 // send email
 Transport.send(message);
 
 } catch (MessagingException e) { 
	    e.printStackTrace(); 
	    System.out.println("CoucouProblemeEnvoiMail");
	} 
 }
}