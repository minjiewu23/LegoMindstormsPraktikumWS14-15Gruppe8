import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;


public class DriveForward implements Behavior{

	private LightSensor sensor;
	
	private DifferentialPilot pilot;
	
	private int maxColorVal;
	
	public DriveForward(LightSensor sensor, DifferentialPilot pilot, int maxColorVal) {
		this.pilot = pilot;
		this.sensor = sensor;
		this.maxColorVal = maxColorVal;
	}
	
	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		LCD.drawString("Drive Forward", 0, 0);
		Sound.beep();
		pilot.travel(-100, true);
	}

	@Override
	public void suppress() {
		pilot.stop();
	}

}
