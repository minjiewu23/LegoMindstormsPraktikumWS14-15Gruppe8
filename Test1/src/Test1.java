import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * Test für Mov + Ultrasonic + Links + Rechs 
 */

/**
 * Motor A: Left
 * Motor B: Right
 * Motor C: Sensor
 * Port 1: Light
 * Port 2: Ultrasonic
 *
 */


public class Test1 implements ButtonListener {
	
	public boolean buttonPressed = false;
	
	public static void main (String args[]) throws Exception{
		Test1 lego = new Test1();
		DifferentialPilot pilot = new DifferentialPilot(20, 100, Motor.A, Motor.B, true);
		UltrasonicSensor headsensor = new UltrasonicSensor(SensorPort.S2);
		
		for(int i=0; i<10; i++){
			int angle = Motor.C.getTachoCount(); // should be -360
			System.out.println(angle);
			Motor.C.rotateTo(10);
		}
		
		//Reset motor C
		Motor.C.rotateTo(0);
		//Show ready
		System.out.println("Ready");
		//Go
		//Lab: Left - Left - Right - Rigt - Left
		while(headsensor.getDistance() > 30){
			pilot.forward();
		}
		
		while(headsensor.getDistance() < 30){
			pilot.setRotateSpeed(10);
			pilot.rotateLeft();
		}
		
		while(headsensor.getDistance() > 30){
			pilot.forward();
		}
	
		
		/*//Reset motor C
		Motor.C.rotateTo(0);
		//Show ready
		System.out.println("Ready");
		//Push boton to start
		Button.ENTER.addButtonListener(lego);
		//Distance
		distance = headsensor.getDistance();
		//Go
		while(distance < 12){
			pilot.setTravelSpeed(200);
			pilot.travel(50);
			distance = headsensor.getDistance();
		}
		//Wall? -> stop
		pilot.stop();
		//Right free? -> Right
		//Right no free -> Left free? -> Left */
	}
		
	@Override
	public void buttonPressed(Button b) {
		while(b != Button.ENTER);
	}

	@Override
	public void buttonReleased(Button b) {
		// TODO Auto-generated method stub
		
	}
}
