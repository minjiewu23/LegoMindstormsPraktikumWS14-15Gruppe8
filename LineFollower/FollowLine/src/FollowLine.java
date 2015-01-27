import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class FollowLine implements ButtonListener {
	
	public static void main(String[] args) throws InterruptedException {
		
		Sound.setVolume(50);
		Sound.beep();
		
		FollowLine followLine = new FollowLine();
		Button.ESCAPE.addButtonListener(followLine);
		LightSensor sensor = new LightSensor(SensorPort.S1);
		sensor.calibrateLow();
		NXTRegulatedMotor leftMotor = Motor.A;
		NXTRegulatedMotor rightMotor = Motor.B;
		DifferentialPilot pilot = new DifferentialPilot(20, 100, leftMotor, rightMotor);
		int maxLight = 50;
		DriveForward forwardDriver = new DriveForward(sensor, pilot, maxLight);
		LineLost lineLost = new LineLost(sensor, pilot, maxLight);
		Behavior[] behaviours = {forwardDriver, lineLost};
		Arbitrator follower = new Arbitrator(behaviours, true);
		Thread.sleep(100);
		Sound.beep();
		follower.start();
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
