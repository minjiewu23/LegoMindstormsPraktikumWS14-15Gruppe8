import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class DriveForward implements Behavior, LineDetectorListener {

	private DifferentialPilot pilot;

	private LineDetector detector;

	private boolean drive = true;
	
	public DriveForward(LightSensor sensor, DifferentialPilot pilot,
			int maxColorVal, LineDetector detector) {
		this.pilot = pilot;
		this.detector = detector;
	}

	@Override
	public boolean takeControl() {
		if (detector.lineFound()) {
			return true;
		}
		return true;
	}

	@Override
	public void action() {
		this.drive = true;
		LCD.drawString("Drive Forward", 0, 0);
		while (this.drive) {
			int arc = detector.getLinePosition();
			LCD.drawString("Arc: "+arc, 0, 1);
			if (detector.lineFound()) {
				if (Math.abs(arc) < FollowLine.change_arc) {
					pilot.travel(-20);
				} else if (detector.lineFound()) {
					if (arc < 0) {
						pilot.rotate(arc/2);
					} else {
						pilot.rotate(arc/2);
					}
				}
				
			}
		}
	}

	@Override
	public void suppress() {
		this.drive = false;
		pilot.stop();
	}

	@Override
	public void lineFound() {
		// TODO Auto-generated method stub
	}

}
