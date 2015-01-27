import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;


public class SensorTests {
	
	public static void main(String[] args) throws Exception {
		SensorTests.rotateSensor();
	}
	
	public static void rotateSensor() throws Exception {
		NXTRegulatedMotor sensorMotor = Motor.C;
		//test change
		//rotate Sensor To Positions (should be tested, which is the 0 Position)
		sensorMotor.rotateTo(0);
		Thread.sleep(1000);

		sensorMotor.rotateTo(90);
		Thread.sleep(1000);
		
		sensorMotor.rotateTo(180);
		Thread.sleep(1000);
		
		sensorMotor.rotateTo(270);
		Thread.sleep(1000);
	}
}
