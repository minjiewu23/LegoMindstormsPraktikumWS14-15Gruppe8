import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.subsumption.Behavior;


public class BridgeFollowingBehaviour implements Behavior{
	
	private enum State {
		SEARCHING_BRIDGE_EDGE,
		FOLLOWING_EDGE,
		FINISHED_BRIDGE
	}
	
	private enum BridgeSide {
		LEFT,
		RIGHT,
		UNDEFINED
	}
	
	private State state = State.SEARCHING_BRIDGE_EDGE;
	
	
	
//	the side of the Bridge where the Robot is on
	private BridgeSide sideOfBridge = BridgeSide.UNDEFINED;
	
	private ContinuousPilot pilot;
	
	private NXTRegulatedMotor sensorMotor;
	
	private LightSensor lightSensor;
	
	public BridgeFollowingBehaviour(ContinuousPilot pilot) {
		this.pilot = pilot;
		this.sensorMotor = Consts.SENSOR_MOTOR;
		this.lightSensor = Consts.LIGHT_SENSOR;
	}

	@Override
	public boolean takeControl() {
		if (this.state == State.FINISHED_BRIDGE) {
			return false;
		}
		return true;
	}

	@Override
	public void action() {
		if (this.state == State.SEARCHING_BRIDGE_EDGE) {
			this.searchBridgeEdge();
		} else if (this.state == State.FOLLOWING_EDGE) {
			this.followBridgeEdge();
		}
	}
	
	public void searchBridgeEdge() {
		while (this.state == State.SEARCHING_BRIDGE_EDGE) {
			this.pilot.forwardStep(Consts.BRIDGE_STEPS_FORWARD_WHILE_SEARCHING_BRIDGE);
			this.sensorMotor.rotateTo(Consts.BRIDGE_SEARCH_MOTOR_POSITION_LEFT);
			if (this.sensorSeesBridge()) {
				state = State.FOLLOWING_EDGE;
				sideOfBridge = BridgeSide.LEFT;
			} else {
				this.sensorMotor.rotateTo(Consts.BRIDGE_SEARCH_MOTOR_POSITION_RIGHT);
				if (this.sensorSeesBridge()) {
					state = State.FOLLOWING_EDGE;
					sideOfBridge = BridgeSide.RIGHT;
				}
			}
		}
	}
	
	public void followBridgeEdge() {
		if (sideOfBridge == BridgeSide.LEFT) {
			sensorMotor.rotateTo(Consts.BRIDGE_SEARCH_MOTOR_POSITION_LEFT);
		} else {
			sensorMotor.rotateTo(Consts.BRIDGE_SEARCH_MOTOR_POSITION_RIGHT);
		}
		int diff = lightSensor.getLightValue() - Consts.BRIDGE_SEPERATION_VALUE;
		if (Math.abs(diff) < Consts.BRIDGE_EDGE_OFFSET) {
			this.pilot.forwardStep(Consts.BRIDGE_MAX_PANS_WHILE_FOLLOWING);
		} else {
			if (sideOfBridge == BridgeSide.LEFT) {
				if (diff > 0) {
//					sees bridge and is on left side of bridge
					this.pilot.smallPanLeft();
				} else {
					this.pilot.smallPanRight();
				}
			} else {
				if (diff > 0) {
//					sees bridge and is on right side of bridge
					this.pilot.smallPanRight();
				} else {
					this.pilot.smallPanLeft();
				}
			}
		}
	}
	
	public boolean sensorSeesBridge() {
		return (this.lightSensor.getLightValue() > Consts.BRIDGE_SEPERATION_VALUE)?true:false;
	}

	@Override
	public void suppress() {
		sensorMotor.rotateTo(0);
	}

}
