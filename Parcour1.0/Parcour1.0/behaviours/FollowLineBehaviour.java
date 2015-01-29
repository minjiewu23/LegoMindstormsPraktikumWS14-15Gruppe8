import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;


public class FollowLineBehaviour implements Behavior{
	
	/**
	 * which line part of the parcour is it
	 * @author steffen
	 *
	 */
	public enum LinePart {
		FIRST,
		SECOND,
		THIRD
	}
	
	private enum LineSide {
		LEFT,
		RIGHT,
		NONE
	}
	
	private enum PanDirection {
		LEFT,
		RIGHT
	}
	
	private enum State {
		//the Robot knows where the line is and is traveling along it
		ONLINE,
		//the Robot lost the line
		LINE_LOST,
		//no line has been found yet
		NO_LINE,
		LINE_FINISHED
	}
	
	/**
	 * when the line is lost, the robot turns in one direction and then in the other
	 * while trying to find the line again
	 * if it was not able to find the line by turning in oben or the other direction
	 * the line did end
	 */
	private enum LineLostState {
		NOT_SEARCHED,
		SEARCHED_FIRST_DIRECTION,
		SEARCHED_SECOND_DIRECTION
	}
	
	private LineSide sideOfLine = LineSide.NONE;
	
	private State state = State.NO_LINE;
	
	private LineLostState lineLostState = LineLostState.NOT_SEARCHED;
	
	private int pansInOneDirection = 0;
	
	private PanDirection lastPanDiretion = PanDirection.LEFT; 
	
	private ContinuousPilot pilot;
	
	private NXTRegulatedMotor sensorMotor;
	
	private LightSensor lightSensor;
	
	private LinePart linePart;
	
	private NXTRegulatedMotor leftMotor;
	
	private NXTRegulatedMotor rightMotor;
	
	public FollowLineBehaviour(ContinuousPilot pilot, LinePart linePart) {
		leftMotor = Consts.LEFT_MOTOR;
		rightMotor = Consts.RIGHT_MOTOR;
		this.pilot = pilot;
		pilot.setSpeed(4);
		leftMotor.setSpeed(pilot.getSpeedLeft());
		rightMotor.setSpeed(pilot.getSpeedRight());
		this.sensorMotor = Consts.SENSOR_MOTOR;
		this.lightSensor = Consts.LIGHT_SENSOR;
		this.linePart = linePart;
	}

	@Override
	public boolean takeControl() {
		if (this.state == State.LINE_FINISHED) {
			return false;
		}
		return true;
	}

	@Override
	public void action() {
		LCD.clear();
		if (this.state == State.NO_LINE) {
			LCD.drawString("Searching Line", 0, 0);
			this.searchLineBeginning();
		} else if (this.state == State.LINE_LOST) {
			LCD.drawString("LineLost", 0, 0);
			this.lineLost();
		} else if (this.state == State.ONLINE){
			LCD.drawString("online", 0, 0);
			this.followLine();
		} else {
			Sound.beepSequence();
		}
	}
	
	public void followLine() {
		//pilot.forward();
		if (this.sideOfLine == LineSide.LEFT) {
			sensorMotor.rotateTo(Consts.SENSOR_MOTOR_LEFT);
		} else if (this.sideOfLine == LineSide.RIGHT) {
			sensorMotor.rotateTo(Consts.SENSOR_MOTOR_RIGHT);
		}
		int lightValue = lightSensor.getLightValue();
		int diff = lightValue - Consts.LINE_SEPERATION_VALUE;
		if (this.sideOfLine == LineSide.RIGHT) {
			if (diff > 0) {
//				not in line
				right();
			} else {
				left();
			}
		} else if (this.sideOfLine == LineSide.LEFT) {
			if (diff > 0) {
//				not in line
				left();
			} else {
				right();
			}
		}
	}
	
	public void left() {
		if (this.lastPanDiretion == PanDirection.LEFT) {
			this.pansInOneDirection++;
			if (this.pansInOneDirection <= Consts.LINE_MAX_FAST_PANS_WHILE_FOLLOWING) {
				this.fastPanLeft();
			} else if (this.pansInOneDirection <= Consts.LINE_MAX_FAST_PANS_WHILE_FOLLOWING + Consts.LINE_MAX_SLOW_PANS_WHILE_FOLLOWING) {
				this.slowPanLeft();
			} else if (this.pansInOneDirection <= Consts.LINE_MAX_FAST_PANS_WHILE_FOLLOWING + Consts.LINE_MAX_SLOW_PANS_WHILE_FOLLOWING + Consts.LINE_MAX_SLOW_ROTATIONS_WHILE_FOLLOWING){
				this.rotateLeft();
			} else {
				this.state = State.LINE_LOST;
			}
		} else {
			this.pansInOneDirection = 0;
			lastPanDiretion = PanDirection.LEFT;
		}
	}
	
	public void right() {
		if (this.lastPanDiretion == PanDirection.RIGHT) {
			this.pansInOneDirection++;
			if (this.pansInOneDirection <= Consts.LINE_MAX_FAST_PANS_WHILE_FOLLOWING) {
				this.fastPanRight();
			} else if (this.pansInOneDirection <= Consts.LINE_MAX_FAST_PANS_WHILE_FOLLOWING + Consts.LINE_MAX_SLOW_PANS_WHILE_FOLLOWING) {
				this.slowPanRight();
			} else if (this.pansInOneDirection <= Consts.LINE_MAX_FAST_PANS_WHILE_FOLLOWING + Consts.LINE_MAX_SLOW_PANS_WHILE_FOLLOWING + Consts.LINE_MAX_SLOW_ROTATIONS_WHILE_FOLLOWING){
				this.rotateRight();
			} else {
				this.state = State.LINE_LOST;
			}
		} else {
			this.pansInOneDirection = 0;
			lastPanDiretion = PanDirection.RIGHT;
		}
	}
	
	public void lineLost() {
		if (this.lineLostState == LineLostState.NOT_SEARCHED) {
//			start to search the line in the directino it should be 
//			(turn right when the robot is following the left line side and vice versa)
			if (this.sideOfLine == LineSide.LEFT) {
				if (searchLineLeft()) {
					this.state = State.ONLINE;
					return;
				}
			} else {
				if (searchLineRight()) {
					this.state = State.ONLINE;
					return;
				}
			}
//			when the line has not be found in the first direction
//			set search state
			if (this.state == State.LINE_LOST) {
				this.lineLostState = LineLostState.SEARCHED_FIRST_DIRECTION;
			}
		} else if (this.lineLostState == LineLostState.SEARCHED_FIRST_DIRECTION) {
//			start to search the line in the directino it should'nt be 
//			(turn left when the robot is following the left line side and vice versa)
//			but first rotate to the point where searching for the lane did start 
//			in the first searching
			if (this.sideOfLine == LineSide.LEFT) {
				if (searchLineRight()) {
					this.state = State.ONLINE;
					return;
				}
			} else {
				if (searchLineLeft()) {
					this.state = State.ONLINE;
					return;
				}
			}
//			when the line has not be found in the first direction
//			set search state
			if (this.state == State.LINE_LOST) {
				this.lineLostState = LineLostState.SEARCHED_SECOND_DIRECTION;
			}
		} else if (this.lineLostState == LineLostState.SEARCHED_SECOND_DIRECTION) {
//			assume that there is no line
			boolean finished = true;
			if (this.linePart == LinePart.FIRST) {
				finished = this.firstLinePartFinished();
			}
			LCD.clear();
			if (finished) {
				LCD.drawString("finished", 0, 0);
				this.state = State.LINE_FINISHED;
			} else {
				LCD.drawString("Error! Finished", 0, 0);
			}
		}
	}
	
	public boolean firstLinePartFinished() {
		UltrasonicSensor ultra = Consts.ULTRASONIC_SENSOR;
		int distance = 0;
		sensorMotor.rotateTo(Consts.SENSOR_MOTOR_LEFT);
		LCD.drawString("FinishedLine1?", 0, 2);
		distance += ultra.getDistance();
		LCD.drawString("dist1: "+distance, 0, 3);
		sensorMotor.rotateTo(Consts.SENSOR_MOTOR_RIGHT);
		distance = ultra.getDistance();
		LCD.drawString("disttot: "+distance, 0, 4);
		if (distance < 40) {
			LCD.drawString("Finished Line 1", 0, 5);
			return true;
		}
		return false;
	}
	
	public boolean searchLineLeft() {
		int arc = 0;
		while (arc < 95) {
			pilot.rotateLeftBy(Consts.LINE_SEARCH_ARC_ROBOT);
			arc += Consts.LINE_SEARCH_ARC_ROBOT;
			if (this.sensorSeesLine()) {
				this.sideOfLine = LineSide.LEFT;
				if (this.sideOfLine == LineSide.RIGHT) {
					pilot.rotateRightBy(45);
				} else {
					this.pilot.forwardStep(0.5);
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean searchLineRight() {
		int arc = 0;
		while (arc > 95) {
			pilot.rotateRightBy(Consts.LINE_SEARCH_ARC_ROBOT);
			arc -= Consts.LINE_SEARCH_ARC_ROBOT;
			if (this.sensorSeesLine()) {
				this.sideOfLine = LineSide.RIGHT;
				if (this.sideOfLine == LineSide.LEFT) {
					pilot.rotateLeftBy(45);
				} else {
					this.pilot.forwardStep(0.5);
				}
				return true;
			}
		}
		return false;
	}
	
	public void searchLineBeginning() {
//		drive a little bit forward and scan with the sensorMotor
		pilot.forwardStep(2);
		int arc = Consts.SENSOR_MOTOR_LEFT;
		sensorMotor.rotateTo(arc);
		while (arc > Consts.SENSOR_MOTOR_RIGHT) {
			sensorMotor.rotate(-Consts.LINE_SEARCH_ARC_ROBOT_ARM);
			if (sensorSeesLine()) {
				this.state = State.ONLINE;
				/**
				 * when line is found check for the side the line is on
				 * and rotate a bit to the other side to ensure that the robot is on the other side 
				 */
				if (arc < 0) {
					this.pilot.rotateRightBy(Consts.LINE_SEARCH_ARC_ROBOT_ARM);
					this.sideOfLine = LineSide.RIGHT;
				} else {
					this.sideOfLine = LineSide.LEFT;
					this.pilot.rotateLeftBy(Consts.LINE_SEARCH_ARC_ROBOT_ARM);
				}
				return;
			}
			if (arc > 0) {
				LCD.drawString("left", 0, 1);
			} else {
				LCD.drawString("right", 0, 1);
			}
			
			/**
			 * rotate the sensor and check the line again
			 */
			arc -= Consts.LINE_SEARCH_ARC_ROBOT_ARM;
		}
	}
	
	public void rotateLeft() {
		pilot.rotateLeftBy(10);
	}
	
	public void slowPanLeft() {
		rightMotor.setSpeed(pilot.getSpeedRight());
		leftMotor.setSpeed(pilot.getSpeedLeft() / 2);
		rightMotor.rotate(180, true);
		leftMotor.rotate(90);
	}
	
	public void fastPanLeft() {
		rightMotor.setSpeed(pilot.getSpeedRight());
		leftMotor.setSpeed(pilot.getSpeedLeft() / 2);
		rightMotor.rotate(360, true);
		leftMotor.rotate(180);
	}
	
	public void rotateRight() {
		pilot.rotateRightBy(10);
	}
	
	public void slowPanRight() {
		rightMotor.setSpeed(pilot.getSpeedRight() / 2);
		leftMotor.setSpeed(pilot.getSpeedLeft());
		leftMotor.rotate(180, true);
		rightMotor.rotate(90);
	}
	
	public void fastPanRight() {
		rightMotor.setSpeed(pilot.getSpeedRight() / 2);
		leftMotor.setSpeed(pilot.getSpeedLeft());
		leftMotor.rotate(360, true);
		rightMotor.rotate(180);
	}
	
	public boolean sensorSeesLine() {
		return (this.lightSensor.getLightValue() > Consts.LINE_SEPERATION_VALUE)?true:false;
	}

	@Override
	public void suppress() {
	}

}
