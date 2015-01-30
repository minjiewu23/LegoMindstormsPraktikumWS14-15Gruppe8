import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class Consts {
	
	/**
	 * general Constants
	 */
	
	public static final NXTRegulatedMotor LEFT_MOTOR = Motor.A;

	public static final NXTRegulatedMotor RIGHT_MOTOR = Motor.B;
	
	public static final NXTRegulatedMotor SENSOR_MOTOR = Motor.C;
	
	
	public static final SensorPort LIGHT_SENSOR_PORT = SensorPort.S1;

	public static final SensorPort ULTRASONIC_SENSOR_PORT = SensorPort.S2;

	public static final SensorPort LEFT_TOUCH_SENSOR_PORT = SensorPort.S3;

	public static final SensorPort RIGHT_TOUCH_SENSOR_PORT = SensorPort.S4;

	public static final LightSensor LIGHT_SENSOR = new LightSensor(LIGHT_SENSOR_PORT);

	public static final UltrasonicSensor ULTRASONIC_SENSOR = new UltrasonicSensor(ULTRASONIC_SENSOR_PORT);
	
	public static final TouchSensor TOUCH_SENSOR_LEFT = new TouchSensor(LEFT_TOUCH_SENSOR_PORT);

	public static final TouchSensor TOUCH_SENSOR_RIGHT = new TouchSensor(RIGHT_TOUCH_SENSOR_PORT);
	
	/**
	 * Sensors Define
	 */
	
	public static final DifferentialPilot PILOT = new DifferentialPilot(20, 100, Motor.A, Motor.B, true);
	
	/**
	 * LineFollower Constants
	 */
	
	public static final int LINE_SEPERATION_VALUE = 35;
	
	public static final int LINE_SEARCH_ARC_ROBOT = 4;
	
	public static final int LINE_SEARCH_ARC_ROBOT_ARM = 10;
	
	public static final int LINE_BORDER_OFFSET = 5;
	
	public static final int LINE_MAX_FAST_PANS_WHILE_FOLLOWING = 1;
	
	public static final int LINE_MAX_SLOW_PANS_WHILE_FOLLOWING = 2;
	
	public static final int LINE_MAX_SLOW_ROTATIONS_WHILE_FOLLOWING = 4;
	
	public static final int LINE_PAN_TIME = 100;

	public static final int SENSOR_MOTOR_LEFT = 90;

	public static final int SENSOR_MOTOR_RIGHT = -90;
	
	public static final int MAX_SEARCH_ARC = 120;
	
	/**
	 * BridgeRider Constants
	 */
	
	public static final int BRIDGE_SEPERATION_VALUE = 30;
	
	public static final int BRIDGE_SEARCH_ARC = 4;
	
	public static final float BRIDGE_STEPS_FORWARD_WHILE_SEARCHING_BRIDGE = 2;
	
	public static final int BRIDGE_SEARCH_MOTOR_POSITION_RIGHT = -90;
	
	public static final int BRIDGE_SEARCH_MOTOR_POSITION_LEFT = 90;
	
	public static final int BRIDGE_EDGE_OFFSET = 2;
	
	public static final int BRIDGE_MAX_PANS_WHILE_FOLLOWING = 1;

	private Consts() {
		throw new AssertionError();
	}
}
