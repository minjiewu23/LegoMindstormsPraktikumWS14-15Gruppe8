import lejos.nxt.LCD;
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
	
	private int lineBorderLightValue = 35;
	
	private int lightOffset = 5;
	
	private int arcStep = 2;
	
	private SIDE lineSide = SIDE.LEFT;
	
	private HIGH highState = HIGH.INSIDE;
	
	private DifferentialPilot pilot;
	
	private Pose pose = new Pose();
	
	private boolean follow = true;

	private LightSensor sensor;
	
	public static void main(String[] args) throws InterruptedException {
		DifferentialPilot pilot = new DifferentialPilot(20, 100, Motor.A, Motor.B);
		LightSensor sensor = new LightSensor(SensorPort.S1);
		LineFollower follower = new LineFollower(pilot, sensor);
		follower.startFollowing();
	}
	
	public LineFollower(DifferentialPilot pilot, LightSensor sensor) {
		this.pilot = pilot;
		this.sensor = sensor;
	}
	
	public void startFollowing() throws InterruptedException {
		this.stepLeft();
		Thread.sleep(1000);
		this.stepRight();
		Thread.sleep(1000);
		while (follow) {
			LCD.refresh();
			int lightValue = this.sensor.getLightValue() - lineBorderLightValue;
			LCD.drawString("Val:"+this.sensor.getLightValue(), 0, 0);
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
				LCD.drawString("outside", 0, 1);
				if (lineSide == SIDE.LEFT) {
					this.stepRight();
				}
				if (lineSide == SIDE.RIGHT) {
					this.stepLeft();
				}
			} else {
				LCD.drawString("inside", 0, 1);
				if (lineSide == SIDE.LEFT) {
					this.stepLeft();
				}
				if (lineSide == SIDE.RIGHT) {
					this.stepRight();
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
		this.pilot.travelArc(50, 300);
	}
	
	private void arcleft() {
		this.pilot.travelArc(500, -20);
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
