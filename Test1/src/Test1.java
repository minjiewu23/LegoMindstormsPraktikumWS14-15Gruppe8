import lejos.nxt.Button;
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


public class Test1 {
	
	public boolean buttonPressed = false;
	
	public static void main (String args[]) throws Exception{
		//Test1 lego = new Test1();
		DifferentialPilot pilot = new DifferentialPilot(20, 100, Motor.A, Motor.B, true);
		UltrasonicSensor headsensor = new UltrasonicSensor(SensorPort.S2);
		int rotateSpeed = 200;
		int rotateRobotRight = -113;
		int rotateRobotLeft = 100;
		//Show ready
		System.out.println("Ready");
		
		Button.waitForAnyPress();
		
		//Go
		while(true){
			while(headsensor.getDistance() > 30){
				pilot.forward();
				lookWall(headsensor, pilot);
			}
			pilot.stop();
			Motor.C.rotate(-90); //Rotate Left
			int distanceLeft = headsensor.getDistance(); //Left Wall
			Motor.C.rotate(180); //Rotate null + Right (90 + 90)
			int distanceRight = headsensor.getDistance(); //Right Wall
			Motor.C.rotate(-90); //Rotate to null
			
			pilot.setRotateSpeed(rotateSpeed); //Rotate speed
			//Go to the direction where is more distance to the next wall
			if(distanceRight < distanceLeft) pilot.rotate(rotateRobotRight);
			else pilot.rotate(rotateRobotLeft);
			pilot.forward();
		}
	}

	private static void lookWall(UltrasonicSensor headsensor, DifferentialPilot pilot) {
		
		Motor.C.rotate(-90); //Rotate Left
		if(headsensor.getDistance() < 30){
			//pilot.stop();
			pilot.rotate(20); //Rotate Right
		}
		
		Motor.C.rotate(180); //Rotate null + Right (90 + 90)
		if(headsensor.getDistance() < 30){ //Right Wall
			//pilot.stop();
			pilot.rotate(-20); //Rotate Left
		}
		Motor.C.rotate(-90); //Rotate to null

		
	}
}
