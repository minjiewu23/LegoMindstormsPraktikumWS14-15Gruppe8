import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;


public class LineLost implements Behavior{

	private LightSensor sensor;

	private DifferentialPilot pilot;
	
	private int maxColorVal;
	
	public LineLost(LightSensor sensor, DifferentialPilot pilot, int maxColorVal) {
		this.pilot = pilot;
		this.sensor = sensor;
		this.maxColorVal = maxColorVal;
	}

	@Override
	public boolean takeControl() {
		if (sensor.getLightValue() <= maxColorVal) {
			return true;
		}
		return false;
	}

	@Override
	public void action() {
		LCD.drawString("Line Lost", 0, 0);
		Sound.beepSequence();
		int stepWidth = 10;
		int distance = 10;
		int radius = 100;
		while (sensor.getLightValue() <= maxColorVal) {
			pilot.travelArc(radius, -distance);
			distance += stepWidth;
			radius *= -1;
		}
	}

	@Override
	public void suppress() {
		pilot.stop();
	}
}
