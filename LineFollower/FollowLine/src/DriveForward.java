import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class DriveForward implements Behavior, LineDetectorListener {

	private LightSensor sensor;

	private DifferentialPilot pilot;

	private int maxColorVal;

	private LineDetector detector;

	private int arc = 0;

	private boolean drive = true;
	
	private Scanner scanner;

	public DriveForward(LightSensor sensor, DifferentialPilot pilot,
			int maxColorVal, LineDetector detector) {
		// (detector).setListener(this);
		this.pilot = pilot;
		this.sensor = sensor;
		this.maxColorVal = maxColorVal;
		this.detector = detector;
	}

	@Override
	public boolean takeControl() {
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
						pilot.travelArc(-50, -10);
					} else {
						pilot.travelArc(50, -10);
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
		LCD.drawString("found", 0, 0);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		arc = Motor.C.getTachoCount();
	}

}
