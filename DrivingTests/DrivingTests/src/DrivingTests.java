import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class DrivingTests implements ButtonListener{
	
	public static void main(String[] args) throws Exception {
		DifferentialPilot pilot = new DifferentialPilot(20, 100, Motor.A, Motor.B);
		DrivingTests test = new DrivingTests();
		Button.ESCAPE.addButtonListener(test);
		test.travelStraight(pilot);
	}
	
	public void travelStraight(DifferentialPilot pilot) {
		pilot.setTravelSpeed(200);
		pilot.travel(-5000);
		pilot.stop();
	}

	@Override
	public void buttonPressed(Button b) {
		System.exit(0);
	}

	@Override
	public void buttonReleased(Button b) {
		// TODO Auto-generated method stub
		
	}
}
