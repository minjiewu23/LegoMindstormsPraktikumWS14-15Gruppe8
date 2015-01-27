import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Pose;


public class LineFollower {

	private enum SIDE {
		LEFT,
		RIGHT
	};

	private enum HIGH {
		INSIDE,
		OUTSIDE
	};
	
	private int lineBorderLightValue = 40;
	
	private int lightOffset = 5;
	
	private int arcStep = 5;
	
	private SIDE lineSide = SIDE.LEFT;
	
	private HIGH highState = HIGH.INSIDE;
	
	private DifferentialPilot pilot;
	
	private Pose pose = new Pose();
	
	private boolean follow = true;

	private LightSensor sensor;
	
	public static void main(String[] args) {
		DifferentialPilot pilot = new DifferentialPilot(20, 100, Motor.A, Motor.B);
		LightSensor sensor = new LightSensor(SensorPort.S1);
		LineFollower follower = new LineFollower(pilot, sensor);
		follower.startFollowing();
	}
	
	public LineFollower(DifferentialPilot pilot, LightSensor sensor) {
		this.pilot = pilot;
		this.sensor = sensor;
	}
	
	public void startFollowing() {
		while (follow) {
			int lightValue = this.sensor.getLightValue() - lineBorderLightValue;
			boolean inside = true;
			if (lightValue < 0 && highState == HIGH.INSIDE) {
				inside = false;
			} else if (lightValue > 0 && highState == HIGH.OUTSIDE) {
					inside = false;
				}
			if (Math.abs(lightValue) < lightOffset) {
				pilot.travel(-10);
				this.pose = new Pose();
			} else if (!inside) {
				if (lineSide == SIDE.LEFT) {
					this.stepRight();
				}
				if (lineSide == SIDE.RIGHT) {
					this.stepLeft();
				}
			} else {
				if (lineSide == SIDE.LEFT) {
					this.arcleft();
				}
				if (lineSide == SIDE.RIGHT) {
					this.arcRight();
				}
			}
			if (pose.getHeading() > 90) {
				this.pilot.rotate(-95);
				this.pose.rotateUpdate(-95);
			} else if (pose.getHeading() < -90) {
				this.pilot.rotate(95);
				this.pose.rotateUpdate(95);
			}
		}
	}
	
	private void arcRight() {
		this.pilot.travelArc(30, 10);
	}
	
	private void arcleft() {
		this.pilot.travelArc(-30, 10);
	}
	
	private void stepRight() {
		this.pilot.rotate(arcStep);
		this.pose.rotateUpdate(arcStep);
	}
	
	private void stepLeft() {
		this.pilot.rotate(-arcStep);
		this.pose.rotateUpdate(-arcStep);
	}
	
}
