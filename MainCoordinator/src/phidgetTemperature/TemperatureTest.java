package phidgetTemperature;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.phidgets.*;
import com.phidgets.event.*;

import mail.SendMail;
import phidgetTemperature.TemperatureTest;
import sound.Sound;
//import takePicture.TakePicture;


import org.joda.time.DateTime;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class TemperatureTest {

	static DateTime debut = DateTime.now();
	static boolean allume = true;

	static String enter = ""; //String pour pattern entrée kot
	static String mdp = "ULDR"; //mot de passe pattern pour rentrer
	static DateTime LedEnterTime = DateTime.now();
	static DateTime LedEnterTimeEnd = DateTime.now();
	static boolean wait = false;

	//BufferedImage imageTake;

	static int MoyenneSound = 0;
	static int TrueMoy = 0;
	static int acc = 0;
	static int accDouble = 0;
	static boolean AlreadyKnow = false;
	static int clap = 250;
	static int DoubleClap;
	static boolean DoubleBool = false;
	static int nbrClap;
	static DateTime dClap1 = DateTime.now();
	static DateTime dClap2 = DateTime.now();
	static DateTime dOuverture = DateTime.now(); //Test pour attendre avant de pouvoir reclaper des mains
	static boolean init = true;

	static long temps = 2000;                      // délai entre les claps

	static long startTime = 0;  

	static Timer timerStart = new Timer();
	static TimerTask tacheStart = new TimerTask() {     // cr�ation et sp�cification de la tache � effectuer
		@Override
		public void run() {
			init = false;
			System.out.println("Stop Init");
		}
	};

	static Timer timer = new Timer();
	static TimerTask tache = new TimerTask() {     // cr�ation et sp�cification de la tache � effectuer
		@Override
		public void run() {
			nbrClap = 0;
			System.out.println("Le clap à 0");
		}
	};


	static Timer timerMoy = new Timer();
	static TimerTask tacheMoy = new TimerTask() {     // cr�ation et sp�cification de la tache � effectuer
		@Override
		public void run() {

			if(MoyenneSound == 0 || acc == 0){
				MoyenneSound = 0;
			}else{
				MoyenneSound = MoyenneSound/acc;
			}

			System.out.println("La moyenne de l'intensit� sonore est de : "+MoyenneSound);

			if(MoyenneSound <10){
				clap = MoyenneSound + 180;
			}
			else{
				clap = MoyenneSound + 180;
			}

			System.out.println("Le clap est de : "+ clap);
		}
	};    
	static InterfaceKitPhidget ik;
	public static void main(String args[]) throws Exception {
		//InterfaceKitPhidget ik;



		System.out.println(Phidget.getLibraryVersion());
		ik = new InterfaceKitPhidget();

		/*timerMoy.schedule(tacheMoy, 6000);
		timerStart.schedule(tacheStart, 6000); //600.000 = 10 minutes*/




		ik.addAttachListener(new AttachListener() {
			@Override
			public void attached(AttachEvent ae) {
				System.out.println("attachment of " + ae);
			}
		});
		ik.addDetachListener(new DetachListener() {
			@Override
			public void detached(DetachEvent ae) { 
				System.out.println("detachment of " + ae);
			}
		});
		ik.addErrorListener(new ErrorListener() {
			@Override
			public void error(ErrorEvent ee) {
				System.out.println("error event for " + ee);
			}
		});
		ik.addInputChangeListener(new InputChangeListener() {
			@Override
			public void inputChanged(InputChangeEvent oe) {
				System.out.println(oe);
			}
		});
		ik.addOutputChangeListener(new OutputChangeListener() {
			@Override
			public void outputChanged(OutputChangeEvent oe) {
				//System.out.println(oe);

			}
		});

		ik.addSensorChangeListener(new SensorChangeListener() {
			@Override
			public void sensorChanged(SensorChangeEvent se) {
				// System.out.println(se);
				// System.out.println("Value : " +se.getValue());
				// System.out.println("Index : " +se.getIndex());

				if(se.getIndex()==0){

					if(se.getValue()>990) {
						// ik.setOutputState(0, true);
						try {
							System.out.println("Allum�e");
							ik.setOutputState(0 , true);
							//System.out.println("server address : " + se.getSource().getServerAddress());
							System.out.println("source: " + se.getClass());
						} catch (PhidgetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(se.getValue()==0){
						try {
							System.out.println("Ferm�e");
							ik.setOutputState(0 , false);
						} catch (PhidgetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				if(se.getIndex()==1){

					DecimalFormat df = new DecimalFormat("0.00");
					double temperature;
					temperature = (se.getValue()*0.2222)-61.111;

					System.out.println("Temperature : Donn�e change = "+ temperature);
					/* if(se.getValue()>400 && se.getValue()<500)
					 System.out.println("Value = " +se.getValue());*/

					if(temperature>25 && !AlreadyKnow){
						System.out.println("Au feu, ou sont les pompiers !?");		 
						SendMail sendmail = new SendMail();
						try {
							redLightFire(true);
						} catch (PhidgetException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						alarmFireSystem();
						try {
							String MailFire = "Bonjour, <br><br>Nos appareils ont d�tect� une temp�rature anormale dans votre kot. Elle est de "+df.format(temperature)+" degr�s celsius.<br><br>Il semblerait donc que votre kot est vraisemblablement en feu..<br><br>Bien � vous,<br><br>Smart Home Security System<br>";
							sendmail.sendMail("thomas.fransolet@hotmail.be", "FireAlert", MailFire);
						} catch (AddressException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (MessagingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						try {
							redLightFire(false);
						} catch (PhidgetException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}

				/* if(se.getIndex()==2){

					 System.out.println("Vibration : Donn�e change = " + se.getValue());

					 if(se.getValue()>420 && se.getValue()<540) {
						 intrusion(false);
					 }else{
						 intrusion(true); //and wait some seconds
						 alarmForceSystem(); 
						 SendMail sendmail = new SendMail();
						 try {
								String MailIntrusion = "Bonjour, <br><br>Nos appareils ont d�tect� une tentative d'intrusion. <br><br>Bien � vous,<br><br>Smart Home Security System<br>";
								sendmail.sendMail("thomas.fransolet@hotmail.be", "IntrusionAlert", MailIntrusion);
							} catch (AddressException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MessagingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

					 }
				 }*/


				if(se.getIndex()==3){

					//System.out.println("SoundDetector : Donn�e change = " + se.getValue());

					acc++;	

					//if(init){MoyenneSound = MoyenneSound + se.getValue();}


					MoyenneSound = MoyenneSound + se.getValue();

					TrueMoy = MoyenneSound/acc;

					//System.out.println("Moyenne = " + TrueMoy);

					//clap = TrueMoy + 200;
					clap = 250;

					//System.out.println("Clap = " + clap);




					if(se.getValue()>clap) {

						nbrClap++;

						//System.out.println("nbrClap = "+ nbrClap);

						if(nbrClap==1){
							
							dClap1 = DateTime.now();
							
							if(dClap1.getSecondOfDay()-dOuverture.getSecondOfDay()<2){
								//System.out.println("Tout doux mon lapin !");
								nbrClap = 0;
							}


						}

						if(nbrClap == 2){
							dClap2 = DateTime.now();
							
							if(dClap2.getSecondOfDay() - dClap1.getSecondOfDay()<2 && dClap2.getMillisOfDay() - dClap1.getMillisOfDay()>100){
								try {
									ik.setOutputState(0 , !ik.getOutputState(0));
									System.out.println("Ouverture/Fermeture lumi�re Clap");
								} catch (PhidgetException e) {
									e.printStackTrace();
								}
								dOuverture = DateTime.now();
								nbrClap = 0;
							}else{
								dClap1 = DateTime.now();
								nbrClap = nbrClap-1;
							}

						}
						//System.out.println("nbrClapApr�s = "+ nbrClap);
					}

				}

				if(se.getIndex()==4){
					System.out.println("Move detector : Donn�e change = " + se.getValue());

					if(se.getValue() > 100){
						DateTime dt = DateTime.now();
						if((dt.getMillis() - debut.getMillis())>1500){
							System.out.println("Je dois allumer/eteindre une lumiere");
							debut=dt;
							try {
								ik.setOutputState(0, allume);
								allume=!allume;
							} catch (PhidgetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}

				//String enter = "";
				//enter = enter + "a";
				//System.out.println(enter);

				if(se.getIndex()==5 && !wait){ // Axe X MiniStick

					if(se.getValue()<10){
						System.out.println("left");
						enter = enter + "L";
					}
					if(se.getValue()>980){
						System.out.println("right");
						enter = enter + "R";
					}
				}
				if(se.getIndex()==6 && !wait){ // Axe Y MiniStick

					if(se.getValue()<10){
						System.out.println("down");
						enter = enter + "D";
					}
					if(se.getValue()>985){
						System.out.println("up");
						enter = enter + "U";
					}
				}

				if(enter.equals(mdp)){

					System.out.println("You can enter !");
					enter = "";
					try {
						ik.setOutputState(0, true);
					} catch (PhidgetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					YouCanEnter();
					wait = true;

					LedEnterTime = DateTime.now();

				}

				LedEnterTimeEnd = DateTime.now();

				if(LedEnterTimeEnd.getSecondOfDay() - LedEnterTime.getSecondOfDay()>6){						//Eteins la lampe après ouverture porte kot (5 secondes)
					setLight(0, false);
					wait = false;
				}

				//System.out.println("Le pattern : "+ enter + " sa longueur : " + enter.length());

				for(int i = 0; i < enter.length(); i++){

					if(!enter.substring(0,i+1).equals(mdp.substring(0,i+1))){
						enter = "";
					}

				}

				/*	if(se.getIndex()==7){


					System.out.println("Luminosit� : Donn�e chanfin ge = " + se.getValue());

					if(se.getValue()>250){
						System.out.println("Luminosit� anormale..");		 
					}
				}*/


			}

			private void alarmForceSystem() {
				// TODO Auto-generated method stub

				Sound player = new Sound("sirens_x.wav");
				InputStream stream = new ByteArrayInputStream(player.getSamples()); 
				player.play(stream);

				//	takepicture();

				//+Take picture with camera

			}

			private void Beep() {
				// TODO Auto-generated method stub
				Sound player = new Sound("beep.wav");
				InputStream stream = new ByteArrayInputStream(player.getSamples()); 
				player.play(stream);
			}

			private void YouCanEnter() {
				// TODO Auto-generated method stub
				Sound player = new Sound("welcome.wav");
				InputStream stream = new ByteArrayInputStream(player.getSamples()); 
				player.play(stream);
			}

			/*	private void takepicture() {
				// TODO Auto-generated method stub
				TakePicture image = new TakePicture();

				imageTake = image.givePicture();
				//TakePicture.saveImage(imageTake);
			}*/

			private void intrusion(boolean b) {
				// TODO Auto-generated method stub
				try {
					ik.setOutputState(1, b);
				} catch (PhidgetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			private void alarmFireSystem() {
				// TODO Auto-generated method stub
				Sound player = new Sound("air_raid.wav");
				InputStream stream = new ByteArrayInputStream(player.getSamples()); 
				player.play(stream);
			}



			private void redLightFire(boolean on) throws PhidgetException {
				// TODO Auto-generated method stub

				ik.setOutputState(0, on);
			}

			private void setLight(int i, boolean b){			
				try {
					ik.setOutputState(i, b);
				} catch (PhidgetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}


		});
		ik.openAny();
		System.out.println("waiting for InterfaceKit attachment...");
		ik.waitForAttachment();
		System.out.println(ik.getDeviceName());
		Thread.sleep(500);
		/*	 System.out.println("input(7,8) = (" +
		 ik.getInputState(7) + "," +
		 ik.getInputState(8) + ")");*/
		/*	 if (false) {
			 System.out.print("closing...");
			 System.out.flush();
			 ik.close();
			 System.out.println(" ok");
			 System.out.print("opening...");
			 ik.openAny();
			 System.out.println(" ok");
			 ik.waitForAttachment();
		 }*/
		/* if (ik.getInputCount() > 8)
			 System.out.println("input(7,8) = (" +
					 ik.getInputState(7) + "," +
					 ik.getInputState(8) + ")");*/
		/* if (true) {
			 System.out.print("turn on outputs (slowly)...");
			 for (int i = 0; i < ik.getOutputCount() ; i++) {
				 ik.setOutputState(i, true);
				 try {
					 Thread.sleep(1000);
				 } catch (Exception e) {
				 }
			 }
			 System.out.println(" ok");
		 }*/
		/*	 if (false)
			 for (;;) {
				 try {
					 Thread.sleep(1000);
				 } catch (Exception e) {
				 }
			 }*/
		/* for (int j = 0; j < 1000 ; j++) {
			 for (int i = 0; i < ik.getOutputCount(); i++) { 
				 ik.setOutputState(i, true);
			 }
			 for (int i = 0; i < ik.getOutputCount(); i++)
				 ik.setOutputState(i, false);
		 }
		/* 	if (false) {
		 		System.out.println("toggling outputs like crazy");
		 		boolean o[] = new boolean[ik.getOutputCount()];
		 		for (int i = 0; i < ik.getOutputCount(); i++)
		 			o[i] = ik.getOutputState(i);
		 		for (int i = 0; i < 100000; i++) {
		 			int n = (int)(Math.random() * ik.getOutputCount());
		 			ik.setOutputState(n, !o[n]);
		 			System.out.println("setOutputState " + n +
		 					": " + !o[n]);
		 			o[n] = !o[n];
		 			try {
		 				Thread.sleep(1);
		 			} catch (Exception e) {
		 			}
		 		}
		 }*/
		System.out.println("Outputting events. Input to stop.");
		System.in.read();
		System.out.print("closing...");
		ik.close();
		//ik = null;
		System.out.println(" ok");
		/* if (false) {
			 System.out.println("wait for finalization...");
			 System.gc();
		 }*/
	} 
}
