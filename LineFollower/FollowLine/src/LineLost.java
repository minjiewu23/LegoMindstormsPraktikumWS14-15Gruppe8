import lejos.nxt.LightSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;


public class LineLost implements Behavior, LineDetectorListener{

	private DifferentialPilot pilot;
	
	private LineDetector detector;
	
	public LineLost(LightSensor sensor, DifferentialPilot pilot, int maxColorVal, LineDetector detector) {
		//detector.setListener(this);
		this.pilot = pilot;
		this.detector = detector;
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
//		when line is lost rotate by 5 deg and try to find it again
//		rotate until found...
		while (!detector.lineFound()) {
			pilot.rotate(5);
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
