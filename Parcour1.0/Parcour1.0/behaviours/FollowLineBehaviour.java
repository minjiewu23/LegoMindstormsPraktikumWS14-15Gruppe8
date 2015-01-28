import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.subsumption.Behavior;


public class FollowLineBehaviour implements Behavior{
	
	private enum LineSide {
		LEFT,
		RIGHT,
		NONE
	}
	
	private enum State {
		//the Robot knows where the line is and is traveling along it
		ONLINE,
		//the Robot lost the line
		LINE_LOST,
		//no line has been found yet
		NO_LINE
	}
	
	private LineSide sideOfLine = LineSide.NONE;
	
	private State state = State.NO_LINE;
	
	private ContinuousPilot pilot;
	
	private NXTRegulatedMotor sensorMotor;
	
	private LightSensor lightSensor;
	
	public FollowLineBehaviour(ContinuousPilot pilot, NXTRegulatedMotor sensorMotor, LightSensor lightSensor) {
		this.pilot = pilot;
		this.sensorMotor = sensorMotor;
		this.lightSensor = lightSensor;
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		if (this.state == State.NO_LINE) {
			
		} else if (this.state == State.LINE_LOST) {
			
		} else if (this.state == State.ONLINE) {
			
		}
	}
	
	public void searchLineBeginning() {
//		drive a little bit forward and scan with the sensorMotor
		pilot.forward();
		int arc = 90;
		while (arc > -90) {
			sensorMotor.rotateTo(arc);
			if (sensorSeesLine()) {
				this.state = State.ONLINE;
				/**
				 * when line is found check for the side the line is on
				 * and rotate a bit to the other side to ensure that the robot is on the other side 
				 */
				if (arc > 0) {
					this.pilot.rotateRightBy(2);
					this.sideOfLine = LineSide.LEFT;
				} else {
					this.sideOfLine = LineSide.RIGHT;
					this.pilot.rotateLeftBy(2);
				}
				sensorMotor.rotateTo(0);
			}
			
			/**
			 * rotate the sensor and check the line again
			 */
			arc -= 4;
		}
	}
	
	public boolean sensorSeesLine() {
		return (this.lightSensor.getLightValue() > Consts.LINE_SEPERATION_VALUE)?true:false;
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
