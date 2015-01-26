import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;


public class DrivingTests {
	public static void main(String[] args) {
		DifferentialPilot pilot = new DifferentialPilot(20, 100, Motor.A, Motor.B);
		pilot.setTravelSpeed(100);
		pilot.forward();
	}
}
