package main;

import java.awt.image.BufferedImage;

//import mail.SendMail;
import phidgetTemperature.TemperatureTest;
//import takePicture.demo;
import takePicture.TakePicture;
/*import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;*/

import com.phidgets.*;
import com.phidgets.event.*;

public class MainCoordinator {

	BufferedImage image;
	
	public static final void main(String args[])  throws Exception {
		
		/*String MailFire = "";
		MailFire += "<br>";
		MailFire += "Votre kot est entrain de bruler tr�s ch�re.. ";
		MailFire += "<br>";
		MailFire += "Bien � vous,";
		MailFire += "<br>";
		MailFire += "Smart Home Security System";
		
		SendMail sendmail = new SendMail();
		sendmail.sendMail("thomas.fransolet@hotmail.be", "FireAlert", MailFire);*/
		
		
		//TakePicture imageT = new TakePicture();

		takepicture();
		
		
		/*TakePicture image = new TakePicture();
		
		BufferedImage imageTake = image.givePicture();*/
		
				
		
		/*String mdp = "LR";
		System.out.println(mdp.substring(0, 2));*/
		
		//demo.captureFrame();
		
		@SuppressWarnings("unused")
		TemperatureTest temperature = new TemperatureTest();
		TemperatureTest.main(args);
	
	}
	
	private static void takepicture() {
		// TODO Auto-generated method stub
		TakePicture image = new TakePicture();
		
		BufferedImage imageTake = image.givePicture();
		//TakePicture.saveImage(imageTake);
	}



	

	
}
