import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;


public class FollowLine implements ButtonListener {

	public static int change_arc = 15;

	public static void main(String[] args) throws InterruptedException {
		FollowLine followLine = new FollowLine();
		Button.ESCAPE.addButtonListener(followLine);
		LightSensor sensor = new LightSensor(SensorPort.S1);
		sensor.calibrateLow();
		NXTRegulatedMotor leftMotor = Motor.A;
		NXTRegulatedMotor rightMotor = Motor.B;
		DifferentialPilot pilot = new DifferentialPilot(20, 100, leftMotor, rightMotor);
		int maxLight = 50;
		Runnable scanner = new Scanner();

		Runnable detector = new LineDetector(50, (Scanner) scanner);
		((Scanner) scanner).addListener((LineDetector)detector);
		DriveForward forwardDriver = new DriveForward(sensor, pilot, maxLight, (LineDetector) detector);
		LineLost lineLost = new LineLost(sensor, pilot, maxLight, (LineDetector) detector);
		Behavior[] behaviours = {lineLost, forwardDriver};
		Arbitrator follower = new Arbitrator(behaviours, true);
		new Thread(scanner).start();
		new Thread(detector).start();
		follower.start();
//		((Scanner) scanner).stopScanning();
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
