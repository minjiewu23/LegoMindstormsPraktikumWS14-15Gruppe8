import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.subsumption.Behavior;


public class FollowLineBehaviour implements Behavior{
	
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
	
	public FollowLineBehaviour(ContinuousPilot pilot) {
		this.pilot = pilot;
		this.sensorMotor = Consts.SENSOR_MOTOR;
		this.lightSensor = Consts.LIGHT_SENSOR;
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
		if (this.state == State.NO_LINE) {
			this.searchLineBeginning();
		} else if (this.state == State.LINE_LOST) {
			this.lineLost();
		} else if (this.state == State.ONLINE) {
			this.followLine();
		}
	}
	
	public void followLine() {
		int lightValue = lightSensor.getLightValue();
		int diff = lightValue - Consts.LINE_SEPERATION_VALUE;
		if (Math.abs(diff) < Consts.LINE_BORDER_OFFSET) {
			pilot.forwardStep(2);
		}
		if (diff > 0) {
			if (this.sideOfLine == LineSide.LEFT) {
//				online
				pilot.smallPanRight();
//				when there are too much pans in one direction the line is lost
				if (this.lastPanDiretion == PanDirection.RIGHT) {
					this.pansInOneDirection++;
					if (this.pansInOneDirection > Consts.LINE_MAX_PANS_WHILE_FOLLOWING) {
						this.state = State.LINE_LOST;
					}
				} else {
					this.lastPanDiretion = PanDirection.RIGHT;
					this.pansInOneDirection = 0;
				}
			} else {
//				offline
				pilot.smallPanLeft();
//				when there are too much pans in one direction the line is lost
				if (this.lastPanDiretion == PanDirection.LEFT) {
					this.pansInOneDirection++;
					if (this.pansInOneDirection > Consts.LINE_MAX_PANS_WHILE_FOLLOWING) {
						this.state = State.LINE_LOST;
					}
				} else {
					this.lastPanDiretion = PanDirection.LEFT;
					this.pansInOneDirection = 0;
				}
			}
		} else {
			if (this.sideOfLine == LineSide.RIGHT) {
//				online
				pilot.smallPanRight();
//				when there are too much pans in one direction the line is lost
				if (this.lastPanDiretion == PanDirection.RIGHT) {
					this.pansInOneDirection++;
					if (this.pansInOneDirection > Consts.LINE_MAX_PANS_WHILE_FOLLOWING) {
						this.state = State.LINE_LOST;
					}
				} else {
					this.lastPanDiretion = PanDirection.RIGHT;
					this.pansInOneDirection = 0;
				}
			} else {
//				offline
				pilot.smallPanLeft();
//				when there are too much pans in one direction the line is lost
				if (this.lastPanDiretion == PanDirection.LEFT) {
					this.pansInOneDirection++;
					if (this.pansInOneDirection > Consts.LINE_MAX_PANS_WHILE_FOLLOWING) {
						this.state = State.LINE_LOST;
					}
				} else {
					this.lastPanDiretion = PanDirection.LEFT;
					this.pansInOneDirection = 0;
				}
			}
		}
	}
	
	public void lineLost() {
		if (this.lineLostState == LineLostState.NOT_SEARCHED) {
//			start to search the line in the directino it should be 
//			(turn right when the robot is following the left line side and vice versa)
			if (this.sideOfLine == LineSide.LEFT) {
				searchLineRight();
			} else {
				searchLineLeft();
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
				pilot.rotateLeft90();
				if (searchLineLeft()) {
					this.state = State.ONLINE;
				}
			} else {
				pilot.rotateRight90();
				if (searchLineRight()) {
					this.state = State.ONLINE;
				}
			}
//			when the line has not be found in the first direction
//			set search state
			if (this.state == State.LINE_LOST) {
				this.lineLostState = LineLostState.SEARCHED_SECOND_DIRECTION;
			}
		} else if (this.lineLostState == LineLostState.SEARCHED_SECOND_DIRECTION) {
//			assume that there is no line
			this.state = State.LINE_FINISHED;
		}
	}
	
	public boolean searchLineLeft() {
		pilot.resetPose();
		while (pilot.getHeadingSinceLastPoseReset() < 95) {
			pilot.rotateLeftBy(Consts.LINE_SEARCH_ARC);
			if (this.sensorSeesLine()) {
				this.sideOfLine = LineSide.RIGHT;
				this.pilot.rotateRightBy(Consts.LINE_SEARCH_ARC);
				return true;
			}
		}
		return false;
	}
	
	public boolean searchLineRight() {
		pilot.resetPose();
		while (pilot.getHeadingSinceLastPoseReset() > -95) {
			pilot.rotateRightBy(Consts.LINE_SEARCH_ARC);
			if (this.sensorSeesLine()) {
				this.sideOfLine = LineSide.LEFT;
				this.pilot.rotateLeftBy(Consts.LINE_SEARCH_ARC);
				return true;
			}
		}
		return false;
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
					this.pilot.rotateRightBy(Consts.LINE_SEARCH_ARC);
					this.sideOfLine = LineSide.LEFT;
				} else {
					this.sideOfLine = LineSide.RIGHT;
					this.pilot.rotateLeftBy(Consts.LINE_SEARCH_ARC);
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
		
	}

}
