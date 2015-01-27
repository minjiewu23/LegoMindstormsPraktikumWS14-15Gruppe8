import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;


public class LineLost implements Behavior, LineDetectorListener{

	private LightSensor sensor;

	private DifferentialPilot pilot;
	
	private int maxColorVal;
	
	private LineDetector detector;
	
	public LineLost(LightSensor sensor, DifferentialPilot pilot, int maxColorVal, LineDetector detector) {
		//(detector).setListener(this);
		this.pilot = pilot;
		this.sensor = sensor;
		this.maxColorVal = maxColorVal;
		this.detector = detector;
	}

	@Override
	public boolean takeControl() {
//		if (this.detector.getLinePosition() > FollowLine.change_arc) {
//			return true;
//		}
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

	@Override
	public void lineFound() {
		// TODO Auto-generated method stub
		
	}
}
