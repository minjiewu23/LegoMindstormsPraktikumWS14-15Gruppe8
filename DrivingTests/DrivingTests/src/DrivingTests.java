import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;

public class DrivingTests {
	public static void main(String[] args) throws Exception {
		DifferentialPilot pilot = new DifferentialPilot(20, 100, Motor.A, Motor.B);
		DrivingTests.travelStraight(pilot);
	}
	
	public static void travelStraight(DifferentialPilot pilot) {
		pilot.setTravelSpeed(200);
		pilot.travel(5000);
		pilot.stop();
	}
}
